package com.kongrentian.plugins.nexus.capability;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kongrentian.plugins.nexus.model.WhiteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import java.time.LocalDate;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.*;

public class SecurityCapabilityConfiguration extends CapabilityConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityCapabilityConfiguration.class);
    private final boolean enableScanRemote;
    private final boolean enableMonitoring;
    private final boolean enableScanLocal;

    private final boolean failOnScanErrors;

    private final String monitoringUrl;
    private final String monitoringLogin;
    private final String monitoringPassword;

    private final LocalDate scanLocalLastModified;
    private Exception scanLocalLastModifiedException = null;
    private final WhiteList scanLocalWhiteList;
    private Exception scanLocalWhiteListException = null;

    private final String scanRemoteUrl;
    private final String scanRemoteToken;
    private final long scanRemoteInterval;

    private final boolean httpSSLVerify;
    private final String httpUserAgent;
    private final long httpConnectionTimeout;
    private final long httpReadTimeout;
    private final long httpWriteTimeout;

    private final Map<String, String> properties;

    public SecurityCapabilityConfiguration(Map<String, String> properties) {
        LocalDate scanLocalLastModifiedTemp;
        WhiteList scanLocalWhiteListTemp;
        this.properties = properties;

        enableMonitoring = (boolean) get(ENABLE_MONITORING);
        enableScanRemote = (boolean) get(ENABLE_SCAN_REMOTE);
        enableScanLocal = (boolean) get(ENABLE_SCAN_LOCAL);

        monitoringUrl = (String) get(MONITORING_URL);
        monitoringLogin = (String) get(MONITORING_LOGIN);
        monitoringPassword = (String) get(MONITORING_PASSWORD);

        scanRemoteUrl = (String) get(SCAN_REMOTE_URL);
        scanRemoteToken = (String) get(SCAN_REMOTE_TOKEN);
        scanRemoteInterval = (long) get(SCAN_REMOTE_INTERVAL);

        String lastModified = (String) get(SCAN_LOCAL_LAST_MODIFIED);
        try {
            scanLocalLastModifiedTemp = SecurityCapabilityField.parseTime(lastModified);
        } catch (Exception exception) {
            LOG.error("Could not parse last_modified date: {}",
                    lastModified, exception);
            scanLocalLastModifiedException = exception;
            scanLocalLastModifiedTemp = SecurityCapabilityField.parseTime(
                    SCAN_LOCAL_LAST_MODIFIED.defaultValue());
        }
        scanLocalLastModified = scanLocalLastModifiedTemp;
        String whiteList = (String) get(SCAN_LOCAL_WHITE_LIST);
        try {
            scanLocalWhiteListTemp = WhiteList.fromYAML(whiteList);
        } catch (Exception exception) {
            LOG.error("Could not parse white list: {}",
                    whiteList, exception);
            scanLocalWhiteListException = exception;
            scanLocalWhiteListTemp = new WhiteList();
        }
        scanLocalWhiteList = scanLocalWhiteListTemp;
        failOnScanErrors = (boolean) get(FAIL_ON_SCAN_ERRORS);

        httpSSLVerify = (boolean) get(HTTP_SSL_VERIFY);
        httpUserAgent = (String) get(HTTP_USER_AGENT);
        httpConnectionTimeout = (Long) get(HTTP_CONNECTION_TIMEOUT);
        httpReadTimeout = (long) get(HTTP_READ_TIMEOUT);
        httpWriteTimeout = (long) get(HTTP_WRITE_TIMEOUT);
    }

    private Object get(SecurityCapabilityKey securityCapabilityKey) {
        String defaultValue = securityCapabilityKey.defaultValue();
        String property = properties.get(securityCapabilityKey.propertyKey());
        try {
            return securityCapabilityKey.field().convert(property);
        } catch (Exception exception) {
            LOG.error("Could not convert property {}, falling back to default - {}",
                    securityCapabilityKey.name(),
                    securityCapabilityKey.defaultValue(),
                    exception);

        }
        return securityCapabilityKey.field().convert(defaultValue);
    }

    public boolean isEnableScanRemote() {
        return enableScanRemote;
    }

    public boolean isEnableMonitoring() {
        return enableMonitoring;
    }

    public boolean isEnableScanLocal() {
        return enableScanLocal;
    }

    public boolean isFailOnScanErrors() {
        return failOnScanErrors;
    }

    public String getMonitoringUrl() {
        return monitoringUrl;
    }

    public String getMonitoringLogin() {
        return monitoringLogin;
    }

    public String getMonitoringPassword() {
        return monitoringPassword;
    }

    public LocalDate getScanLocalLastModified() {
        return scanLocalLastModified;
    }

    public WhiteList getScanLocalWhiteList() {
        return scanLocalWhiteList;
    }

    public String getScanRemoteUrl() {
        return scanRemoteUrl;
    }

    public String getScanRemoteToken() {
        return scanRemoteToken;
    }

    public long getScanRemoteInterval() {
        return scanRemoteInterval;
    }

    public boolean isHttpSSLVerify() {
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

    public Exception getScanLocalLastModifiedException() {
        return scanLocalLastModifiedException;
    }

    public Exception getScanLocalWhiteListException() {
        return scanLocalWhiteListException;
    }
}

