package com.shishifubing.plugins.nexus.model.bundle.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BundleConfigurationMonitoring implements Serializable {

    @JsonProperty
    private boolean enabled = false;

    @JsonProperty("base_url")
    private String baseUrl = "https://localhost:9200";

    @JsonProperty
    private String auth = "";

    @JsonProperty("anonymous_user_id")
    private String anonymousUserId = "anonymous";

    @JsonProperty
    private String pipeline = "timestamp";

    @JsonProperty
    private String index = "nexus-asset-requests";

    public BundleConfigurationMonitoring() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BundleConfigurationMonitoring setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public BundleConfigurationMonitoring setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getAuth() {
        return auth;
    }

    public BundleConfigurationMonitoring setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    public String getAnonymousUserId() {
        return anonymousUserId;
    }

    public BundleConfigurationMonitoring setAnonymousUserId(String anonymousUserId) {
        this.anonymousUserId = anonymousUserId;
        return this;
    }

    public String getPipeline() {
        return pipeline;
    }

    public BundleConfigurationMonitoring setPipeline(String pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public BundleConfigurationMonitoring setIndex(String index) {
        this.index = index;
        return this;
    }
}
