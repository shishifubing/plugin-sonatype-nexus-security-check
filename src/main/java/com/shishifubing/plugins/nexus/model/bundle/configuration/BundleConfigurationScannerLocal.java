package com.shishifubing.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shishifubing.plugins.nexus.main.BundleHelper;
import com.shishifubing.plugins.nexus.model.white_list.WhiteList;
import org.joda.time.DateTime;

import java.io.Serializable;

public class BundleConfigurationScannerLocal implements Serializable {

    @JsonProperty
    private boolean enabled = false;

    @JsonProperty("fail_on_errors")
    private boolean failOnErrors = true;

    @JsonProperty("last_modified")
    private DateTime lastModified = BundleHelper
            .parseTime("2022-02-20");

    @JsonProperty("white_list")
    private WhiteList whiteList;


    public BundleConfigurationScannerLocal() {
        whiteList = new WhiteList();
    }

    public BundleConfigurationScannerLocal(WhiteList whiteList) {
        this.whiteList = whiteList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BundleConfigurationScannerLocal setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isFailOnErrors() {
        return failOnErrors;
    }

    public BundleConfigurationScannerLocal setFailOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
        return this;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public BundleConfigurationScannerLocal setLastModified(DateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public WhiteList getWhiteList() {
        return whiteList;
    }

    public BundleConfigurationScannerLocal setWhiteList(WhiteList whiteList) {
        this.whiteList = whiteList;
        return this;
    }
}
