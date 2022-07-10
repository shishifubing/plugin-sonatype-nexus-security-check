package com.kongrentian.plugins.nexus.capability;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;


@Named
public class SecurityCapabilityHelper {

    public final static ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // to deserialize joda datetime
            .registerModule(new JodaModule());
    public final static ObjectMapper yamlMapper = new ObjectMapper(
            new YAMLFactory().disable(
                    YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
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
