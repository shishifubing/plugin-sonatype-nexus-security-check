package com.kongrentian.plugins.nexus.capability.task;

import com.kongrentian.plugins.nexus.api.BundleConfigurationApi;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.sonatype.nexus.scheduling.TaskSupport;
import retrofit2.Response;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;

import static com.kongrentian.plugins.nexus.capability.SecurityCapability.STATUS_KEY_TASK;
import static java.lang.String.format;

@Named
public class SecurityCapabilityUpdateTask extends TaskSupport {


    private final BundleHelper bundleHelper;

    @Inject
    public SecurityCapabilityUpdateTask(final BundleHelper bundleHelper) {
        this.bundleHelper = bundleHelper;
    }

    @Override
    protected Object execute() throws IOException {
        BundleConfigurationApi api = bundleHelper.getBundleConfigurationApi();
        Map<String, Object> status = bundleHelper.getCapabilityStatus();
        status.put(STATUS_KEY_TASK, "Getting a new config");

        BundleConfiguration newConfig;
        try {
            bundleHelper.getCapabilityConfiguration();
            Response<BundleConfiguration> response =
                    api.get(bundleHelper
                            .getCapabilityConfiguration()
                            .getConfigUrlRequest()
                    ).execute();
            String responseMessage = response.message();
            log.debug("Config update response: {}",
                    responseMessage);
            newConfig = response.body();
            if (!response.isSuccessful() || newConfig == null) {
                throw new RuntimeException(
                        "Invalid response code or null config: "
                                + response.code()
                                + ", "
                                + responseMessage);
            }
        } catch (Throwable exception) {
            String message = format("Could not update config: %s",
                    ExceptionUtils.getStackTrace(exception));
            log.error(message);
            status.put(STATUS_KEY_TASK, message);
            throw exception;
        }
        bundleHelper.setBundleConfiguration(newConfig);
        status.put(STATUS_KEY_TASK,
                "Successfully updated the config");
        return null;
    }


    @Override
    public String getMessage() {
        return (String) bundleHelper
                .getCapabilityStatus()
                .getOrDefault(STATUS_KEY_TASK, "");
    }

}
