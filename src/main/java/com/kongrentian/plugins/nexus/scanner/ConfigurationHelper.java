package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.kongrentian.plugins.nexus.api.Client;
import com.kongrentian.plugins.nexus.api.ClientAPI;
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
    public ClientAPI getSecurityClientAPI() {
        CapabilityLocator locator = locatorProvider.get();
        if (locator == null) {
            LOG.warn("Client cannot be built because CapabilityLocator is null!");
            return null;
        }
        try {
            Client.Config config = new Client.Config(locator.getSecurityCapabilityConfiguration()
                    .getApiToken());
            return new Client(config).buildSync();
        } catch (Exception exception) {
            LOG.error("Client could not be created", exception);
            return null;
        }

    }

    @Nullable
    public CapabilityConfiguration getConfiguration() {
        CapabilityLocator locator = locatorProvider.get();
        if (locator == null) {
            return null;
        }
        return locator.getSecurityCapabilityConfiguration();

    }

    public boolean isCapabilityEnabled() {
        CapabilityLocator locator = locatorProvider.get();
        if (locator == null) {
            return false;
        }
        return locator.isSecurityCapabilityActive();
    }
}
