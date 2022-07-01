package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.ClientAPI;
import com.kongrentian.plugins.nexus.capability.CapabilityConfiguration;
import com.kongrentian.plugins.nexus.model.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.collect.AttributesMap;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Context;
import org.sonatype.nexus.repository.view.Response;
import org.sonatype.nexus.repository.view.handlers.ContributedHandler;

@Named
@Singleton
public class ScannerHandler implements ContributedHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ScannerHandler.class);

  private final ConfigurationHelper configurationHelper;
  private final Scanner scanner;

  private final ClientAPI clientAPI;
  private CapabilityConfiguration configuration;

  @Inject
  public ScannerHandler(final ConfigurationHelper configurationHelper,
                        Scanner scanner) {
    this.configurationHelper = configurationHelper;
    this.scanner = scanner;
    clientAPI = configurationHelper.getSecurityClientAPI();
    if (configuration == null) {
      configuration = configurationHelper.getConfiguration();
    }
  }

  @Nonnull
  @Override
  public Response handle(@Nonnull Context context) throws Exception {
    LOG.info(context.getRequest().toString());
    LOG.info(context.getRequest().toString());
    Response response = context.proceed();
    LOG.info(context.getRepository().toString());
    LOG.info(response.toString());
    LOG.info(response.getHeaders().toString());
    LOG.info(response.getPayload().toString());
    LOG.info(((Content) response.getPayload()).toString());
    if (!configurationHelper.isCapabilityEnabled()) {
      LOG.debug("Capability is not enabled.");
      return response;
    }
    Repository repository = context.getRepository();
    if (!ProxyType.NAME.equals(repository.getType().getValue())) {
      LOG.warn("Only proxy repositories are supported: {} - {}",
              repository.getName(), repository.getType());
      return response;
    }
    AttributesMap attributes = context.getAttributes();
    ScanResult scanResult = scanner.scan(response, repository, clientAPI);
    if (scanResult == null || scanResult.allowed) {
      return response;
    }
    throw new RuntimeException("not allowed");
  }

}
