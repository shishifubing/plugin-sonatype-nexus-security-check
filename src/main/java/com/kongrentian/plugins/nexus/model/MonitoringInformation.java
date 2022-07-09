package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitoringInformation implements Serializable {

    @JsonProperty("request_information")
    private final RequestInformation requestInformation;

    @JsonProperty
    private boolean allowed = true;

    @JsonProperty
    private final List<MonitoringInformationScanResult> scans;



    public MonitoringInformation(RequestInformation requestInformation) {
        this.requestInformation = requestInformation;
        this.scans = new ArrayList<>();
    }

    public void add(MonitoringInformationScanResult scanResult) {
        scans.add(scanResult);
    }

    public String getLastReason() {
        return scans.get(scans.size()-1).getResult().getReason();
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }
}