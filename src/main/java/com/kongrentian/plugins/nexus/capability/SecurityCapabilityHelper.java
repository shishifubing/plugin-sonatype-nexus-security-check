package com.kongrentian.plugins.nexus.capability;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;


@Named
public class SecurityCapabilityHelper {
    private final CapabilityRegistry capabilityRegistry;
    private CapabilityReference securityCapabilityReference;
    private SecurityClient securityClient;

    @Inject
    public SecurityCapabilityHelper(final CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = capabilityRegistry;
    }

    private static boolean isTypeEqual(CapabilityReference reference) {
        return SecurityCapabilityDescriptor.CAPABILITY_TYPE
                .equals(reference.context().type());
    }

    protected static String errorMessage(@Nullable Exception exception) {
        return exception == null ?
                " is valid\n" :
                " is not valid\n" + exception.getMessage();
    }

    @Nonnull
    public SecurityCapabilityConfiguration getCapabilityConfiguration() {
        return getSecurityCapabilityReference().capabilityAs(SecurityCapability.class).getConfig();
    }

    @Nonnull
    private CapabilityReference getSecurityCapabilityReference() {
        if (securityCapabilityReference != null) {
            return securityCapabilityReference;
        }
        CapabilityReference capabilityReference =
                capabilityRegistry
                        .get(SecurityCapabilityHelper::isTypeEqual)
                        .stream()
                        .findFirst()
                        .orElse(null);
        if (capabilityReference == null) {
            securityCapabilityReference = capabilityRegistry.add(
                    SecurityCapabilityDescriptor.CAPABILITY_TYPE,
                    false,
                    "Automatically created at " + Instant.now().toString(),
                    null);
        } else {
            securityCapabilityReference = capabilityReference;
        }
        return securityCapabilityReference;
    }

    protected void unsetCapabilityReference() {
        securityCapabilityReference = null;
        securityClient = null;
    }

    protected void recreateSecurityClient() {
        securityClient = new SecurityClient(getCapabilityConfiguration());
    }

    public boolean isCapabilityActive() {
        return getSecurityCapabilityReference().context().isActive();
    }

    public SecurityClient getClient() {
        if (securityClient == null) {
            recreateSecurityClient();
        }
        return securityClient;
    }

}
