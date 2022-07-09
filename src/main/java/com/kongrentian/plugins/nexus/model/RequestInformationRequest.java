package com.kongrentian.plugins.nexus.model;

import org.sonatype.nexus.repository.view.Request;

import java.io.Serializable;

public class RequestInformationRequest implements Serializable {

    private final String path;

    public RequestInformationRequest(Request request) {
        this.path = request.getPath();
    }

    public String getPath() {
        return path;
    }
}
