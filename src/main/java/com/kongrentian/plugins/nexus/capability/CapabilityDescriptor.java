package com.kongrentian.plugins.nexus.capability;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sonatype.nexus.capability.CapabilityDescriptorSupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.StringTextFormField;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.API_TOKEN;
import static com.kongrentian.plugins.nexus.capability.CapabilityKey.API_URL;

@Singleton
@Named(CapabilityDescriptor.CAPABILITY_ID)
public class CapabilityDescriptor
        extends CapabilityDescriptorSupport<CapabilityConfiguration>
        implements Taggable {
    static final String CAPABILITY_ID = "scan.security";
    private static final String CAPABILITY_NAME = "Scan Security Configuration";
    private static final String CAPABILITY_DESCRIPTION = "Provides support to test artifacts";

    private final StringTextFormField fieldApiUrl;
    private final StringTextFormField fieldApiToken;

    public CapabilityDescriptor() {
        fieldApiUrl = new StringTextFormField(API_URL.propertyKey(), "API URL", "", FormField.MANDATORY)
                .withInitialValue(API_URL.defaultValue());
        fieldApiToken = new StringTextFormField(API_TOKEN.propertyKey(), "API Token", "", FormField.OPTIONAL)
                .withInitialValue(API_TOKEN.defaultValue());
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
        return Arrays.asList(fieldApiUrl, fieldApiToken);
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
