package com.kongrentian.plugins.nexus.model.white_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.io.Serializable;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty
    private final boolean allowed = false;
    @JsonProperty("allowed_date")
    private DateTime allowedDate;

    public WhiteListPackageVersion() {
    }

    public DateTime getAllowedDate() {
        return allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
