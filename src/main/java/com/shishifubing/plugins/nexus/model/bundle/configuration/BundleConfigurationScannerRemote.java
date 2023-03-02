package com.shishifubing.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BundleConfigurationScannerRemote implements Serializable {

    @JsonProperty
    private boolean enabled = false;
    @JsonProperty("base_url")
    private String baseUrl = "https://localhost";
    @JsonProperty
    private String auth = "";
    @JsonProperty("fail_on_errors")
    private boolean failOnErrors = true;
    @JsonProperty
    private long interval = 5;

    public BundleConfigurationScannerRemote() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public BundleConfigurationScannerRemote setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getAuth() {
        return auth;
    }

    public BundleConfigurationScannerRemote setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    public boolean isFailOnErrors() {
        return failOnErrors;
    }

    public BundleConfigurationScannerRemote setFailOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
        return this;
    }

    public long getInterval() {
        return interval;
    }

    public BundleConfigurationScannerRemote setInterval(long interval) {
        this.interval = interval;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BundleConfigurationScannerRemote setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
