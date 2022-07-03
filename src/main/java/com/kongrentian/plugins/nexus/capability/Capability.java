package com.kongrentian.plugins.nexus.capability;

import javax.inject.Named;
import java.util.Map;

import org.sonatype.nexus.capability.CapabilitySupport;

@Named(CapabilityDescriptor.CAPABILITY_ID)
public class Capability extends CapabilitySupport<CapabilityConfiguration> {

  @Override
  protected CapabilityConfiguration createConfig(Map<String, String> properties) {
    return new CapabilityConfiguration(properties);
  }
}
