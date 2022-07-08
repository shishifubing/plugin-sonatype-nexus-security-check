package com.kongrentian.plugins.nexus.capability;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import org.sonatype.nexus.capability.CapabilitySupport;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

  private final SecurityCapabilityHelper securityCapabilityHelper;

  @Inject
  public SecurityCapability(SecurityCapabilityHelper securityCapabilityHelper) {
    this.securityCapabilityHelper = securityCapabilityHelper;
  }

  @Override
  protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
    return new SecurityCapabilityConfiguration(properties);
  }

  @Nullable
  @Override
  protected String renderStatus() throws Exception {
    return super.renderStatus();
  }

  /**
   * in case the capability gets removed
   * @throws Exception
   */
  @Override
  public void onRemove() throws Exception {
    securityCapabilityHelper.setSecurityCapabilityReference(null);
    super.onRemove();
  }

}
