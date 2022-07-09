package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import org.sonatype.nexus.common.collect.NestedAttributesMap;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.lang.String.format;

public class ScanResult implements Serializable {
    public static final long NO_LAST_SCAN = -1;
    @JsonProperty
    private boolean allowed;
    @JsonProperty
    private String reason;
    @JsonProperty
    private String exception = null;
    private Instant scanDate;

    public ScanResult() {
    }

    public ScanResult(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public ScanResult(boolean allowed, String reason, Throwable exception) {
        this(allowed, reason);
        this.exception = exception.getMessage();
    }

    public ScanResult(boolean allowed, String reason, Instant scanDate) {
        this(allowed, reason);
        this.scanDate = scanDate;
    }

    public static ScanResult tooRecent(
            boolean allowed,
            long remoteScanTimeDifference,
            long maximumRemoteScanTimeDifference,
            ScanResult lastScan) {
        return new ScanResult(allowed,
                format("The asset was checked recently, using the last scan "
                                + "scan difference - %s minutes, "
                                + "maximum - %s",
                        remoteScanTimeDifference,
                        maximumRemoteScanTimeDifference));
    }

    @Nullable
    public static ScanResult fromAttributes(NestedAttributesMap attributes) {
        Boolean allowed = (Boolean) attributes.get("security_allowed");
        String reason = (String) attributes.get("security_reason");
        Instant scanDate = (Instant) attributes.get("security_scan_date");
        if (allowed == null || reason == null || scanDate == null) {
            return null;
        }
        return new ScanResult(allowed, reason, scanDate);
    }

    public long getInterval() {
        if (scanDate == null) {
            return NO_LAST_SCAN;
        }
        return ChronoUnit.MINUTES.between(scanDate, Instant.now());
    }

    public void updateAssetAttributes(NestedAttributesMap attributes) {
        attributes.clear();
        attributes.set("security_allowed", allowed);
        attributes.set("security_scan_date",
                scanDate == null ? Instant.now() : scanDate);
        attributes.set("security_reason", reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getReason() {
        return reason;
    }

    public boolean isFailure() {
        return exception != null;
    }
}

