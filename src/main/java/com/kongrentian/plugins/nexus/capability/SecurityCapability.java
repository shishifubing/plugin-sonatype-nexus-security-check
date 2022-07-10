package com.kongrentian.plugins.nexus.capability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilitySupport;
import org.sonatype.nexus.common.template.TemplateParameters;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.util.Map;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {


    private static final Logger LOG = LoggerFactory.getLogger(SecurityCapability.class);
    private final SecurityCapabilityHelper securityCapabilityHelper;
    private Instant updateTime = Instant.now();

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
        try {
            return securityCapabilityHelper.render(
                    new TemplateParameters()
                            .set("status", getConfig().getStatus())
                            .set("config", getConfig().getProperties()));
        } catch (Throwable exception) {
            LOG.error("Could not render the status", exception);
            return "Could not render the status: " + exception;
        }
    }

    @Nullable
    @Override
    protected String renderDescription() {
        return "Updated at " + updateTime.toString();
    }

    @Override
    public void onRemove() throws Exception {
        securityCapabilityHelper.unsetCapabilityReference();
        super.onRemove();
    }

    @Override
    public void onUpdate() throws Exception {
        super.onUpdate();
        updateTime = Instant.now();
        securityCapabilityHelper.recreateSecurityClient();
    }
}
