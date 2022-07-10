package com.kongrentian.plugins.nexus.api;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MonitoringApi {
    @POST("{target}-{date}/_bulk")
    void bulk(@Body String body,
              @Query("pipeline") String pipeline,
              @Path("target") String target,
              @Path("date") String date);
}
