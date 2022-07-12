package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.formfields.AbstractFormField;
import org.sonatype.nexus.formfields.FormField;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class SecurityCapabilityField<TEMPLATE> {

    private final Class<? extends AbstractFormField<TEMPLATE>> formField;
    private final Function<String, TEMPLATE> convertFunction;

    private final String propertyKey;
    private final String defaultValue;
    private final String description;


    // you always have to specify convertFunction, even if
    // it is useless,
    // otherwise you will not be able to use `convert`
    // you can always do it if you remove templating, but
    // then you will not be able to generate fields
    public SecurityCapabilityField(
            String propertyKey,
            String defaultValue,
            String description,
            Class<? extends AbstractFormField<TEMPLATE>> formField,
            Function<String, TEMPLATE> convertFunction) {
        this.propertyKey = propertyKey;
        this.defaultValue = defaultValue;
        this.description = description;
        this.formField = formField;
        this.convertFunction = convertFunction;
    }

    public static List<FormField> createFields()
            throws RuntimeException {
        return Arrays.stream(SecurityCapabilityKey.values())
                .map(securityCapabilityField -> {
                    try {
                        return securityCapabilityField
                                .getField()
                                .createFormField();
                    } catch (NoSuchMethodException
                             | InvocationTargetException
                             | InstantiationException
                             | IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                }).collect(Collectors.toList());
    }

    public static Map<String, String> createDefaultProperties()
            throws RuntimeException {
        Map<String, String> result = new HashMap<>();
        for (SecurityCapabilityKey key : SecurityCapabilityKey.values()) {
            result.put(key.propertyKey(), key.defaultValue());
        }
        return result;
    }

    public TEMPLATE convert(String input) {
        return convertFunction.apply(input);
    }

    public FormField<TEMPLATE> createFormField()
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        AbstractFormField<TEMPLATE> result = formField.getDeclaredConstructor(
                        String.class,
                        String.class,
                        String.class,
                        boolean.class)
                .newInstance(
                        propertyKey,
                        propertyKey,
                        description,
                        FormField.OPTIONAL);
        result.setInitialValue(convert(defaultValue));
        return result;
    }

    public String propertyKey() {
        return propertyKey;
    }

    public String defaultValue() {
        return defaultValue;
    }

}
