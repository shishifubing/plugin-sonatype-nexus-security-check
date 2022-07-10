package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.io.Serializable;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    public DateTime allowedDate;
    @JsonProperty
    public boolean allowed = false;

    public WhiteListPackageVersion() {
    }

    public boolean isAllowed(DateTime lastModified) {
        if (allowed) {
            return true;
        }
        if (allowedDate == null) {
            return false;
        }
        return allowedDate.isBefore(lastModified);
    }

}
