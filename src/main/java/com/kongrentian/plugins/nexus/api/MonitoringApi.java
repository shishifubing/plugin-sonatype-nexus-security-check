package com.kongrentian.plugins.nexus.api;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MonitoringApi {
    @POST("{index}-{date}/_bulk")
    void bulk(@Body String body,
              @Path("index") String index,
              @Path("date") String date,
              @Query("pipeline") String pipeline);
}
