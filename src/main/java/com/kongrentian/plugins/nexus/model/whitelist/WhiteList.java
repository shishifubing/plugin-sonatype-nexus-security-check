package com.kongrentian.plugins.nexus.model.whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kongrentian.plugins.nexus.model.information.request.RequestInformation;
import com.kongrentian.plugins.nexus.model.information.request.RequestInformationComponent;
import com.kongrentian.plugins.nexus.model.information.request.RequestInformationRepository;
import com.kongrentian.plugins.nexus.model.scanresult.ScanResultType;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

import static java.lang.String.format;

/**
 * <pre>
 * formats:
 *   - go
 * repositories:
 *   - pypi.org-proxy
 * extensions:
 *   - json
 * users:
 *   - admin
 * packages:
 *   pypi:
 *     pip:
 *       22.1.1:
 *         allowed_date: 2022-07-07
 *         allowed: true
 * </pre>
 */
public class WhiteList implements Serializable {

    @JsonIgnore
    private final static List<ScanResultType> scanFailureTypes = Arrays.asList(
            ScanResultType.WHITE_LIST_PACKAGE_VERSION_DATE_INVALID,
            ScanResultType.WHITE_LIST_PACKAGE_VERSION_MISSING,
            ScanResultType.WHITE_LIST_LAST_MODIFIED_MISSING);
    @JsonProperty
    private final List<String> extensions;
    @JsonProperty
    private final List<String> repositories;
    @JsonProperty
    private final List<String> users;
    @JsonProperty
    private final List<String> formats;
    @JsonProperty
    private final Map<String, Map<String, Map<String, WhiteListPackageVersion>>> packages;

    public WhiteList() {
        users = new ArrayList<>();
        packages = new HashMap<>();
        extensions = new ArrayList<>();
        repositories = new ArrayList<>();
        formats = new ArrayList<>();
    }

    public static boolean isFailure(ScanResultType scanResultType) {
        return scanFailureTypes.contains(scanResultType);
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
            String repositoryFormat, RequestInformationComponent component) {
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

    public ScanResultType contains(RequestInformation requestInformation) {
        RequestInformationComponent component = requestInformation.getComponent();
        RequestInformationRepository repository = requestInformation.getRepository();

        if (users.contains(requestInformation.getRequest().getUserId())) {
            return ScanResultType.WHITE_LIST_CONTAINS_USER;
        }
        if (formats.contains(repository.getFormat())) {
            return ScanResultType.WHITE_LIST_CONTAINS_FORMAT;
        }
        if (repositories.contains(repository.getName())) {
            return ScanResultType.WHITE_LIST_CONTAINS_REPOSITORY;
        }
        if (extensions.contains(component.getExtension())) {
            return ScanResultType.WHITE_LIST_CONTAINS_EXTENSION;
        }
        WhiteListPackageVersion version = getVersion(
                repository.getFormat(), component);
        if (version == null) {
            return ScanResultType.WHITE_LIST_PACKAGE_VERSION_MISSING;
        }
        if (version.isAllowed()) {
            return ScanResultType.WHITE_LIST_PACKAGE_VERSION_ALLOWED;
        }
        DateTime lastModified = component.getLastModified();
        if (lastModified == null) {
            return ScanResultType.WHITE_LIST_LAST_MODIFIED_MISSING;
        }
        DateTime allowedDate = version.getAllowedDate();
        if (allowedDate != null && lastModified.isBefore(allowedDate)) {
            return ScanResultType.WHITE_LIST_PACKAGE_VERSION_DATE_VALID;
        }
        return ScanResultType.WHITE_LIST_PACKAGE_VERSION_DATE_INVALID;
    }

}

