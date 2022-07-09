package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    private LocalDate allowedDate;
    @JsonProperty
    private boolean allowed;

    public WhiteListPackageVersion(LocalDate allowedDate, boolean allowed) {
        this.allowedDate = allowedDate;
        this.allowed = allowed;
    }

    public WhiteListPackageVersion() {
    }

    public LocalDate getAllowedDate() {
        return allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
