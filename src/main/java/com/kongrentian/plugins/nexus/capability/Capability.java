package com.kongrentian.plugins.nexus.capability;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import org.sonatype.nexus.capability.CapabilitySupport;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.API_TOKEN;

@Named(CapabilityDescriptor.CAPABILITY_ID)
public class Capability extends CapabilitySupport<CapabilityConfiguration> {

  @Inject
  public Capability() {
  }

  @Override
  protected CapabilityConfiguration createConfig(Map<String, String> properties) {
    return new CapabilityConfiguration(properties);
  }

  @Override
  public boolean isPasswordProperty(String propertyName) {
    return API_TOKEN.propertyKey().equals(propertyName);
  }

  @Override
  protected void configure(CapabilityConfiguration config) throws Exception {
    super.configure(config);
  }
}
