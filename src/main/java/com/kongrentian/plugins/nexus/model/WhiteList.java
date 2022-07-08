package com.kongrentian.plugins.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.io.Serializable;
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

    public boolean isUserIn(String user) {
        return users.contains(user);
    }

    public boolean isComponentIn(CheckRequest checkRequest) {
        return getVersion(checkRequest.getRepository().getFormat(),
                checkRequest.getComponent()) != null;

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
            String repositoryFormat, CheckRequestComponent component) {
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

