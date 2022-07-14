package com.kongrentian.plugins.nexus.logging;

import com.kongrentian.plugins.nexus.main.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.log.LogConfigurationCustomizer;
import org.sonatype.nexus.common.log.LoggerLevel;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class SecurityLogConfiguration
        implements LogConfigurationCustomizer {

    public static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public void customize(final Configuration configuration) {
        configuration.setLoggerLevel(RequestHandler.class.getName(),
                LoggerLevel.INFO);
    }
}