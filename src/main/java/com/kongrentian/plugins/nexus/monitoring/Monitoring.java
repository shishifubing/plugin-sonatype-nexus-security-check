package com.kongrentian.plugins.nexus.monitoring;

import com.kongrentian.plugins.nexus.api.MonitoringApi;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.model.MonitoringInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class Monitoring {


    static final Logger LOG = LoggerFactory.getLogger(Monitoring.class);

    private final SecurityCapabilityHelper securityCapabilityHelper;

    @Inject
    public Monitoring(final SecurityCapabilityHelper securityCapabilityHelper) {
        this.securityCapabilityHelper = securityCapabilityHelper;
    }

    public void send(MonitoringInformation information) {
        SecurityCapabilityConfiguration config = securityCapabilityHelper
                .getCapabilityConfiguration();
        if (!config.isEnableMonitoring()) {
            return;
        }
        try {
            sendImpl(information, config);
        } catch (Throwable exception) {
            LOG.error("Could not send monitoring information", exception);
        }
    }

    public void sendImpl(MonitoringInformation information,
                         SecurityCapabilityConfiguration config) throws IOException {
        MonitoringApi api = securityCapabilityHelper.getMonitoringApi();
        StringBuilder builder = new StringBuilder(
                SecurityCapabilityHelper.jsonMapper
                        .writeValueAsString(information));
        builder.insert(0, "{\"index\":{ } }\n");
        api.bulk(builder.toString(),
                config.getMonitoringIndex(),
                SecurityCapabilityHelper.todayDate(),
                config.getMonitoringPipeline()).execute();
    }
}
