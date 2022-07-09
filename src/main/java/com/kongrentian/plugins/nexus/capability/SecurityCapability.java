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
  protected String renderStatus() {
    SecurityCapabilityConfiguration config = getConfig();
    StringBuilder builder = new StringBuilder();
    Exception lastModified = config.getScanLocalLastModifiedException();
    Exception whiteList = config.getScanLocalWhiteListException();
    builder.append(SecurityCapabilityKey.SCAN_LOCAL_LAST_MODIFIED.propertyKey());
    builder.append(SecurityCapabilityHelper.errorMessage(lastModified));
    builder.append("\n\r\n\r===================================\n\r\n\r");
    builder.append(SecurityCapabilityKey.SCAN_LOCAL_WHITE_LIST.propertyKey());
    builder.append(SecurityCapabilityHelper.errorMessage(whiteList));
    return builder.toString();
  }

  /**
   * in case the capability gets removed
   */
  @Override
  public void onRemove() throws Exception {
    securityCapabilityHelper.setSecurityCapabilityReference(null);
    super.onRemove();
  }

}
