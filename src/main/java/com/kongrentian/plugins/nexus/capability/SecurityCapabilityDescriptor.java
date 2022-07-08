package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityDescriptorSupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
import org.sonatype.nexus.formfields.*;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.*;

@Singleton
@Named(SecurityCapabilityDescriptor.CAPABILITY_ID)
public class SecurityCapabilityDescriptor
        extends CapabilityDescriptorSupport<SecurityCapabilityConfiguration>
        implements Taggable {
    static final String CAPABILITY_ID = "security.capability.id";
    private static final String CAPABILITY_NAME = "Security and monitoring of assets";
    private static final String CAPABILITY_DESCRIPTION = "Security and monitoring of assets";

    private final List<FormField> fields;

    public SecurityCapabilityDescriptor() {
        fields = Arrays.asList(
                new CheckboxFormField(
                        ENABLE_SCAN_REMOTE.propertyKey(),
                        ENABLE_SCAN_REMOTE.propertyKey(),
                        "Enables remote scans",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                ENABLE_SCAN_REMOTE.defaultValue())),

                new CheckboxFormField(
                        ENABLE_SCAN_LOCAL.propertyKey(),
                        ENABLE_SCAN_LOCAL.propertyKey(),
                        "Enables local scans",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                ENABLE_SCAN_LOCAL.defaultValue())),

                new CheckboxFormField(
                        ENABLE_MONITORING.propertyKey(),
                        ENABLE_MONITORING.propertyKey(),
                        "Enables monitoring",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                ENABLE_MONITORING.defaultValue())),

                new UrlFormField(
                        SCAN_REMOTE_URL.propertyKey(),
                        SCAN_REMOTE_URL.propertyKey(),
                        "Base url for remote scans",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                SCAN_REMOTE_URL.defaultValue()),

                new PasswordFormField(
                        SCAN_REMOTE_TOKEN.propertyKey(),
                        SCAN_REMOTE_TOKEN.propertyKey(),
                        "Token for remote scans",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                SCAN_REMOTE_TOKEN.defaultValue()),

                new NumberTextFormField(
                        SCAN_REMOTE_INTERVAL_MINUTES.propertyKey(),
                        SCAN_REMOTE_INTERVAL_MINUTES.propertyKey(),
                        "Interval for remote scans, minutes",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                SCAN_REMOTE_INTERVAL_MINUTES.defaultValue())),

                new StringTextFormField(
                        SCAN_LOCAL_LAST_MODIFIED.propertyKey(),
                        SCAN_LOCAL_LAST_MODIFIED.propertyKey(),
                        "All last_modified dates after this one are blocked",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                SCAN_LOCAL_LAST_MODIFIED.defaultValue()),

                new TextAreaFormField(
                        SCAN_LOCAL_WHITE_LIST.propertyKey(),
                        SCAN_LOCAL_WHITE_LIST.propertyKey(),
                        "White list for packages and users",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                SCAN_LOCAL_WHITE_LIST.defaultValue()),

                new UrlFormField(
                        MONITORING_URL.propertyKey(),
                        MONITORING_URL.propertyKey(),
                        "Base url for monitoring",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                MONITORING_URL.defaultValue()),

                new StringTextFormField(
                        MONITORING_LOGIN.propertyKey(),
                        MONITORING_LOGIN.propertyKey(),
                        "Login for monitoring",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                MONITORING_LOGIN.defaultValue()),

                new PasswordFormField(
                        MONITORING_PASSWORD.propertyKey(),
                        MONITORING_PASSWORD.propertyKey(),
                        "Password for monitoring",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                MONITORING_PASSWORD.defaultValue()),


                new CheckboxFormField(
                        HTTP_FAIL_ON_REQUEST_ERRORS.propertyKey(),
                        HTTP_FAIL_ON_REQUEST_ERRORS.propertyKey(),
                        "Whether to throw an error if a scan request fails",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                HTTP_FAIL_ON_REQUEST_ERRORS.defaultValue())),

                new CheckboxFormField(
                        HTTP_SSL_VERIFY.propertyKey(),
                        HTTP_SSL_VERIFY.propertyKey(),
                        "Whether to verify ssl certificates",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                HTTP_SSL_VERIFY.defaultValue())),

                new StringTextFormField(
                        HTTP_USER_AGENT.propertyKey(),
                        HTTP_USER_AGENT.propertyKey(),
                        "User agent for all requests",
                        FormField.OPTIONAL)
                        .withInitialValue(
                                HTTP_USER_AGENT.defaultValue()),

                new NumberTextFormField(
                        HTTP_CONNECTION_TIMEOUT.propertyKey(),
                        HTTP_CONNECTION_TIMEOUT.propertyKey(),
                        "Connection timeout for all requests, milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                HTTP_CONNECTION_TIMEOUT.defaultValue())),

                new NumberTextFormField(
                        HTTP_READ_TIMEOUT.propertyKey(),
                        HTTP_READ_TIMEOUT.propertyKey(),
                        "Read timeout for all requests, milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                HTTP_READ_TIMEOUT.defaultValue())),

                new NumberTextFormField(
                        HTTP_WRITE_TIMEOUT.propertyKey(),
                        HTTP_WRITE_TIMEOUT.propertyKey(),
                        "Write timeout for all requests, milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                HTTP_WRITE_TIMEOUT.defaultValue())));
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

}
