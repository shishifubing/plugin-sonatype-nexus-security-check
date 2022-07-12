package com.kongrentian.plugins.nexus.capability;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.sonatype.nexus.capability.CapabilitySupport;
import org.sonatype.nexus.common.template.TemplateParameters;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;

@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapability extends CapabilitySupport<SecurityCapabilityConfiguration> {

    public final static String STATUS_KEY_CONFIG = "config";
    public final static String STATUS_KEY_CAPABILITY = "capability";

    public static final String STATUS_KEY_TASK = "task";

    public final static String capabilityStatusTemplate =
            // apache velocity template
            // I cannot get access to the whole context ($context)
            // (the tool is not enabled, I think)
            String.join("\n", new String[]{
                    "<h4>update task status</h4>",
                    "<div><pre>$task</pre></div>",
                    "<h4>remote configuration</h4>",
                    "<div><pre>$config</pre></div>",
                    "<h4>capability configuration</h4>",
                    "<div><pre>$capability</pre></div>",
            });
    private final BundleHelper bundleHelper;
    private final VelocityEngine velocityEngine;
    private Instant updateTime = Instant.now();

    @Inject
    public SecurityCapability(final BundleHelper bundleHelper,
                              final VelocityEngine velocityEngine) {
        this.bundleHelper = bundleHelper;
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
                    .set(STATUS_KEY_TASK,
                            bundleHelper
                                    .getCapabilityStatus()
                                    .get(STATUS_KEY_TASK))
                    .set(STATUS_KEY_CONFIG,
                            BundleHelper.yamlMapper
                                    .writeValueAsString(
                                            bundleHelper.getBundleConfiguration())));
        } catch (Throwable exception) {
            LOG.error("Could not render the status", exception);
            return "Could not render the status: <br>"
                    + ExceptionUtils.getStackTrace(exception);
        }
    }

    @Nullable
    @Override
    protected String renderDescription() {
        return "Updated at " + updateTime.toString();
    }

    @Override
    public void onRemove() throws Exception {
        super.onRemove();
        bundleHelper.getCapabilityConfiguration();
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

    private void update() throws JsonProcessingException {
        updateTime = Instant.now();
        bundleHelper.recreateBundleConfigurationApi();
        String status = BundleHelper.yamlMapper
                .writeValueAsString(
                        bundleHelper.getCapabilityConfiguration());
        bundleHelper.getCapabilityStatus()
                .put(STATUS_KEY_CAPABILITY, status);
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
                BundleHelper.class.getName(),
                capabilityStatusTemplate);
        return writer.toString();
    }
}
