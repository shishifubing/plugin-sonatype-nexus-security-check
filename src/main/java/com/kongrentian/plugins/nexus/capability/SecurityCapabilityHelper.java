package com.kongrentian.plugins.nexus.capability;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.kongrentian.plugins.nexus.api.MonitoringApi;
import com.kongrentian.plugins.nexus.api.RemoteScanApi;
import com.kongrentian.plugins.nexus.api.SecurityClient;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.sonatype.nexus.capability.CapabilityReference;
import org.sonatype.nexus.capability.CapabilityRegistry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;


@Named
public class SecurityCapabilityHelper {

    public final static ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // to deserialize joda datetime
            .registerModule(new JodaModule());
    public final static ObjectMapper yamlMapper = new ObjectMapper(
            new YAMLFactory().disable(
                    YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormat
                    .forPattern("yyyy-MM-dd")
                    .withZone(DateTimeZone.UTC);
    private final CapabilityRegistry capabilityRegistry;
    private CapabilityReference securityCapabilityReference;
    private RemoteScanApi remoteScanApi;
    private MonitoringApi monitoringApi;

    @Inject
    public SecurityCapabilityHelper(final CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = capabilityRegistry;
    }

    private static boolean isTypeEqual(CapabilityReference reference) {
        return SecurityCapabilityDescriptor.CAPABILITY_TYPE
                .equals(reference.context().type());
    }

    public static DateTime parseTime(String text) {
        return DateTime.parse(text, dateTimeFormatter);
    }

    public static String todayDate() {
        return DateTime.now().toString(dateTimeFormatter);
    }

    @Nonnull
    public SecurityCapabilityConfiguration getCapabilityConfiguration() {
        return getSecurityCapabilityReference()
                .capabilityAs(SecurityCapability.class)
                .getConfig();
    }

    @Nonnull
    private CapabilityReference getSecurityCapabilityReference() {
        if (securityCapabilityReference != null) {
            return securityCapabilityReference;
        }
        CapabilityReference capabilityReference =
                capabilityRegistry
                        .get(SecurityCapabilityHelper::isTypeEqual)
                        .stream()
                        .findFirst()
                        .orElse(null);
        if (capabilityReference == null) {
            securityCapabilityReference = capabilityRegistry.add(
                    SecurityCapabilityDescriptor.CAPABILITY_TYPE,
                    false,
                    "Automatically created at " + Instant.now().toString(),
                    null);
        } else {
            securityCapabilityReference = capabilityReference;
        }
        return securityCapabilityReference;
    }

    protected void unsetCapabilityReference() {
        securityCapabilityReference = null;
        remoteScanApi = null;
        monitoringApi = null;
    }

    protected void recreateSecurityClientApi() {
        remoteScanApi = SecurityClient
                .createSecurityClientApi(getCapabilityConfiguration());
    }

    protected void recreateMonitoringApi() {
        monitoringApi = SecurityClient
                .createMonitoringApi(getCapabilityConfiguration());
    }

    public boolean isCapabilityActive() {
        return getSecurityCapabilityReference()
                .context()
                .isActive();
    }

    public RemoteScanApi getSecurityClientApi() {
        if (remoteScanApi == null) {
            recreateSecurityClientApi();
        }
        return remoteScanApi;
    }

    public MonitoringApi getMonitoringApi() {
        if (monitoringApi == null) {
            recreateMonitoringApi();
        }
        return monitoringApi;
    }

}
