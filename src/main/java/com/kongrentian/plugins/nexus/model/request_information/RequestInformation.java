package com.kongrentian.plugins.nexus.model.request_information;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Request;

import java.io.Serializable;

public class RequestInformation implements Serializable {

    @JsonProperty
    private final RequestInformationRepository repository;
    @JsonProperty
    private final RequestInformationComponent component;

    @JsonProperty
    private final RequestInformationRequest request;


    public RequestInformation(String userId, Repository repository, Content content,
                              Asset asset, Component component, Request request) {
        this.repository = new RequestInformationRepository(repository);
        this.component = new RequestInformationComponent(content, asset, component);
        this.request = new RequestInformationRequest(request, userId);
    }

    public RequestInformationRepository getRepository() {
        return repository;
    }

    public RequestInformationComponent getComponent() {
        return component;
    }

    public RequestInformationRequest getRequest() {
        return request;
    }
}

