package com.shishifubing.plugins.nexus.api;

import com.shishifubing.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface BundleConfigurationApi {
    @GET("{path}")
    Call<BundleConfiguration> get(@Path(value = "path", encoded = true) String path,
                                  @QueryMap Map<String, String> parameters);
}