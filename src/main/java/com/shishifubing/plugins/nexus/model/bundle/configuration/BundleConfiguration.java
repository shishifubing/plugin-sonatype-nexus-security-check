package com.shishifubing.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BundleConfiguration implements Serializable {

    @JsonProperty
    private BundleConfigurationMonitoring monitoring;
    @JsonProperty
    private BundleConfigurationScanners scanners;


    public BundleConfiguration() {
        monitoring = new BundleConfigurationMonitoring();
        scanners = new BundleConfigurationScanners();
    }

    public BundleConfiguration(BundleConfigurationMonitoring monitoring,
                               BundleConfigurationScanners scanners) {
        this.monitoring = monitoring;
        this.scanners = scanners;
    }

    public BundleConfigurationMonitoring getMonitoring() {
        return monitoring;
    }

    public BundleConfiguration setMonitoring(BundleConfigurationMonitoring monitoring) {
        this.monitoring = monitoring;
        return this;
    }

    public BundleConfigurationScanners getScanners() {
        return scanners;
    }

    public BundleConfiguration setScanners(BundleConfigurationScanners scanners) {
        this.scanners = scanners;
        return this;
    }
}


