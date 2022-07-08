package com.kongrentian.plugins.nexus.capability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.sonatype.nexus.capability.CapabilityIdentity;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.capability.CapabilityType;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


@Named
public class SecurityCapabilityHelper {
  private final CapabilityRegistry capabilityRegistry;
  private CapabilityReference securityCapabilityReference;

  @Inject
  public SecurityCapabilityHelper(final CapabilityRegistry capabilityRegistry) {
    this.capabilityRegistry = capabilityRegistry;
  }

  @Nonnull
  public SecurityCapabilityConfiguration getCapabilityConfiguration() {
    return getSecurityCapabilityReference().capabilityAs(SecurityCapability.class).getConfig();
  }

  public void setSecurityCapabilityReference(CapabilityIdentity capabilityIdentity) {
    securityCapabilityReference = capabilityRegistry.get(capabilityIdentity);
  }

  @Nonnull
  public CapabilityReference getSecurityCapabilityReference() {
    if (securityCapabilityReference != null) {
      return securityCapabilityReference;
    }
    CapabilityReference capabilityReference = capabilityRegistry.get(
            SecurityCapabilityDescriptor.CAPABILITY_IDENTITY);
    if (capabilityReference == null) {
      securityCapabilityReference = capabilityRegistry.add(
              CapabilityType.capabilityType(SecurityCapabilityDescriptor.CAPABILITY_ID),
              false,
              null,
              null);
    } else {
      securityCapabilityReference = capabilityReference;
    }
    return securityCapabilityReference;
  }


  public boolean isCapabilityActive() {
    return getSecurityCapabilityReference().context().isActive();
  }

  public SecurityClient createClient() throws NoSuchAlgorithmException, KeyManagementException {
    return new SecurityClient(getCapabilityConfiguration());
  }

}
