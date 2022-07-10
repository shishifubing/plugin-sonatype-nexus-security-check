package com.kongrentian.plugins.nexus.model.scan_result;

public enum ScanResultType {
    WHITE_LIST_CONTAINS_EXTENSION,
    WHITE_LIST_CONTAINS_USER,
    WHITE_LIST_PACKAGE_VERSION_ALLOWED,
    WHITE_LIST_PACKAGE_VERSION_DATE_VALID,
    WHITE_LIST_PACKAGE_VERSION_DATE_INVALID,
    WHITE_LIST_PACKAGE_VERSION_MISSING,
    WHITE_LIST_LAST_MODIFIED_MISSING,
    WHITE_LIST_CONTAINS_REPOSITORY,
    WHITE_LIST_CONTAINS_FORMAT,
    LAST_MODIFIED_INVALID,
    LAST_MODIFIED_VALID,
    EXCEPTION,
    EXCEPTION_IGNORED,
}