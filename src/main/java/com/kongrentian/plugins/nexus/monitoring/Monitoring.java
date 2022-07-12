package com.kongrentian.plugins.nexus.monitoring;

import com.kongrentian.plugins.nexus.api.MonitoringApi;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import com.kongrentian.plugins.nexus.model.information.monitoring.MonitoringInformation;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;
import static com.kongrentian.plugins.nexus.main.BundleHelper.MAPPER_JSON;

@Named
public class Monitoring implements Serializable {


    private final BundleHelper bundleHelper;

    @Inject
    public Monitoring(final BundleHelper bundleHelper) {
        this.bundleHelper = bundleHelper;
    }

    public void send(MonitoringInformation information) {
        BundleConfiguration config = bundleHelper
                .getBundleConfiguration();
        if (!config.getMonitoring().isEnabled()) {
            return;
        }
        try {
            sendImpl(information, config);
        } catch (Throwable exception) {
            LOG.error("Could not send monitoring information", exception);
        }
    }

    public void sendImpl(MonitoringInformation information,
                         BundleConfiguration config) throws IOException {
        MonitoringApi api = bundleHelper.getMonitoringApi();

        api.bulk("{\"index\":{ } }\n"
                        + MAPPER_JSON
                        .writeValueAsString(information),
                config.getMonitoring().getIndex(),
                BundleHelper.todayDate(),
                config.getMonitoring().getPipeline()
        ).execute();
    }
}
