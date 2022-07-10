package com.kongrentian.plugins.nexus.main;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.model.monitoring_information.MonitoringInformation;
import com.kongrentian.plugins.nexus.model.monitoring_information.MonitoringInformationScanResult;
import com.kongrentian.plugins.nexus.model.request_information.RequestInformation;
import com.kongrentian.plugins.nexus.model.scan_result.ScanResult;
import com.kongrentian.plugins.nexus.monitoring.Monitoring;
import com.kongrentian.plugins.nexus.scanner.AbstractScanner;
import com.kongrentian.plugins.nexus.scanner.LocalScanner;
import com.kongrentian.plugins.nexus.scanner.RemoteScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.collect.AttributesMap;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.storage.ComponentStore;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.repository.view.*;
import org.sonatype.nexus.repository.view.handlers.ContributedHandler;
import org.sonatype.nexus.security.SecurityFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


@Named
@Singleton
public class RequestHandler implements ContributedHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);
    private final SecurityCapabilityHelper securityCapabilityHelper;
    private final RemoteScanner remoteScanner;
    private final Monitoring monitoring;
    private final LocalScanner localScanner;
    private final ComponentStore componentStore;


    @Inject
    public RequestHandler(final SecurityCapabilityHelper securityCapabilityHelper,
                          final RemoteScanner remoteScanner,
                          final LocalScanner localScanner,
                          final Monitoring monitoring,
                          final ComponentStore componentStore) {
        this.securityCapabilityHelper = securityCapabilityHelper;
        this.remoteScanner = remoteScanner;
        this.monitoring = monitoring;
        this.localScanner = localScanner;
        this.componentStore = componentStore;
    }

    @Nonnull
    @Override
    public Response handle(@Nonnull Context context) throws Exception {
        Response response = context.proceed();
        if (!securityCapabilityHelper.isCapabilityActive()) {
            return response;
        }
        Repository repository = context.getRepository();
        if (!ProxyType.NAME.equals(repository.getType().getValue())) {
            return response;
        }
        Request request = context.getRequest();
        Payload payload = response.getPayload();
        if (!(payload instanceof Content)) {
            return response;
        }
        Content content = (Content) payload;
        AttributesMap attributes = content.getAttributes();
        Asset asset = attributes.get(Asset.class);
        if (asset == null) {
            return response;
        }
        Component component = componentStore.read(asset.componentId());

        SecurityCapabilityConfiguration config = securityCapabilityHelper.getCapabilityConfiguration();
        String userId = (String) request.getAttributes().get(SecurityFilter.ATTR_USER_ID);
        if (userId == null) {
            userId = config.getMonitoringAnonymousUserId();
        }
        RequestInformation information = new RequestInformation(
                userId, repository, content, asset, component, request);
        MonitoringInformation results = new MonitoringInformation(information);
        for (AbstractScanner scanner : getScanners(config)) {
            ScanResult result = scanner.scan(information);
            results.add(new MonitoringInformationScanResult(scanner.getClass(), result));
            if (!result.isAllowed()) {
                results.setAllowed(false);
                break;
            }
        }
        LOG.info("RESULTS: " + SecurityCapabilityHelper.jsonMapper
                .writeValueAsString(results));
        monitoring.send(results);
        if (results.isAllowed()) {
            return response;
        }
        throw new RuntimeException(format("Asset '%s' is not allowed: %s",
                information.getRequest().getPath(),
                results.getLastReason()));
    }

    private List<AbstractScanner> getScanners(SecurityCapabilityConfiguration config) {
        List<AbstractScanner> scanners = new ArrayList<>();
        if (config.isEnableScanLocal()) {
            scanners.add(localScanner);
        }
        if (config.isEnableScanRemote()) {
            scanners.add(remoteScanner);
        }
        return scanners;
    }

}
