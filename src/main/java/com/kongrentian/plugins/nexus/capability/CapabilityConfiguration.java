package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.capability.CapabilityConfigurationSupport;
import org.sonatype.nexus.formfields.FormField;

import java.util.List;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.*;

public class CapabilityConfiguration extends CapabilityConfigurationSupport {
    private final String apiUrl;
    private final String apiAuth;
    private final boolean apiTrustAllCertificates;
    private final long scanInterval;
    private final boolean failOnRequestErrors;
    private final String userAgent;
    private final long connectionTimeout;
    private final long readTimeout;
    private final long writeTimeout;

    private final Map<String, String> properties;

    public CapabilityConfiguration(Map<String, String> properties) {
        /*
         you cannot use just `get`, because capabilities do not change after
         they are created

         if you use `get` and the capability does not have such property, it will return `null`,
         an exception will be thrown, and nexus will not boot up
        */
        this.properties = properties;

        apiUrl = get(API_URL);
        apiAuth = get(API_TOKEN);
        apiTrustAllCertificates = Boolean.parseBoolean(get(API_TRUST_ALL_CERTIFICATES));
        userAgent = get(USER_AGENT);
        connectionTimeout = Long.parseLong(get(CONNECTION_TIMEOUT));
        readTimeout = Long.parseLong(get(READ_TIMEOUT));
        writeTimeout = Long.parseLong(get(WRITE_TIMEOUT));
        scanInterval = Long.parseLong(get(SCAN_INTERVAL));
        failOnRequestErrors = Boolean.parseBoolean(get(FAIL_ON_REQUEST_ERRORS));
    }

    private String get(CapabilityKey property) {
        return properties.getOrDefault(
                property.propertyKey(), property.defaultValue());
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

    public boolean getFailOnRequestErrors(){ return failOnRequestErrors; }
}

