package com.kongrentian.plugins.nexus.api;

import com.kongrentian.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BundleConfigurationApi {
    @GET("{path}")
    Call<BundleConfiguration> get(@Path("path") String path);
}