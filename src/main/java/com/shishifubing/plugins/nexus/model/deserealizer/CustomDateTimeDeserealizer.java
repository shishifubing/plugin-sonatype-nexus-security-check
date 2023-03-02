package com.shishifubing.plugins.nexus.model.deserealizer;

import com.fasterxml.jackson.datatype.joda.cfg.FormatConfig;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import org.joda.time.DateTime;

public class CustomDateTimeDeserealizer extends DateTimeDeserializer {
    public CustomDateTimeDeserealizer() {
        // no arg constructor providing default values for super call
        super(DateTime.class, FormatConfig.DEFAULT_DATEONLY_FORMAT);
    }
}