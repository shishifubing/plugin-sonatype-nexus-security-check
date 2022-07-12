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

    CONFIG_URL_BASE(new SecurityCapabilityFormField<>(
            "security.config.url.base",
            "https://gitlab.com",
            "Base url for config requests",
            UrlFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_URL_REQUEST(new SecurityCapabilityFormField<>(
            "security.config.url.request",
            "kongrentian-groups/java/nexus-plugin-security-check/-/raw/master/plugin_config.yml",
            "Request for fetch the config",
            StringTextFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_AUTH(new SecurityCapabilityFormField<>(
            "security.config.auth",
            "",
            "Auth for config fetching, either login:password or token",
            PasswordFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    CONFIG_OVERRIDE(new SecurityCapabilityFormField<>(
            "security.config.override",
            "{}",
            "Override remote config",
            TextAreaFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),


    HTTP_SSL_VERIFY(new SecurityCapabilityFormField<>(
            "security.http.ssl.verify",
            "false",
            "Whether to verify ssl certificates",
            CheckboxFormField.class,
            Boolean::parseBoolean
    )),
    HTTP_USER_AGENT(new SecurityCapabilityFormField<>(
            "security.http.user_agent",
            "",
            "User agent for all requests",
            StringTextFormField.class,
            SecurityCapabilityKey::uselessStringPlaceholder
    )),
    HTTP_CONNECTION_TIMEOUT(new SecurityCapabilityFormField<>(
            "security.http.connection.timeout",
            "30",
            "Connection timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_READ_TIMEOUT(new SecurityCapabilityFormField<>(
            "security.http.read_timeout",
            "60",
            "Read timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong
    )),
    HTTP_WRITE_TIMEOUT(new SecurityCapabilityFormField<>(
            "security.http.write_timeout",
            "60",
            "Write timeout for all requests, milliseconds",
            NumberTextFormField.class,
            Long::parseLong));

    private final SecurityCapabilityFormField<?> securityCapabilityFormField;

    SecurityCapabilityKey(SecurityCapabilityFormField<?> securityCapabilityFormField) {
        this.securityCapabilityFormField = securityCapabilityFormField;
    }

    private static String uselessStringPlaceholder(String input) {
        return input;
    }

    public String propertyKey() {
        return securityCapabilityFormField.propertyKey();
    }

    public String defaultValue() {
        return securityCapabilityFormField.defaultValue();
    }

    public SecurityCapabilityFormField<?> getField() {
        return securityCapabilityFormField;
    }

}
