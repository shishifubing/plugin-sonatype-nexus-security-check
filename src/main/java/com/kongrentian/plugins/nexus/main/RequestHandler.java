package com.kongrentian.plugins.nexus.main;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.model.MonitoringInformation;
import com.kongrentian.plugins.nexus.model.RequestInformation;
import com.kongrentian.plugins.nexus.model.ScanResult;
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
    private final SecurityClient securityClient;


    @Inject
    public RequestHandler(final SecurityCapabilityHelper securityCapabilityHelper,
                          final RemoteScanner remoteScanner,
                          final LocalScanner localScanner,
                          final Monitoring monitoring,
                          final ComponentStore componentStore,
                          final SecurityClient securityClient) {
        this.securityCapabilityHelper = securityCapabilityHelper;
        this.remoteScanner = remoteScanner;
        this.monitoring = monitoring;
        this.localScanner = localScanner;
        this.componentStore = componentStore;
        this.securityClient = securityClient;
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

        String userId = (String) request.getAttributes().get(SecurityFilter.ATTR_USER_ID);
        RequestInformation information = new RequestInformation(
                userId, repository, content, asset, component, request);
        SecurityCapabilityConfiguration config = securityCapabilityHelper.getCapabilityConfiguration();
        MonitoringInformation results = new MonitoringInformation(information);
        for (AbstractScanner scanner : getScanners(config)) {
            ScanResult result = scanner.scan(information);
            results.add(scanner.asResult(result));
            if (!result.isAllowed()) {
                results.setAllowed(false);
                break;
            }
        }
        LOG.info("RESULTS: " + securityClient.getMapper().writeValueAsString(results));
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
