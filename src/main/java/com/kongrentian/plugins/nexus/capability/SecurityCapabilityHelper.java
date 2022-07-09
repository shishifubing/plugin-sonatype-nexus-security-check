package com.kongrentian.plugins.nexus.capability;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.sonatype.nexus.capability.CapabilityIdentity;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.capability.CapabilityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
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

    private static boolean isTypeEqual(CapabilityReference reference) {
        return SecurityCapabilityDescriptor.CAPABILITY_TYPE
                .equals(reference.context().type());
    }

    @Nonnull
    public SecurityCapabilityConfiguration getCapabilityConfiguration() {
        return getSecurityCapabilityReference().capabilityAs(SecurityCapability.class).getConfig();
    }

    @Nonnull
    public CapabilityReference getSecurityCapabilityReference() {
        if (securityCapabilityReference != null) {
            return securityCapabilityReference;
        }
        CapabilityReference capabilityReference = capabilityRegistry.get(
                        SecurityCapabilityHelper::isTypeEqual)
                .stream().findFirst().orElse(null);
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

    public void setSecurityCapabilityReference(CapabilityIdentity capabilityIdentity) {
        securityCapabilityReference = capabilityRegistry.get(capabilityIdentity);
    }

    public boolean isCapabilityActive() {
        return getSecurityCapabilityReference().context().isActive();
    }

    public SecurityClient createClient() throws NoSuchAlgorithmException, KeyManagementException {
        return new SecurityClient(getCapabilityConfiguration());
    }

}
