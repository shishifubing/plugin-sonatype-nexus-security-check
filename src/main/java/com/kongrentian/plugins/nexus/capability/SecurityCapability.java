package com.kongrentian.plugins.nexus.capability;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.sonatype.nexus.capability.CapabilitySupport;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

  private final SecurityCapabilityHelper securityCapabilityHelper;
  @Inject
  public SecurityCapability(SecurityCapabilityHelper securityCapabilityHelper) {
    this.securityCapabilityHelper = securityCapabilityHelper;
    securityCapabilityHelper.setCapabilityReference(context().id());
  }

  @Override
  protected SecurityCapabilityConfiguration createConfig(
          Map<String, String> properties) throws JsonProcessingException {
    return new SecurityCapabilityConfiguration(properties);
  }

  @Nullable
  @Override
  protected String renderStatus() throws Exception {
    return super.renderStatus();
  }

  @Override
  public void onCreate() throws Exception {
    super.onCreate();
    securityCapabilityHelper.setCapabilityReference(context().id());
  }

  @Override
  public void onRemove() throws Exception {
    securityCapabilityHelper.setCapabilityReference(null);
    super.onRemove();
  }

  @Override
  protected void onActivate(SecurityCapabilityConfiguration config) throws Exception {
    super.onActivate(config);
  }

  @Override
  public void onPassivate() throws Exception {
    super.onPassivate();
  }
}
