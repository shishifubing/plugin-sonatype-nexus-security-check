package com.kongrentian.plugins.nexus.logging;

import org.sonatype.nexus.common.log.LogConfigurationCustomizer;
import org.sonatype.nexus.common.log.LoggerLevel;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class SecurityLogConfigurationCustomizer
        implements LogConfigurationCustomizer {

    @Override
    public void customize(final Configuration configuration) {
        configuration.setLoggerLevel("random_logger",
                LoggerLevel.ERROR);
    }
}