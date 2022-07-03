package com.kongrentian.plugins.nexus.capability;

public enum CapabilityKey {
  API_URL("scan.api.url", "https://localhost"),
  API_TOKEN("scan.api.auth", ""),
  API_TRUST_ALL_CERTIFICATES("scan.api.trust_all_certificates", "false"),
  SCAN_INTERVAL("scan.interval", "5"),
  FAIL_ON_REQUEST_ERRORS("scan.fail_on_request_errors", "true"),
  USER_AGENT("scan.connection.user_agent", ""),
  CONNECTION_TIMEOUT("scan.connection.timeout", "30"),
  READ_TIMEOUT("scan.connection.read_timeout", "60"),
  WRITE_TIMEOUT("scan.connection.write_timeout", "60");
  private final String propertyKey;
  private final String defaultValue;

  CapabilityKey(String propertyKey, String defaultValue) {
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
