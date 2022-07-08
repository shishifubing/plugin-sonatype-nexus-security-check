package com.kongrentian.plugins.nexus.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilityIdentity;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.common.entity.EntityId;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


@Named
public class SecurityCapabilityHelper {
  private static final Logger LOG = LoggerFactory.getLogger(SecurityCapabilityHelper.class);

  private static final String capabilityClassName = SecurityCapability.class.getSimpleName();

  private final CapabilityRegistry capabilityRegistry;
  private CapabilityReference capabilityReference;

  @Inject
  public SecurityCapabilityHelper(final CapabilityRegistry capabilityRegistry) {
    this.capabilityRegistry = capabilityRegistry;
  }

  public SecurityCapabilityConfiguration getCapabilityConfiguration() {
    return getCapabilityReference().capabilityAs(SecurityCapability.class).getConfig();
  }

  public void setCapabilityReference(CapabilityIdentity capabilityIdentity) {
    capabilityReference = capabilityRegistry.get(capabilityIdentity);
  }

  @Nonnull
  public CapabilityReference getCapabilityReference() {
    if (capabilityReference == null) {
      capabilityReference = capabilityRegistry.add(
              CapabilityType.capabilityType(SecurityCapabilityDescriptor.CAPABILITY_ID),
              false,
              null,
              null);
    }
    return capabilityReference;
  }

  public boolean isCapabilityActive() {
    return getCapabilityReference().context().isActive();
  }

  public SecurityClient createClient() throws NoSuchAlgorithmException, KeyManagementException {
    return new SecurityClient(Objects.requireNonNull(
            getCapabilityConfiguration()));
  }

}
