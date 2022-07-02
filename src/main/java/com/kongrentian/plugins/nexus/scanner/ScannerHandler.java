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
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.security.SecurityFacet;
import org.sonatype.nexus.repository.security.VariableResolverAdapter;
import org.sonatype.nexus.repository.types.ProxyType;
import org.sonatype.nexus.repository.view.Context;
import org.sonatype.nexus.repository.view.Response;
import org.sonatype.nexus.repository.view.handlers.ContributedHandler;
import org.sonatype.nexus.repository.view.handlers.SecurityHandler;

import java.util.Map;

import static java.lang.String.format;


@Named
@Singleton
public class ScannerHandler implements ContributedHandler {
  private static final Logger LOG = LoggerFactory.getLogger(Scanner.class);

  private final ConfigurationHelper configurationHelper;
  private final Scanner scanner;

  private final ClientAPI clientAPI;
  private CapabilityConfiguration configuration;

  private final SecurityHandler securityHandler;
  private final VariableResolverAdapter variableResolverAdapter;

  @Inject
  public ScannerHandler(ConfigurationHelper configurationHelper,
                        SecurityHandler securityHandler,
                        VariableResolverAdapter variableResolverAdapter,
                        Scanner scanner) {
    this.variableResolverAdapter = variableResolverAdapter;
    this.securityHandler = securityHandler;
    this.configurationHelper = configurationHelper;
    this.scanner = scanner;
    this.clientAPI = configurationHelper.getSecurityClientAPI();
    if (configuration == null) {
      configuration = configurationHelper.getConfiguration();
    }
  }

  @Nonnull
  @Override
  public Response handle(@Nonnull Context context) throws Exception {
    Response response = context.proceed();
    if (!configurationHelper.isCapabilityEnabled()) {
      return response;
    }
    LOG.info("REQUEST - {}", context.getRequest());
    for (Map.Entry<String, Object> entry: context.getRequest().getAttributes().entries()) {
      LOG.info("REQUEST ENTRY({}) {}: {}", entry.getValue().getClass(), entry.getKey(), entry.getValue());
    }

    //SecurityFacet securityFacet = context.getRepository().facet(SecurityFacet.class);
    // variableResolverAdapter.fromRequest()
    Repository repository = context.getRepository();
    if (!ProxyType.NAME.equals(repository.getType().getValue())) {
      return response;
    }
    ScanResult scanResult = scanner.scan(response, repository, clientAPI);
    if (scanResult == null || scanResult.allowed) {
      return response;
    }
    throw new RuntimeException(format("Asset '%s' is not allowed: %s",
                                      context.getRequest().getPath(),
                                      scanResult.reason));
  }

}
