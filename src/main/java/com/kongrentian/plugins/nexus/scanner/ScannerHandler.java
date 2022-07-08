package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.model.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.repository.view.Context;
import org.sonatype.nexus.repository.view.Request;
import org.sonatype.nexus.repository.view.Response;
import org.sonatype.nexus.repository.view.handlers.ContributedHandler;
import org.sonatype.nexus.security.SecurityFilter;


import static java.lang.String.format;


@Named
@Singleton
public class ScannerHandler implements ContributedHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ScannerHandler.class);
    private final SecurityCapabilityHelper securityCapabilityHelper;
    private final Scanner scanner;

    private final SecurityClient securityClient;

    @Inject
    public ScannerHandler(SecurityCapabilityHelper securityCapabilityHelper,
                          Scanner scanner) throws Exception {
        this.securityCapabilityHelper = securityCapabilityHelper;
        this.scanner = scanner;
        this.securityClient = securityCapabilityHelper.createClient();
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
        String user = (String) request.getAttributes()
                .get(SecurityFilter.ATTR_USER_ID);
        ScanResult scanResult = null;
        try {
             scanResult = scanner.scan(response, repository,
                    securityClient, user);
        } catch (Exception exception) {
            if (securityClient.getConfiguration().isFailOnScanErrors()) {
                throw exception;
            }
            LOG.error("Could not check asset '{}' because of '{}'",
                    request.getPath(), exception);
        }
        if (scanResult == null || scanResult.allowed) {
            return response;
        }
        throw new RuntimeException(format("Asset '%s' is not allowed: %s",
                request.getPath(), scanResult.reason));
    }
}
