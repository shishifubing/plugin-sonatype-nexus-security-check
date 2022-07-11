package com.kongrentian.plugins.nexus.model.white_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.io.Serializable;

public class WhiteListPackageVersion implements Serializable {
    @JsonProperty("allowed_date")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime allowedDate;
    @JsonProperty
    private boolean allowed;

    public WhiteListPackageVersion(DateTime allowedDate, boolean allowed) {
        this.allowed = allowed;
        this.allowedDate = allowedDate;
    }

    public WhiteListPackageVersion() {
        this.allowed = false;
        this.allowedDate = null;
    }

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
