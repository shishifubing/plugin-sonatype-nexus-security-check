package com.kongrentian.plugins.nexus.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SecurityClientMonitoringApi {
    @POST("{target}-{date}/_bulk")
    Call<?> bulk(@Body String body,
                  @Query("pipeline") String pipeline,
                  @Path("target") String target,
                 @Path("date") String date);
}
