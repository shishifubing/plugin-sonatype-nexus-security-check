package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilitySupport;
import org.sonatype.nexus.common.template.TemplateParameters;

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
        return render(SecurityCapabilityHelper.capabilityStatusTemplate,
                new TemplateParameters(getConfig().getStatus()));
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
