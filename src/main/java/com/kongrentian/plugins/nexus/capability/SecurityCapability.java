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
  }

  @Override
  protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
    return new SecurityCapabilityConfiguration(properties);
  }

  private void set() {
    securityCapabilityHelper.setCapabilityReference(context().id());
  }
  private void unset() {
    securityCapabilityHelper.setCapabilityReference(null);
  }

  @Nullable
  @Override
  protected String renderStatus() throws Exception {
    return super.renderStatus();
  }

  @Override
  public void onCreate() throws Exception {
    super.onCreate();
    set();
  }

  @Override
  public void onRemove() throws Exception {
    unset();
    super.onRemove();
  }

  @Override
  public void onActivate() throws Exception {
    super.onActivate();
    set();
  }

}
