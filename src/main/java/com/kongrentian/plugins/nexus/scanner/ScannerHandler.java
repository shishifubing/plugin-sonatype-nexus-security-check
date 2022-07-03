package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.Client;
import com.kongrentian.plugins.nexus.model.ScanResult;
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

    private final ConfigurationHelper configurationHelper;
    private final Scanner scanner;

    private final Client client;

    @Inject
    public ScannerHandler(ConfigurationHelper configurationHelper,
                          Scanner scanner) {
        this.configurationHelper = configurationHelper;
        this.scanner = scanner;
        this.client = configurationHelper.getSecurityClient();
    }

    @Nonnull
    @Override
    public Response handle(@Nonnull Context context) throws Exception {
        Response response = context.proceed();
        if (!configurationHelper.isCapabilityEnabled()) {
            return response;
        }
        Repository repository = context.getRepository();
        if (!ProxyType.NAME.equals(repository.getType().getValue())) {
            return response;
        }
        Request request = context.getRequest();
        String user = (String) request.getAttributes()
                .get(SecurityFilter.ATTR_USER_ID);
        ScanResult scanResult = scanner.scan(response, repository,
                client, user);
        if (scanResult == null || scanResult.allowed) {
            return response;
        }
        throw new RuntimeException(format("Asset '%s' is not allowed: %s",
                request.getPath(), scanResult.reason));
    }
}
