package com.kongrentian.plugins.nexus.model.scanresult;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.Serializable;

public class ScanResultException implements Serializable {

    @JsonProperty("class")
    private final Class<? extends Throwable> exceptionClass;
    @JsonProperty("stack_trace")
    private final String stackTrace;

    public ScanResultException(Throwable exception) {
        this.exceptionClass = exception.getClass();
        this.stackTrace = ExceptionUtils.getStackTrace(exception);
    }
}
