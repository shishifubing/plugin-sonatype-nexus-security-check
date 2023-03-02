package com.shishifubing.plugins.nexus.model.information.monitoring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shishifubing.plugins.nexus.model.information.request.RequestInformation;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResultType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitoringInformation implements Serializable {

    @JsonProperty("request_information")
    private final RequestInformation requestInformation;
    @JsonProperty
    private final List<MonitoringInformationScanResult> scans;
    @JsonProperty
    private boolean allowed = true;


    public MonitoringInformation(RequestInformation requestInformation) {
        this.requestInformation = requestInformation;
        this.scans = new ArrayList<>();
    }

    public void add(MonitoringInformationScanResult scanResult) {
        scans.add(scanResult);
    }

    @JsonIgnore
    public ScanResultType getLastReason() {
        return scans.get(scans.size() - 1).getResult().getType();
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}