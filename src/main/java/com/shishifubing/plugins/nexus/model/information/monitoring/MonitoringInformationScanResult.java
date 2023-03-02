package com.shishifubing.plugins.nexus.model.information.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResult;
import com.shishifubing.plugins.nexus.scanner.AbstractScanner;

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