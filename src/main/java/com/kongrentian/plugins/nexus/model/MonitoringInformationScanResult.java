package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kongrentian.plugins.nexus.scanner.AbstractScanner;

import java.io.Serializable;

public class MonitoringInformationScanResult implements Serializable {

    @JsonProperty
    private final Class<? extends AbstractScanner> type;

    @JsonProperty
    private final ScanResult result;

    public MonitoringInformationScanResult(Class<? extends AbstractScanner> type,
                                           ScanResult result) {
        this.type = type;
        this.result = result;
    }

    public ScanResult getResult() {
        return result;
    }
}