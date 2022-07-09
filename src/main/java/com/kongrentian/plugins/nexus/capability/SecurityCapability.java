package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilitySupport;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

    private final SecurityCapabilityHelper securityCapabilityHelper;

    @Inject
    public SecurityCapability(SecurityCapabilityHelper securityCapabilityHelper) {
        this.securityCapabilityHelper = securityCapabilityHelper;
    }

    @Override
    protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
        return new SecurityCapabilityConfiguration(properties);
    }

    @Nullable
    @Override
    protected String renderStatus() {
        SecurityCapabilityConfiguration config = getConfig();
        return SecurityCapabilityKey.SCAN_LOCAL_LAST_MODIFIED.propertyKey() +
                SecurityCapabilityHelper.errorMessage(
                        config.getScanLocalLastModifiedException()) +
                ", " +
                SecurityCapabilityKey.SCAN_LOCAL_WHITE_LIST.propertyKey() +
                SecurityCapabilityHelper.errorMessage(
                        config.getScanLocalWhiteListException());
    }

    @Override
    public void onRemove() throws Exception {
        securityCapabilityHelper.unsetCapabilityReference();
        super.onRemove();
    }

    @Override
    public void onUpdate() throws Exception {
        super.onUpdate();
        securityCapabilityHelper.recreateSecurityClient();
    }
}
