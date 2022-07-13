package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityBooterSupport;
import org.sonatype.nexus.capability.CapabilityRegistry;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SecurityCapabilityBooter
        extends CapabilityBooterSupport {
    @Override
    protected void boot(final CapabilityRegistry registry) throws Exception {
        maybeAddCapability(
                registry,
                SecurityCapabilityDescriptor.CAPABILITY_TYPE,
                true,
                null,
                SecurityCapabilityFormField.createDefaultProperties()
        );
    }
}