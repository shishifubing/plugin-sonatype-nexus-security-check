package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.*;

public class CapabilityConfiguration extends CapabilityConfigurationSupport {
    private final String apiUrl;
    private final String apiAuth;
    private final boolean apiTrustAllCertificates;
    private final long scanInterval;
    private final String userAgent;
    private final long connectionTimeout;
    private final long readTimeout;
    private final long writeTimeout;


    public CapabilityConfiguration(Map<String, String> properties) {
        /*
         you cannot use just `get`, because capabilities do not change after
         they are created

         if you use `get` and the capability does not have such property, it will return `null`,
         an exception will be thrown, and nexus will not boot up
        */

        apiUrl = properties.getOrDefault(
                API_URL.propertyKey(),
                API_URL.defaultValue());
        apiAuth = properties.getOrDefault(
                API_TOKEN.propertyKey(),
                API_TOKEN.defaultValue());
        apiTrustAllCertificates = Boolean.parseBoolean(properties.getOrDefault(
                API_TRUST_ALL_CERTIFICATES.propertyKey(),
                API_TRUST_ALL_CERTIFICATES.defaultValue()));
        userAgent = properties.getOrDefault(
                USER_AGENT.propertyKey(),
                USER_AGENT.defaultValue());
        connectionTimeout = Long.parseLong(properties.getOrDefault(
                CONNECTION_TIMEOUT.propertyKey(),
                CONNECTION_TIMEOUT.defaultValue()));
        readTimeout = Long.parseLong(properties.getOrDefault(
                READ_TIMEOUT.propertyKey(),
                READ_TIMEOUT.defaultValue()));
        writeTimeout = Long.parseLong(properties.getOrDefault(
                WRITE_TIMEOUT.propertyKey(),
                WRITE_TIMEOUT.defaultValue()));
        scanInterval = Long.parseLong(properties.getOrDefault(
                SCAN_INTERVAL.propertyKey(),
                SCAN_INTERVAL.defaultValue()));
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiAuth() {
        return apiAuth;
    }

    public boolean getApiTrustAllCertificates() {
        return apiTrustAllCertificates;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public long getScanInterval() {
        return scanInterval;
    }
}

