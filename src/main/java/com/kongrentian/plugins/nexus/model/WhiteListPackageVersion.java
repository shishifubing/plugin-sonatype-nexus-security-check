package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    private final LocalDate allowedDate;
    @JsonProperty("allowed")
    private final boolean allowed;

    public WhiteListPackageVersion(LocalDate allowedDate, boolean allowed) {
        this.allowedDate = allowedDate;
        this.allowed = allowed;
    }

    public LocalDate getAllowedDate() {
        return allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
