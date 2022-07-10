package com.kongrentian.plugins.nexus.capability;

import com.kongrentian.plugins.nexus.model.WhiteList;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import java.util.HashMap;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.*;
import static java.lang.String.format;

public class SecurityCapabilityConfiguration extends CapabilityConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityCapabilityConfiguration.class);
    private final boolean enableScanRemote;
    private final boolean enableMonitoring;
    private final boolean enableScanLocal;


    private final String monitoringUrl;
    private final String monitoringAuth;

    private final DateTime scanLocalLastModified;
    private final WhiteList scanLocalWhiteList;

    private final String scanRemoteUrl;
    private final String scanRemoteAuth;
    private final long scanRemoteInterval;

    private final boolean httpSSLVerify;
    private final String httpUserAgent;
    private final long httpConnectionTimeout;
    private final long httpReadTimeout;
    private final long httpWriteTimeout;

    private final Map<String, String> properties;
    private final Map<String, Object> status = new HashMap<>();
    private final String monitoringPipeline;
    private final String monitoringIndex;


    public SecurityCapabilityConfiguration(Map<String, String> properties) {
        DateTime scanLocalLastModifiedTemp;
        WhiteList scanLocalWhiteListTemp;
        status.put(SCAN_LOCAL_LAST_MODIFIED.propertyKey(), "valid");
        status.put(SCAN_LOCAL_WHITE_LIST.propertyKey(), "valid");
        this.properties = properties;


        enableMonitoring = (boolean) get(ENABLE_MONITORING);
        enableScanRemote = (boolean) get(ENABLE_SCAN_REMOTE);
        enableScanLocal = (boolean) get(ENABLE_SCAN_LOCAL);

        monitoringUrl = (String) get(MONITORING_URL);
        monitoringAuth = (String) get(MONITORING_AUTH);
        monitoringPipeline = (String) get(MONITORING_PIPELINE);
        monitoringIndex = (String) get(MONITORING_INDEX);

        scanRemoteUrl = (String) get(SCAN_REMOTE_URL);
        scanRemoteAuth = (String) get(SCAN_REMOTE_AUTH);
        scanRemoteInterval = (long) get(SCAN_REMOTE_INTERVAL);

        String lastModified = (String) get(SCAN_LOCAL_LAST_MODIFIED);

        try {
            scanLocalLastModifiedTemp = SecurityCapabilityHelper.parseTime(lastModified);
        } catch (Throwable exception) {
            LOG.error("Could not parse last_modified date: {}",
                    lastModified, exception);
            status.put(SCAN_LOCAL_LAST_MODIFIED.propertyKey(),
                    ExceptionUtils.getFullStackTrace(exception));
            scanLocalLastModifiedTemp = SecurityCapabilityHelper.parseTime(
                    SCAN_LOCAL_LAST_MODIFIED.defaultValue());
        }
        scanLocalLastModified = scanLocalLastModifiedTemp;
        String whiteList = (String) get(SCAN_LOCAL_WHITE_LIST);
        try {
            scanLocalWhiteListTemp = WhiteList.fromYAML(whiteList);
        } catch (Throwable exception) {
            LOG.error("Could not parse white list: {}",
                    whiteList, exception);
            status.put(SCAN_LOCAL_WHITE_LIST.propertyKey(),
                    ExceptionUtils.getFullStackTrace(exception));
            scanLocalWhiteListTemp = new WhiteList();
        }
        scanLocalWhiteList = scanLocalWhiteListTemp;

        httpSSLVerify = (boolean) get(HTTP_SSL_VERIFY);
        httpUserAgent = (String) get(HTTP_USER_AGENT);
        httpConnectionTimeout = (Long) get(HTTP_CONNECTION_TIMEOUT);
        httpReadTimeout = (long) get(HTTP_READ_TIMEOUT);
        httpWriteTimeout = (long) get(HTTP_WRITE_TIMEOUT);
    }

    public Object get(SecurityCapabilityKey securityCapabilityKey) {
        String defaultValue = securityCapabilityKey.defaultValue();
        String propertyKey = securityCapabilityKey.propertyKey();
        String property = properties.get(propertyKey);
        try {
            if (property != null && !property.isEmpty()) {
                return securityCapabilityKey.field().convert(property);
            }
        } catch (Throwable exception) {
            String message = format("Could not convert property '%s', falling back to default - '%s'",
                    propertyKey, securityCapabilityKey.defaultValue());
            LOG.error(message, exception);
            status.put(propertyKey, exception.getMessage());
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


    public String getMonitoringUrl() {
        return monitoringUrl;
    }

    public String getMonitoringAuth() {
        return monitoringAuth;
    }

    public DateTime getScanLocalLastModified() {
        return scanLocalLastModified;
    }

    public WhiteList getScanLocalWhiteList() {
        return scanLocalWhiteList;
    }

    public String getScanRemoteUrl() {
        return scanRemoteUrl;
    }

    public String getScanRemoteAuth() {
        return scanRemoteAuth;
    }

    public long getScanRemoteInterval() {
        return scanRemoteInterval;
    }

    public boolean httpSSLVerify() {
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


    public Map<String, Object> getStatus() {
        return status;
    }

    public String getMonitoringPipeline() {
        return monitoringPipeline;
    }

    public String getMonitoringIndex() {
        return monitoringIndex;
    }
}

