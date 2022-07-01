package com.kongrentian.plugins.nexus.capability;

import java.util.Map;

import org.sonatype.nexus.capability.CapabilityConfigurationSupport;

import static com.kongrentian.plugins.nexus.capability.CapabilityKey.API_TOKEN;
import static com.kongrentian.plugins.nexus.capability.CapabilityKey.API_URL;

public class CapabilityConfiguration extends CapabilityConfigurationSupport {
  private final String apiUrl;
  private final String apiToken;

  CapabilityConfiguration(Map<String, String> properties) {
    apiUrl = properties.getOrDefault(API_URL.propertyKey(), API_URL.defaultValue());
    apiToken = properties.getOrDefault(API_TOKEN.propertyKey(), "");
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public String getApiToken() {
    return apiToken;
  }
}
