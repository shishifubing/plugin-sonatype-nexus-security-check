package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    private final Instant allowedDate;
    private final boolean allowed;

    public WhiteListPackageVersion(Instant allowedDate, boolean allowed) {
        this.allowedDate = allowedDate;
        this.allowed = allowed;
    }

    public Instant getAllowedDate() {
        return allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
