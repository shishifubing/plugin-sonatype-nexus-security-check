package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    public LocalDate allowedDate;
    @JsonProperty
    public boolean allowed = false;

    public WhiteListPackageVersion() {
    }

    public boolean isAllowed() {
        if (allowed) {
            return true;
        }
        if (allowedDate == null) {
            return false;
        }
        return allowedDate.isAfter(LocalDate.now());
    }

}
