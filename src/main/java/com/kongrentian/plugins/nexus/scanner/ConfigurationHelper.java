package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.SecurityClient;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class ConfigurationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationHelper.class);

    @Inject
    private Provider<SecurityCapabilityLocator> locatorProvider;

    @Nullable
    public SecurityClient getSecurityClient() {
        SecurityCapabilityLocator locator = locatorProvider.get();
        if (locator == null) {
            LOG.warn("SecurityClient cannot be built because SecurityCapabilityLocator is null");
            return null;
        }
        try {
            return new SecurityClient(locator.getCapabilityConfiguration());
        } catch (Exception exception) {
            LOG.error("SecurityClient could not be created", exception);
            return null;
        }

    }

    public boolean isCapabilityActive() {
        SecurityCapabilityLocator locator = locatorProvider.get();
        return locator != null && locator.isCapabilityActive();
    }
}
