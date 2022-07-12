package com.kongrentian.plugins.nexus.model.information.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sonatype.nexus.repository.view.Request;

import java.io.Serializable;

public class RequestInformationRequest implements Serializable {

    @JsonProperty
    private final String path;
    @JsonProperty
    private final String userId;

    public RequestInformationRequest(Request request, String userId) {
        this.path = request.getPath();
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public String getUserId() {
        return userId;
    }
}
