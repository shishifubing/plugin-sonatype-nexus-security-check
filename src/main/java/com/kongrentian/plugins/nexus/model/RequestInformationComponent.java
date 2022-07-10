package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Component;
import org.sonatype.nexus.repository.view.Content;

import java.io.Serializable;

public class RequestInformationComponent implements Serializable {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String group;
    @JsonProperty
    private final String version;
    @JsonProperty("last_modified")
    private final DateTime lastModified;
    @JsonProperty("asset_path")
    private final String assetPath;
    @JsonProperty
    private final String extension;
    @JsonIgnore
    private final Asset asset;

    public RequestInformationComponent(Content content, Asset asset,
                                       Component component) {
        lastModified = (DateTime) content
                .getAttributes().get("last_modified");
        name = component.name();
        group = component.group();
        version = component.version();
        assetPath = asset.name();
        this.asset = asset;
        String[] assetSplit = assetPath.split("\\.");
        extension = assetSplit[assetSplit.length - 1];
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

    public String getAssetPath() {
        return assetPath;
    }

    public String getExtension() {
        return extension;
    }

    public Asset getAsset() {
        return asset;
    }
}
