package com.shishifubing.plugins.nexus.monitoring;

import com.shishifubing.plugins.nexus.api.MonitoringApi;
import com.shishifubing.plugins.nexus.main.BundleHelper;
import com.shishifubing.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import com.shishifubing.plugins.nexus.model.information.monitoring.MonitoringInformation;
import com.shishifubing.plugins.nexus.logging.SecurityLogConfiguration;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

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
            SecurityLogConfiguration.LOG.error("Could not send monitoring information", exception);
        }
    }

    public void sendImpl(MonitoringInformation information,
                         BundleConfiguration config) throws IOException {
        MonitoringApi api = bundleHelper.getMonitoringApi();

        api.bulk("{\"index\":{ } }\n"
                        + BundleHelper.MAPPER_JSON
                        .writeValueAsString(information),
                config.getMonitoring().getIndex(),
                BundleHelper.todayDate(),
                config.getMonitoring().getPipeline()
        ).execute();
    }
}
