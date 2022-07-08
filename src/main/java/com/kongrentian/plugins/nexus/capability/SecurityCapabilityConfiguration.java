package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.*;

public class SecurityCapabilityConfiguration extends CapabilityConfigurationSupport {
    private final String apiUrl;
    private final String apiAuth;
    private final boolean httpSSLVerify;
    private final long scanInterval;
    private final boolean failOnRequestErrors;
    private final String userAgent;
    private final long connectionTimeout;
    private final long readTimeout;
    private final long writeTimeout;

    private final Map<String, String> properties;

    public SecurityCapabilityConfiguration(Map<String, String> properties) {
        /*
         you cannot use just `get`, because properties can be null sometimes

         if some of them are null, an exception will be thrown and nexus will not boot up
        */
        this.properties = properties;

        apiUrl = get(SCAN_REMOTE_URL);
        apiAuth = get(SCAN_REMOTE_TOKEN);
        httpSSLVerify = Boolean.parseBoolean(get(HTTP_SSL_VERIFY));
        userAgent = get(HTTP_USER_AGENT);
        connectionTimeout = Long.parseLong(get(HTTP_CONNECTION_TIMEOUT));
        readTimeout = Long.parseLong(get(HTTP_READ_TIMEOUT));
        writeTimeout = Long.parseLong(get(HTTP_WRITE_TIMEOUT));
        scanInterval = Long.parseLong(get(SCAN_REMOTE_INTERVAL_MINUTES));
        failOnRequestErrors = Boolean.parseBoolean(get(HTTP_FAIL_ON_REQUEST_ERRORS));
    }

    private String get(SecurityCapabilityKey capabilityKey) {
        return properties.getOrDefault(
                capabilityKey.propertyKey(),
                capabilityKey.defaultValue());
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiAuth() {
        return apiAuth;
    }

    public boolean getHttpSSLVerify() {
        return httpSSLVerify;
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

    public boolean getFailOnRequestErrors(){ return failOnRequestErrors; }
}

