package com.kongrentian.plugins.nexus.capability.form_fields;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityDescriptor;
import org.sonatype.nexus.formfields.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * this monstrosity is created in order to
 * avoid duplication in {@link SecurityCapabilityDescriptor}
 * <p></p>
 * cannot avoid duplication in {@link SecurityCapabilityConfiguration} though
 */
public enum SecurityCapabilityKey implements SecurityCapabilityKeyInterface {
    ENABLE_SCAN_REMOTE(new SecurityCapabilityField<>(
            "security.enable.scan.remote",
            "false",
            "Enable remote scans",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    ENABLE_MONITORING(new SecurityCapabilityField<>(
            "security.enable.monitoring",
            "false",
            "Enable monitoring",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    ENABLE_SCAN_LOCAL(new SecurityCapabilityField<>(
            "security.enable.scan.local",
            "true",
            "Enable local scan",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),

    HTTP_SSL_VERIFY(new SecurityCapabilityField<>(
            "security.http.ssl.verify",
            "false",
            "Whether to verify ssl certificates",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    HTTP_USER_AGENT(new SecurityCapabilityField<>(
            "security.http.user_agent",
            "",
            "User agent for all requests",
            StringTextFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    HTTP_CONNECTION_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.connection.timeout",
            "30",
            "Connection timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_READ_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.connection.read_timeout",
            "60",
            "Read timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_WRITE_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.connection.write_timeout",
            "60",
            "Write timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),

    SCAN_REMOTE_URL(new SecurityCapabilityField<>(
            "security.scan.remote.url",
            "https://localhost",
            "Base url for remote scans",
            UrlFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    SCAN_REMOTE_AUTH(new SecurityCapabilityField<>(
            "security.scan.remote.auth",
            "",
            "Auth for remote scans, either login:password or token",
            PasswordFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    SCAN_REMOTE_FAIL_ON_ERRORS(new SecurityCapabilityField<>(
            "security.scan.remote.fail.on_scan_errors",
            "true",
            "Whether to fail the request on errors",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    SCAN_REMOTE_INTERVAL(new SecurityCapabilityField<>(
            "security.scan.remote.interval.minutes",
            "5",
            "Interval for remote scans, minutes",
            NumberTextFormField.class,
            Long::parseLong
    )),

    SCAN_LOCAL_FAIL_ON_ERRORS(new SecurityCapabilityField<>(
            "security.scan.local.fail.on_scan_errors",
            "true",
            "Whether to fail the request on errors",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    SCAN_LOCAL_LAST_MODIFIED(new SecurityCapabilityField<>(
            "security.scan.local.last_modified",
            "2022-02-20",
            "All last_modified dates after this one are blocked (inclusive)",
            StringTextFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    SCAN_LOCAL_WHITE_LIST(new SecurityCapabilityField<>(
            "security.scan.local.white_list",
            String.join("\n", new String[]{
                    "# do not leave any top keys without content,",
                    "# it will cause null pointer exception",
                    "# which will fail all local scans",
                    "formats:",
                    "    - format",
                    "repositories:",
                    "    - repository",
                    "extensions:",
                    "    - extension",
                    "users:",
                    "    - user",
                    "packages:",
                    "    pypi:",
                    "        pip:",
                    "            some_nonexistent_version:",
                    "                allowed: true",
                    "            some_other_version",
                    "                allowed_date: 2022-07-07"
            }),
            "White list for packages and users",
            TextAreaFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),

    MONITORING_URL(new SecurityCapabilityField<>(
            "security.monitoring.url",
            "https://localhost:9200",
            "Base url for monitoring",
            UrlFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    MONITORING_AUTH(new SecurityCapabilityField<>(
            "security.monitoring.auth",
            "",
            "Auth for monitoring, either login:password or token ",
            PasswordFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    MONITORING_ANONYMOUS_USER_ID(new SecurityCapabilityField<>(
            "security.monitoring.anonymous.user_id",
            "anonymous",
            "Username assigned to anonymous requests (for monitoring)",
            StringTextFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    MONITORING_PIPELINE(new SecurityCapabilityField<>(
            "security.monitoring.pipeline",
            "timestamp",
            "Which pipeline to use for bulk request",
            StringTextFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    )),
    MONITORING_INDEX(new SecurityCapabilityField<>(
            "security.monitoring.index",
            "nexus-assets",
            "Which index to use for bulk request",
            StringTextFormField.class,
            SecurityCapabilityKeyInterface::uselessStringPlaceholder
    ));

    private final SecurityCapabilityField<?> securityCapabilityField;

    SecurityCapabilityKey(SecurityCapabilityField<?> securityCapabilityField) {
        this.securityCapabilityField = securityCapabilityField;
    }

    public String propertyKey() {
        return securityCapabilityField.propertyKey();
    }

    public String defaultValue() {
        return securityCapabilityField.defaultValue();
    }

    public SecurityCapabilityField<?> getField() {
        return securityCapabilityField;
    }

    public List<SecurityCapabilityField<?>> getFields() {
        return Arrays.stream(values())
                .map(SecurityCapabilityKey::getField)
                .collect(Collectors.toList());
    }

}
