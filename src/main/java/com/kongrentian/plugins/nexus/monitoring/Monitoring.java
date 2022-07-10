package com.kongrentian.plugins.nexus.monitoring;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.model.MonitoringInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Monitoring {


    static final Logger LOG = LoggerFactory.getLogger(Monitoring.class);

    private final SecurityCapabilityHelper securityCapabilityHelper;

    @Inject
    public Monitoring(final SecurityCapabilityHelper securityCapabilityHelper) {
        this.securityCapabilityHelper = securityCapabilityHelper;
    }

    public void send(MonitoringInformation information) {
        if (!securityCapabilityHelper.getCapabilityConfiguration().isEnableMonitoring()) {
            return;
        }
        try {
            sendImpl(information);
        } catch (Throwable exception) {
            LOG.error("Could not send monitoring information", exception);
        }
    }

    public void sendImpl(MonitoringInformation information) {

    }
}
