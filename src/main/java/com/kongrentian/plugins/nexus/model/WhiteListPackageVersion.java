package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.io.Serializable;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    private DateTime allowedDate;
    @JsonProperty
    private final boolean allowed = false;

    public WhiteListPackageVersion() {
    }

    public DateTime getAllowedDate() {
        return allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
