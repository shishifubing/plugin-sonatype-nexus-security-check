package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.formfields.*;

import java.io.Serializable;


/**
 * this monstrosity is created in order to
 * avoid duplication in {@link SecurityCapabilityDescriptor}
 * <p></p>
 * cannot avoid duplication in {@link SecurityCapabilityConfiguration} though
 */
public enum SecurityCapabilityKey implements Serializable {

    CONFIG_URL_BASE(new SecurityCapabilityField<>(
            "security.config.url.base",
            "https://gitlab.com",
            "Base url for config requests",
            UrlFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_URL_REQUEST(new SecurityCapabilityField<>(
            "security.config.url.request",
            "kongrentian-groups/java/nexus-plugin-security-check/-/raw/master/plugin_config.yml",
            "Request for fetch the config",
            StringTextFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_AUTH(new SecurityCapabilityField<>(
            "security.config.auth",
            "",
            "Auth for config fetching, either login:password or token",
            PasswordFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_OVERRIDE(new SecurityCapabilityField<>(
            "security.config.override",
            "{}",
            "Override remote config",
            TextAreaFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
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
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    HTTP_CONNECTION_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.connection.timeout",
            "30",
            "Connection timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_READ_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.read_timeout",
            "60",
            "Read timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_WRITE_TIMEOUT(new SecurityCapabilityField<>(
            "security.http.write_timeout",
            "60",
            "Write timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong));

    private final SecurityCapabilityField<?> securityCapabilityField;

    SecurityCapabilityKey(SecurityCapabilityField<?> securityCapabilityField) {
        this.securityCapabilityField = securityCapabilityField;
    }

    private static String uselessStringPlaceholder(String input) {
        return input;
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

}
