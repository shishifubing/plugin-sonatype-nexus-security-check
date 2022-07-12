package com.kongrentian.plugins.nexus.model.whitelist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kongrentian.plugins.nexus.model.deserealizer.CustomDateTimeDeserealizer;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.io.Serializable;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    @JsonDeserialize(using = CustomDateTimeDeserealizer.class)
    private DateTime allowedDate;
    @JsonProperty
    private boolean allowed;

    @Nullable
    public DateTime getAllowedDate() {
        return allowedDate;
    }

    public void setAllowedDate(DateTime allowedDate) {
        this.allowedDate = allowedDate;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
