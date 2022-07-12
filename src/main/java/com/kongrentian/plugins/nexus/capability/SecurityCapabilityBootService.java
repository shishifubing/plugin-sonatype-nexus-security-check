package com.kongrentian.plugins.nexus.capability;

import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.nexus.capability.CapabilityRegistry;
import org.sonatype.nexus.common.app.ManagedLifecycle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.sonatype.nexus.common.app.ManagedLifecycle.Phase.CAPABILITIES;


@Named
@Singleton
@ManagedLifecycle(phase = CAPABILITIES)
public class SecurityCapabilityBootService
        extends LifecycleSupport {

    private final CapabilityRegistry capabilityRegistry;

    @Inject
    public SecurityCapabilityBootService(final CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = capabilityRegistry;
    }

    @Override
    protected void doStart() {
        SecurityCapabilityHelper.getOrCreateCapability(capabilityRegistry);
    }
}

