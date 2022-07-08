package com.kongrentian.plugins.nexus.capability;

import org.sonatype.nexus.formfields.AbstractFormField;
import org.sonatype.nexus.formfields.FormField;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey.values;

public final class SecurityCapabilityField<TEMPLATE> {
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter
                    .ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault());

    private final Class<? extends AbstractFormField<TEMPLATE>> formField;
    private final Function<String, TEMPLATE> convertFunction;

    private final String propertyKey;
    private final String defaultValue;
    private final String description;

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

    public static LocalDate parseTime(String text) {
        return LocalDate.parse(text, dateTimeFormatter);
    }

    public static List<FormField> createFields() throws RuntimeException {
        return Arrays.stream(values()).map(securityCapabilityKey -> {
            try {
                return securityCapabilityKey.field().createField();
            } catch (NoSuchMethodException
                     | InvocationTargetException
                     | InstantiationException
                     | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }).collect(Collectors.toList());
    }

    public TEMPLATE convert(String input) {
        return convertFunction.apply(input);
    }

    public FormField<TEMPLATE> createField()
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

    public String description() {
        return description;
    }

}
