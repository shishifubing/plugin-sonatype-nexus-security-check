package com.kongrentian.plugins.nexus.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.view.Response;

public class CheckRequest implements Serializable {

    @JsonProperty("repository_name")
    public final String repositoryName;
    @JsonProperty("repository_type")
    public final String repositoryType;
    @JsonProperty("repository_format")
    public final String repositoryFormat;
    @JsonProperty("last_modified")
    public final DateTime lastModified;
    @JsonProperty("asset")
    public final Asset asset;

    public CheckRequest(Response response, Repository repository, Asset asset) {
        this.repositoryFormat = repository.getFormat().getValue();
        this.repositoryType = repository.getType().getValue();
        this.repositoryName = repository.getName();
        this.lastModified = (DateTime) response.getAttributes().get("last_modified");
        this.asset = asset;
    }
}