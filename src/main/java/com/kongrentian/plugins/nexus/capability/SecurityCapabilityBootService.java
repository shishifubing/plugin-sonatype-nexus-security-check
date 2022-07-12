package com.kongrentian.plugins.nexus.capability;

import com.kongrentian.plugins.nexus.main.BundleHelper;
import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.nexus.common.app.ManagedLifecycle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;


@Named
@Singleton
@ManagedLifecycle(phase = ManagedLifecycle.Phase.TASKS)
public class SecurityCapabilityBootService
        extends LifecycleSupport {

    private final BundleHelper bundleHelper;

    @Inject
    public SecurityCapabilityBootService(final BundleHelper bundleHelper) {
        this.bundleHelper = bundleHelper;
    }

    @Override
    protected void doStart() {
        bundleHelper.getOrCreateCapability();
    }
}

