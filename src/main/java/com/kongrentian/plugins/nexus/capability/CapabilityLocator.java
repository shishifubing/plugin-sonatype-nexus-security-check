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

  private final CapabilityRegistry capabilityRegistry;

  @Inject
  public CapabilityLocator(final CapabilityRegistry capabilityRegistry) {
    this.capabilityRegistry = capabilityRegistry;
  }

  public boolean isSecurityCapabilityActive() {
    LOG.debug("List all available Nexus capabilities");
    CapabilityReference reference = capabilityRegistry.getAll()
                                                      .stream()
                                                      .peek(e -> {
                                                        org.sonatype.nexus.capability.Capability capability = e.capability();
                                                        LOG.debug("  {}", capability);
                                                      })
                                                      .filter(e -> Capability.class.getSimpleName().equals(e.capability().getClass().getSimpleName()))
                                                      .findFirst().orElse(null);
    if (reference == null) {
      LOG.debug("Security Configuration capability does not exist.");
      return false;
    }

    return reference.context().isActive();
  }

  public CapabilityConfiguration getSecurityCapabilityConfiguration() {
    CapabilityReference reference = capabilityRegistry.getAll().stream()
                                                      .filter(e -> Capability.class.getSimpleName().equals(e.capability().getClass().getSimpleName()))
                                                      .findFirst().orElse(null);
    if (reference == null) {
      LOG.debug("Security Configuration capability is not created.");
      return null;
    }

    Capability Capability = reference.capabilityAs(Capability.class);
    return Capability.getConfig();
  }
}
