package com.kongrentian.plugins.nexus.model.scan_result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.sonatype.nexus.common.collect.NestedAttributesMap;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ScanResult implements Serializable {
    public static final long NO_LAST_SCAN = -1;
    @JsonProperty
    private final boolean allowed;
    @JsonProperty
    private final ScanResultType type;
    @JsonProperty
    private ScanResultException exception;
    @JsonIgnore
    private Instant scanDate;

    public ScanResult(boolean allowed, ScanResultType type) {
        this.allowed = allowed;
        this.type = type;
    }

    @Nullable
    public static ScanResult fromAttributes(NestedAttributesMap attributes) {
        Boolean allowed = (Boolean) attributes.get("security_allowed");
        ScanResultType type = (ScanResultType) attributes.get("security_result_type");
        Instant scanDate = (Instant) attributes.get("security_scan_date");
        if (allowed == null || type == null || scanDate == null) {
            return null;
        }
        return new ScanResult(allowed, type).setScanDate(scanDate);
    }

    public ScanResult setException(Throwable exception) {
        this.exception = new ScanResultException(exception);
        return this;
    }


    private ScanResult setScanDate(Instant scanDate) {
        this.scanDate = scanDate;
        return this;
    }

    @JsonIgnore
    public long getInterval() {
        if (scanDate == null) {
            return NO_LAST_SCAN;
        }
        return ChronoUnit.MINUTES.between(scanDate, Instant.now());
    }

    public void updateAssetAttributes(NestedAttributesMap attributes) {
        attributes.clear();
        attributes.set("security_allowed", allowed);
        attributes.set("security_scan_date", Instant.now());
        attributes.set("security_result_type", type);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public ScanResultType getType() {
        return type;
    }

}

