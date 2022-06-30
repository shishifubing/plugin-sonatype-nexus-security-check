package com.kongrentian.plugins.nexus.model;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanResult implements Serializable {

    @JsonProperty("issue")
    public String issue = "";
    @JsonProperty("allowed")
    public boolean allowed;
    @JsonProperty("reason")
    public String reason = "";
    @JsonProperty("checkDate")
    public Instant checkDate;
}

