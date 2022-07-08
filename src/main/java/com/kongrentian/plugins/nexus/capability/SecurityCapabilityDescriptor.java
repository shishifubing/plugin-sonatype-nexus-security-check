package com.kongrentian.plugins.nexus.capability;

import org.sonatype.goodies.i18n.I18N;
import org.sonatype.goodies.i18n.MessageBundle;
import org.sonatype.nexus.capability.*;
import org.sonatype.nexus.formfields.FormField;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapabilityDescriptor
        extends CapabilityDescriptorSupport<SecurityCapabilityConfiguration>
        implements Taggable {
    static final String CAPABILITY_ID = "kongrentian.security";
    static final CapabilityIdentity CAPABILITY_IDENTITY =
            CapabilityIdentity.capabilityIdentity(CAPABILITY_ID);
    private static final String CAPABILITY_NAME = "Security and monitoring of assets";
    private static final String CAPABILITY_DESCRIPTION = "Security and monitoring of assets";
    private static final Messages messages = I18N.create(Messages.class);
    private final List<FormField> fields;

    public SecurityCapabilityDescriptor() throws RuntimeException {
        fields = SecurityCapabilityField.createFields();
    }

    @Override
    public CapabilityType type() {
        return CapabilityType.capabilityType(CAPABILITY_ID);
    }

    @Override
    public String name() {
        return CAPABILITY_NAME;
    }

    @Override
    public String about() {
        return CAPABILITY_DESCRIPTION;
    }

    @Override
    public List<FormField> formFields() {
        return fields;
    }

    @Override
    protected SecurityCapabilityConfiguration createConfig(Map<String, String> properties) {
        return new SecurityCapabilityConfiguration(properties);
    }

    @Override
    public Set<Tag> getTags() {
        return Collections.singleton(Tag.categoryTag("Security"));
    }

    private interface Messages
            extends MessageBundle {
        @DefaultMessage("UI: Settings")
        String name();

        @DefaultMessage("Debug allowed")
        String debugAllowedLabel();

        @DefaultMessage("Allow developer debugging")
        String debugAllowedHelp();

        @DefaultMessage("Authenticated user status interval")
        String statusIntervalAuthenticatedLabel();

        @DefaultMessage("Interval between status requests for authenticated users (seconds)")
        String statusIntervalAuthenticatedHelp();

        @DefaultMessage("Anonymous user status interval")
        String statusIntervalAnonymousLabel();

        @DefaultMessage("Interval between status requests for anonymous user (seconds)")
        String statusIntervalAnonymousHelp();

        @DefaultMessage("Session timeout")
        String sessionTimeoutLabel();

        @DefaultMessage(
                "Period of inactivity before session times out (minutes). A value of 0 will mean that a session never expires."
        )
        String sessionTimeoutHelp();

        @DefaultMessage("Standard request timeout")
        String requestTimeoutLabel();

        @DefaultMessage(
                "Period of time to keep the connection alive for requests expected to take a normal period of time (seconds)")
        String requestTimeoutHelp();

        @DefaultMessage("Extended request timeout")
        String longRequestTimeoutLabel();

        @DefaultMessage(
                "Period of time to keep the connection alive for requests expected to take an extended period of time (seconds)")
        String longRequestTimeoutHelp();

        @DefaultMessage("Search request timeout")
        String searchRequestTimeoutLabel();

        @DefaultMessage("Period of time to keep the connection alive for search requests (seconds); " +
                "this value should be less than the request time in order for the correct error messages to be displayed. " +
                "If this value is not set, the standard request timeout will be used instead.")
        String searchRequestTimeoutHelp();

        @DefaultMessage("Title")
        String titleLabel();

        @DefaultMessage("Browser page title")
        String titleHelp();
    }

}
