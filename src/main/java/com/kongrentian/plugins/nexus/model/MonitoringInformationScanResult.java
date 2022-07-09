package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import java.io.Serializable;

public class MonitoringInformationScanResult implements Serializable {

    @JsonProperty("fail_key")
    private final SecurityCapabilityKey failKey;

    @JsonProperty
    private final ScanResult result;

    public MonitoringInformationScanResult(SecurityCapabilityKey failKey, ScanResult result) {
        this.failKey = failKey;
        this.result = result;
    }

    public ScanResult getResult() {
        return result;
    }
}