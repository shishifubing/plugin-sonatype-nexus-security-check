package com.kongrentian.plugins.nexus.model.scan_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.exception.ExceptionUtils;

public class ScanResultException {

    @JsonProperty("class")
    private final Class<? extends Throwable> exceptionClass;
    @JsonProperty("stack_trace")
    private final String stackTrace;

    public ScanResultException(Throwable exception) {
        this.exceptionClass = exception.getClass();
        this.stackTrace = ExceptionUtils.getStackTrace(exception);
    }
}
