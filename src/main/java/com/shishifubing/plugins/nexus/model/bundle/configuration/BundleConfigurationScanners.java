package com.shishifubing.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BundleConfigurationScanners implements Serializable {

    @JsonProperty
    private BundleConfigurationScannerLocal local;

    @JsonProperty
    private BundleConfigurationScannerRemote remote;

    public BundleConfigurationScanners() {
        local = new BundleConfigurationScannerLocal();
        remote = new BundleConfigurationScannerRemote();
    }

    public BundleConfigurationScanners(BundleConfigurationScannerLocal local,
                                       BundleConfigurationScannerRemote remote) {
        this.local = local;
        this.remote = remote;
    }

    public BundleConfigurationScannerLocal getLocal() {
        return local;
    }

    public BundleConfigurationScanners setLocal(BundleConfigurationScannerLocal local) {
        this.local = local;
        return this;
    }

    public BundleConfigurationScannerRemote getRemote() {
        return remote;
    }

    public BundleConfigurationScanners setRemote(BundleConfigurationScannerRemote remote) {
        this.remote = remote;
        return this;
    }
}
