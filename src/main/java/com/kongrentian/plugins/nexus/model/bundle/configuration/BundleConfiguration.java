package com.kongrentian.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BundleConfiguration implements Serializable {

    @JsonProperty
    private final BundleConfigurationMonitoring monitoring;
    @JsonProperty
    private final BundleConfigurationScanners scanners;

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

    public BundleConfigurationScanners getScanners() {
        return scanners;
    }

}


