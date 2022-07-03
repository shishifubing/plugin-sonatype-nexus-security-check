package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.Client;
import com.kongrentian.plugins.nexus.capability.CapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.CapabilityLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class ConfigurationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationHelper.class);

    @Inject
    private Provider<CapabilityLocator> locatorProvider;

    @Nullable
    public Client getSecurityClient() {
        CapabilityLocator locator = locatorProvider.get();
        if (locator == null) {
            LOG.warn("Client cannot be built because CapabilityLocator is null");
            return null;
        }
        try {
            return new Client(locator.getCapabilityConfiguration());
        } catch (Exception exception) {
            LOG.error("Client could not be created", exception);
            return null;
        }

    }

    public boolean isCapabilityEnabled() {
        CapabilityLocator locator = locatorProvider.get();
        return locator != null && locator.isCapabilityActive();
    }
}
