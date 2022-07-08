package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * <pre>
 * users:
 *   - user
 * packages:
 *   format:
 *     package:
 *       version:
 *         allowed_date: 2022-07-07
 *         allowed: true
 * </pre>
 */
public class WhiteList implements Serializable {

    @JsonProperty
    private final List<String> users;
    @JsonProperty
    private final Map<String, Map<String, Map<String, WhiteListPackageVersion>>> packages;

    public WhiteList(List<String> users,
                     Map<String, Map<String, Map<String, WhiteListPackageVersion>>> packages) {
        this.users = users;
        this.packages = packages;
    }
    public WhiteList() {
        users = new ArrayList<>();
        packages = new HashMap<>();
    }

    public static WhiteList fromYAML(String yaml) throws JsonProcessingException {
        return new ObjectMapper(
                new YAMLFactory().disable(
                        YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .readValue(yaml, WhiteList.class);

    }

    public boolean isUserIn(String user) {
        return users.contains(user);
    }

    public boolean isComponentIn(RequestInfo requestInfo) {
        return getVersion(requestInfo.getRepository().getFormat(),
                requestInfo.getComponent()) != null;

    }

    @Nullable
    public Map<String, Map<String, WhiteListPackageVersion>> getComponents(String repositoryFormat) {
        return packages.get(repositoryFormat);
    }

    @Nullable
    public Map<String, WhiteListPackageVersion> getVersions(String format, String componentName) {
        Map<String, Map<String, WhiteListPackageVersion>> components = getComponents(format);
        return components == null ? null : components.get(componentName);
    }

    @Nullable
    public WhiteListPackageVersion getVersion(
            String repositoryFormat, RequestInfoComponent component) {
        String[] names = new String[]{
                format("%s:%s", component.getGroup(), component.getName()),
                component.getName()
        };
        for (String name : names) {
            Map<String, WhiteListPackageVersion> versions = getVersions(
                    repositoryFormat, name.toLowerCase());
            if (versions == null) {
                continue;
            }
            WhiteListPackageVersion version = versions.get(component.getVersion());
            if (version == null) {
                continue;
            }
            return version;
        }
        return null;
    }

    public List<String> getUsers() {
        return users;
    }

}

