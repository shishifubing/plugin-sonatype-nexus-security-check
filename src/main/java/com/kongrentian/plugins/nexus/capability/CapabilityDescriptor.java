package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityDescriptorSupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
import org.sonatype.nexus.formfields.*;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.*;

@Singleton
@Named(CapabilityDescriptor.CAPABILITY_ID)
public class CapabilityDescriptor
        extends CapabilityDescriptorSupport<CapabilityConfiguration>
        implements Taggable {
    static final String CAPABILITY_ID = "scan.security";
    private static final String CAPABILITY_NAME = "Scan Security Configuration";
    private static final String CAPABILITY_DESCRIPTION = "Provides support to test artifacts";

    private final List<FormField> fields;

    public CapabilityDescriptor() {
        fields = Arrays.asList(
                new UrlFormField(
                        API_URL.propertyKey(),
                        "API URL",
                        "",
                        FormField.OPTIONAL)
                        .withInitialValue(API_URL.defaultValue()),
                new PasswordFormField(
                        API_TOKEN.propertyKey(),
                        "API token",
                        "",
                        FormField.OPTIONAL)
                        .withInitialValue(API_TOKEN.defaultValue()),
                new NumberTextFormField(
                        SCAN_INTERVAL.propertyKey(),
                        "Scan interval",
                        "minutes",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                SCAN_INTERVAL.defaultValue())),
                new CheckboxFormField(
                        FAIL_ON_REQUEST_ERRORS.propertyKey(),
                        "Whether to fail on scan requests errors",
                        "If true, and a request fails, an artifact "
                                + "will not be allowed to be downloaded",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                FAIL_ON_REQUEST_ERRORS.defaultValue())),
                new CheckboxFormField(
                        API_TRUST_ALL_CERTIFICATES.propertyKey(),
                        "Trust all ssl certificates",
                        "",
                        FormField.OPTIONAL)
                        .withInitialValue(Boolean.parseBoolean(
                                API_TRUST_ALL_CERTIFICATES.defaultValue())),
                new StringTextFormField(
                        USER_AGENT.propertyKey(),
                        "User agent",
                        "",
                        FormField.OPTIONAL)
                        .withInitialValue(USER_AGENT.defaultValue()),
                new NumberTextFormField(
                        CONNECTION_TIMEOUT.propertyKey(),
                        "Connection timeout",
                        "milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                CONNECTION_TIMEOUT.defaultValue())),
                new NumberTextFormField(
                        READ_TIMEOUT.propertyKey(),
                        "Read timeout",
                        "milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                READ_TIMEOUT.defaultValue())),
                new NumberTextFormField(
                        WRITE_TIMEOUT.propertyKey(),
                        "Write timeout",
                        "milliseconds",
                        FormField.OPTIONAL)
                        .withInitialValue(Long.parseLong(
                                WRITE_TIMEOUT.defaultValue())));
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
    protected CapabilityConfiguration createConfig(Map<String, String> properties) {
        return new CapabilityConfiguration(properties);
    }

    @Override
    public Set<Tag> getTags() {
        return Collections.singleton(Tag.categoryTag("Security"));
    }

}
