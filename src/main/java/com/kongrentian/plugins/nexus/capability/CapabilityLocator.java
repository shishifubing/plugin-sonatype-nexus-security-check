package com.kongrentian.plugins.nexus.capability;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;


@Named
public class CapabilityLocator {
  private static final Logger LOG = LoggerFactory.getLogger(CapabilityLocator.class);

  private static final String capabilityClassName = Capability.class.getSimpleName();

  private final CapabilityRegistry capabilityRegistry;

  @Inject
  public CapabilityLocator(final CapabilityRegistry capabilityRegistry) {
    this.capabilityRegistry = capabilityRegistry;
  }

  public boolean isCapabilityActive() {
    CapabilityReference reference = getCapabilityReference();
    return reference != null && reference.context().isActive();
  }

  public CapabilityConfiguration getCapabilityConfiguration() {
    return getCapabilityReference().capabilityAs(Capability.class).getConfig();
  }

  private boolean isCapability(CapabilityReference reference) {
    return capabilityClassName.equals(reference.capability().getClass().getSimpleName());
  }

  private CapabilityReference getCapabilityReference() {
    CapabilityReference reference = capabilityRegistry
            .getAll()
            .stream()
            .filter(this::isCapability)
            .findFirst()
            .orElse(null);
    if (reference == null) {
      LOG.debug("Security Configuration capability does not exist.");
    }
    return reference;
  }

}
