package com.kongrentian.plugins.nexus.capability;

public enum CapabilityKey {
  API_URL("scan.api.url", ""),
  API_TOKEN("scan.api.token", "");
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
