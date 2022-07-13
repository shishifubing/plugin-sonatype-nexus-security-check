package com.kongrentian.plugins.nexus.capability.task;

import com.kongrentian.plugins.nexus.api.BundleConfigurationApi;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.sonatype.nexus.scheduling.TaskSupport;
import retrofit2.Call;
import retrofit2.Response;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.time.Instant;
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
        SecurityCapabilityConfiguration config = bundleHelper.getCapabilityConfiguration();
        Call<BundleConfiguration> request = api.get(
                config.getConfigUrlRequest(),
                config.getConfigUrlParameters());
        log.info("request parameters: {}", config.getConfigUrlParameters());
        Response<BundleConfiguration> response = null;
        try {
            response = request.execute();
            String responseMessage = response.message();
            log.info("Config update response: {}",
                    responseMessage);
            newConfig = response.body();
            if (!response.isSuccessful() || newConfig == null) {
                throw new RuntimeException(format(
                        "Invalid response code or null config:\n%s, message\n'%s'\n",
                        response.code(),
                        responseMessage));
            }
        } catch (Throwable exception) {
            status.put(STATUS_KEY_TASK, format(
                    "Could not update config at %s:\n%s\n%s\n%s",
                    Instant.now(),
                    request.request().url(),
                    response,
                    ExceptionUtils.getStackTrace(exception)));
            throw exception;
        }
        bundleHelper.setBundleConfiguration(newConfig);
        status.put(STATUS_KEY_TASK,
                "Successfully updated the config"
                        + Instant.now());
        return null;
    }


    @Override
    public String getMessage() {
        return (String) bundleHelper
                .getCapabilityStatus()
                .getOrDefault(STATUS_KEY_TASK, "");
    }

}
