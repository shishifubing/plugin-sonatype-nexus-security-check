package com.kongrentian.plugins.nexus.capability;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilitySupport;
import org.sonatype.nexus.common.template.TemplateParameters;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.SCAN_LOCAL_WHITE_LIST;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

    public final static String capabilityStatusTemplate =
            // apache velocity template
            // I cannot get access to the whole context ($context)
            // (the tool is not enabled, I think)
            String.join("\n", new String[]{
                    "#foreach( $entry in $status.entrySet() )",
                    "<h4>$entry.getKey()</h4>",
                    "<div><pre>$entry.getValue()</pre></div>",
                    "#end",
                    "<h4>white list</h4>",
                    "<div><pre>$white_list</pre></div>"
            });
    private static final Logger LOG = LoggerFactory.getLogger(SecurityCapability.class);
    private final SecurityCapabilityHelper securityCapabilityHelper;
    private final VelocityEngine velocityEngine;
    private Instant updateTime = Instant.now();

    @Inject
    public SecurityCapability(final SecurityCapabilityHelper securityCapabilityHelper,
                              final VelocityEngine velocityEngine) {
        this.securityCapabilityHelper = securityCapabilityHelper;
        this.velocityEngine = velocityEngine;
    }

    @Override
    protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
        return new SecurityCapabilityConfiguration(properties);
    }

    @Nullable
    @Override
    protected String renderStatus() {
        try {
            return render(new TemplateParameters()
                    .set("status",
                            getConfig().getStatus())
                    .set("white_list",
                            getConfig().get(SCAN_LOCAL_WHITE_LIST)));
        } catch (Throwable exception) {
            LOG.error("Could not render the status", exception);
            return "Could not render the status: <br>" + exception;
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
        update();
    }

    @Override
    public void onCreate() throws Exception {
        super.onCreate();
        update();
    }

    private void update() {
        updateTime = Instant.now();
        securityCapabilityHelper.recreateSecurityClientApi();
        securityCapabilityHelper.recreateMonitoringApi();
    }

    /**
     * you are supposed to use 'render' from {@link CapabilitySupport},
     * but it only works with urls - you cannot just render a random template
     */
    public String render(final TemplateParameters parameters) {
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(
                new VelocityContext(parameters.get()),
                writer,
                SecurityCapabilityHelper.class.getName(),
                capabilityStatusTemplate);
        return writer.toString();
    }
}
