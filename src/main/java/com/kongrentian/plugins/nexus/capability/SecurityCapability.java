package com.kongrentian.plugins.nexus.capability;

import javax.inject.Named;
import java.util.Map;

import org.sonatype.nexus.capability.CapabilitySupport;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

  @Override
  protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
    return new SecurityCapabilityConfiguration(properties);
  }
}
