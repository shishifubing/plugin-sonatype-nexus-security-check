package com.kongrentian.plugins.nexus.model.request_information;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sonatype.nexus.repository.Repository;

import java.io.Serializable;

public class RequestInformationRepository implements Serializable {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String format;

    public RequestInformationRepository(Repository repository) {
        this.name = repository.getName();
        this.type = repository.getType().getValue();
        this.format = repository.getFormat().getValue();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }
}
