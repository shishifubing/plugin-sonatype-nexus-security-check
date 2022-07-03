package com.kongrentian.plugins.nexus.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.Type;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Response;

public class CheckRequest implements Serializable {

    @JsonProperty
    public CheckRequestRepository repository;
    @JsonProperty
    public CheckRequestComponent component;
    @JsonProperty
    public String userId;

    public CheckRequest(String userId, Repository repository, Content content,
                        Asset asset, Component component) {
        this.repository = new CheckRequestRepository(repository);
        this.component = new CheckRequestComponent(content, asset, component);
        this.userId = userId;
    }
}

class CheckRequestRepository implements Serializable {
    @JsonProperty
    public String name;
    @JsonProperty
    public String type;
    @JsonProperty
    public String format;

    public CheckRequestRepository(Repository repository) {
        this.name = repository.getName();
        this.type = repository.getType().getValue();
        this.format = repository.getFormat().getValue();
    }
}

class CheckRequestComponent implements  Serializable {
    @JsonProperty
    public String name;
    @JsonProperty
    public String group;
    @JsonProperty
    public String version;
    @JsonProperty("last_modified")
    public DateTime lastModified;
    @JsonProperty("last_updated")
    public DateTime lastUpdated;
    @JsonProperty
    public String asset;

    public CheckRequestComponent(Content content, Asset asset,
                                 Component component) {
        this.lastModified = (DateTime) content
                .getAttributes().get("last_modified");
        this.name = component.name();
        this.group = component.group();
        this.version = component.version();
        this.lastUpdated = asset.lastUpdated();
        this.asset = asset.name();
    }
}