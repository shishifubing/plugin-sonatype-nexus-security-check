package com.kongrentian.plugins.nexus.capability;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.*;
import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;
import static java.lang.String.format;

public class SecurityCapabilityConfiguration
        extends CapabilityConfigurationSupport
        implements Serializable {


    @JsonProperty("http_ssl_verify")
    private final boolean httpSSLVerify;

    @JsonProperty("http_user_agent")
    private final String httpUserAgent;
    @JsonProperty("http_connection_timeout")
    private final long httpConnectionTimeout;
    @JsonProperty("http_read_timeout")
    private final long httpReadTimeout;
    @JsonProperty("http_write_timeout")
    private final long httpWriteTimeout;

    @JsonIgnore
    private final Map<String, String> properties;
    @JsonProperty("config_url_base")
    private final String configUrlBase;
    @JsonProperty("config_auth")
    private final String configAuth;
    @JsonProperty("config_url_request")
    private final String configUrlRequest;
    @JsonProperty("config_url_parameters")
    private final Map<String, String> configUrlParameters = new HashMap<>();
    @JsonProperty("config_override")
    private final String configOverride;


    public SecurityCapabilityConfiguration(Map<String, String> properties) {
        this.properties = properties;
        httpSSLVerify = (boolean) get(HTTP_SSL_VERIFY);
        httpUserAgent = (String) get(HTTP_USER_AGENT);
        httpConnectionTimeout = (Long) get(HTTP_CONNECTION_TIMEOUT);
        httpReadTimeout = (long) get(HTTP_READ_TIMEOUT);
        httpWriteTimeout = (long) get(HTTP_WRITE_TIMEOUT);
        configUrlBase = (String) get(CONFIG_URL_BASE);
        configAuth = (String) get(CONFIG_AUTH);
        configUrlRequest = (String) get(CONFIG_URL_REQUEST);
        String[] parameters = ((String) get(CONFIG_URL_PARAMETERS))
                .split("\\w+");
        for (int index = 0; index < parameters.length; index += 2) {
            configUrlParameters.put(parameters[index], parameters[index + 1]);
        }
        configOverride = (String) get(CONFIG_OVERRIDE);
    }

    @Nonnull
    public Object get(SecurityCapabilityKey securityCapabilityKey) {
        String defaultValue = securityCapabilityKey.defaultValue();
        String propertyKey = securityCapabilityKey.propertyKey();
        String property = properties.get(propertyKey);
        try {
            if (property != null && !property.isEmpty()) {
                return securityCapabilityKey.getField().convert(property);
            }
        } catch (Throwable exception) {
            String message = format(
                    "Could not convert property '%s', falling back to default - '%s'",
                    propertyKey, securityCapabilityKey.defaultValue());
            LOG.error(message, exception);
        }
        return securityCapabilityKey.getField().convert(defaultValue);
    }


    public boolean getHttpSSLVerify() {
        return httpSSLVerify;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public long getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }

    public long getHttpReadTimeout() {
        return httpReadTimeout;
    }

    public long getHttpWriteTimeout() {
        return httpWriteTimeout;
    }

    public String getConfigUrlBase() {
        return configUrlBase;
    }

    public String getConfigAuth() {
        return configAuth;
    }

    public String getConfigUrlRequest() {
        return configUrlRequest;
    }

    public Map<String, String> getConfigUrlParameters() {
        return configUrlParameters;
    }
}

