package com.shishifubing.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityDescriptorSupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
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
    public static final String CAPABILITY_ID = "kongrentian.security";
    public static final CapabilityType CAPABILITY_TYPE = CapabilityType.capabilityType(CAPABILITY_ID);
    public static final String CAPABILITY_NAME = "Security and monitoring of assets";
    public static final String CAPABILITY_DESCRIPTION = "Security and monitoring of assets";
    private final List<FormField> fields;

    public SecurityCapabilityDescriptor() throws RuntimeException {
        // cannot define static methods in interface
        // cannot inherit enums
        fields = SecurityCapabilityFormField.createFields();
    }

    @Override
    public CapabilityType type() {
        return CAPABILITY_TYPE;
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
