package com.kongrentian.plugins.nexus.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.view.Content;

public class CheckRequest implements Serializable {

    @JsonProperty
    private final CheckRequestRepository repository;
    @JsonProperty
    private final CheckRequestComponent component;
    @JsonProperty
    private final String userId;

    public CheckRequest(String userId, Repository repository, Content content,
                        Asset asset, Component component) {
        this.repository = new CheckRequestRepository(repository);
        this.component = new CheckRequestComponent(content, asset, component);
        this.userId = userId;
    }

    public CheckRequestRepository getRepository() {
        return repository;
    }

    public CheckRequestComponent getComponent() {
        return component;
    }

    public String getUserId() {
        return userId;
    }
}

class CheckRequestRepository implements Serializable {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String format;

    public CheckRequestRepository(Repository repository) {
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

class CheckRequestComponent implements  Serializable {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String group;
    @JsonProperty
    private final String version;
    @JsonProperty("last_modified")
    private final DateTime lastModified;
    @JsonProperty
    private final String asset;

    public CheckRequestComponent(Content content, Asset asset,
                                 Component component) {
        this.lastModified = (DateTime) content
                .getAttributes().get("last_modified");
        this.name = component.name();
        this.group = component.group();
        this.version = component.version();
        this.asset = asset.name();
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public DateTime getLastModified() {
        return lastModified;
    }


    public String getAsset() {
        return asset;
    }
}