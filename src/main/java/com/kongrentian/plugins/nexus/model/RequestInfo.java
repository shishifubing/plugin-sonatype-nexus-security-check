package com.kongrentian.plugins.nexus.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.view.Content;

public class RequestInfo implements Serializable {

    @JsonProperty
    private final RequestInfoRepository repository;
    @JsonProperty
    private final RequestInfoComponent component;
    @JsonProperty
    private final String userId;

    public RequestInfo(String userId, Repository repository, Content content,
                       Asset asset, Component component) {
        this.repository = new RequestInfoRepository(repository);
        this.component = new RequestInfoComponent(content, asset, component);
        this.userId = userId;
    }

    public RequestInfoRepository getRepository() {
        return repository;
    }

    public RequestInfoComponent getComponent() {
        return component;
    }

    public String getUserId() {
        return userId;
    }
}

class RequestInfoRepository implements Serializable {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String format;

    public RequestInfoRepository(Repository repository) {
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

class RequestInfoComponent implements  Serializable {
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

    public RequestInfoComponent(Content content, Asset asset,
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