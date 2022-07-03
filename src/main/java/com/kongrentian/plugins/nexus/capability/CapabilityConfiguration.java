package com.kongrentian.plugins.nexus.capability;

import java.util.Map;

import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

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
        apiUrl = properties.get(API_URL.propertyKey());
        apiAuth = properties.get(API_TOKEN.propertyKey());
        apiTrustAllCertificates = Boolean.parseBoolean(
                properties.get(API_TRUST_ALL_CERTIFICATES.propertyKey()));
        userAgent = properties.get(USER_AGENT.propertyKey());
        connectionTimeout = Long.parseLong(
                properties.get(CONNECTION_TIMEOUT.propertyKey()));
        readTimeout = Long.parseLong(
                properties.get(READ_TIMEOUT.propertyKey()));
        writeTimeout = Long.parseLong(
                properties.get(WRITE_TIMEOUT.propertyKey()));
        scanInterval = Long.parseLong(properties.get(SCAN_INTERVAL.propertyKey()));
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiAuth() { return apiAuth; }
    public boolean getApiTrustAllCertificates(){return apiTrustAllCertificates;}
    public long getReadTimeout() { return readTimeout; }
    public String getUserAgent() { return userAgent; }

    public long getConnectionTimeout() { return connectionTimeout; }

    public long getWriteTimeout() { return writeTimeout; }

    public long getScanInterval() { return scanInterval; }
}

