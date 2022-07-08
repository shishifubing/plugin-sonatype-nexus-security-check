package com.kongrentian.plugins.nexus.capability;

public enum SecurityCapabilityKey {
  ENABLE_SCAN_REMOTE("security.enable.scan.remote", "false"),
  ENABLE_MONITORING("security.enable.monitoring", "false"),
  ENABLE_SCAN_LOCAL("security.enable.scan.local", "true"),

  HTTP_SSL_VERIFY("security.http.ssl.verify", "false"),
  HTTP_USER_AGENT("security.http.user_agent", ""),
  HTTP_CONNECTION_TIMEOUT("security.http.connection.timeout", "30"),
  HTTP_READ_TIMEOUT("security.http.connection.read_timeout", "60"),
  HTTP_WRITE_TIMEOUT("security.http.connection.write_timeout", "60"),
  HTTP_FAIL_ON_REQUEST_ERRORS("security.http.fail_on_request_errors", "true"),

  SCAN_REMOTE_URL("security.scan.remote.url", "https://localhost"),
  SCAN_REMOTE_TOKEN("security.scan.remote.token", ""),
  SCAN_REMOTE_INTERVAL_MINUTES("security.scan.remote.interval.minutes", "5"),

  MONITORING_URL("security.monitoring.url", "https://localhost:5601"),
  MONITORING_LOGIN("security.monitoring.login", ""),
  MONITORING_PASSWORD("security.monitoring.password", ""),

  SCAN_LOCAL_LAST_MODIFIED("security.scan.local.last_modified", "2022-02-20"),
  SCAN_LOCAL_WHITE_LIST("security.scan.local.white_list", "{}");

  private final String propertyKey;
  private final String defaultValue;

  SecurityCapabilityKey(String propertyKey, String defaultValue) {
    this.propertyKey = propertyKey;
    this.defaultValue = defaultValue;
  }

  public String propertyKey() {
    return propertyKey;
  }

  public String defaultValue() {
    return defaultValue;
  }
}
