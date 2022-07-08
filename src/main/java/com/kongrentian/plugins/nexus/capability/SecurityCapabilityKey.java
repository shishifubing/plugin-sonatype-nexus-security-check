package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.formfields.*;

import java.time.Instant;

/**
 * this monstrosity is created in order to
 * avoid duplication in {@link SecurityCapabilityField}
 * <p>
 * cannot avoid duplication in {@link SecurityCapabilityConfiguration} though
 */
public enum SecurityCapabilityKey {
    ENABLE_SCAN_REMOTE(new SecurityCapabilityField<>(
            "security.enable.scan.remote",
            "false",
            "Enables remote scans",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    ENABLE_MONITORING(new SecurityCapabilityField<>(
            "security.enable.monitoring",
            "false",
            "Enables local scans",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    ENABLE_SCAN_LOCAL(new SecurityCapabilityField<>(
            "security.enable.scan.local",
            "true",
            "Enables monitoring",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),

    FAIL_ON_SCAN_ERRORS(new SecurityCapabilityField<>(
            "security.fail.on_scan_errors",
            "true",
            "Whether to throw an error if a scan request fails",
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
            String::format
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
            String::format
    )),
    SCAN_REMOTE_TOKEN(new SecurityCapabilityField<>(
            "security.scan.remote.token",
            "",
            "Token for remote scans",
            PasswordFormField.class,
            String::format
    )),
    SCAN_REMOTE_INTERVAL(new SecurityCapabilityField<>(
            "security.scan.remote.interval.minutes",
            "5",
            "Interval for remote scans, minutes",
            NumberTextFormField.class,
            Long::parseLong
    )),

    MONITORING_URL(new SecurityCapabilityField<>(
            "security.monitoring.url",
            "https://localhost:5601",
            "Base url for monitoring",
            UrlFormField.class,
            String::format
    )),
    MONITORING_LOGIN(new SecurityCapabilityField<>(
            "security.monitoring.login",
            "",
            "Login for monitoring",
            StringTextFormField.class,
            String::format
    )),
    MONITORING_PASSWORD(new SecurityCapabilityField<>(
            "security.monitoring.password",
            "",
            "Password for monitoring",
            PasswordFormField.class,
            String::format
    )),

    SCAN_LOCAL_LAST_MODIFIED(new SecurityCapabilityField<>(
            "security.scan.local.last_modified",
            "2022-02-20",
            "All last_modified dates after this one are blocked",
            StringTextFormField.class,
            String::format
    )),
    SCAN_LOCAL_WHITE_LIST(new SecurityCapabilityField<>(
            "security.scan.local.white_list",
            "{}",
            "White list for packages and users",
            TextAreaFormField.class,
            String::format
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

    public String description() {
        return securityCapabilityField.description();
    }

    public SecurityCapabilityField<?> field() {
        return securityCapabilityField;
    }

}
