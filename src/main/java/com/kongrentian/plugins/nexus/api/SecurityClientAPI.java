package com.kongrentian.plugins.nexus.api;

import com.kongrentian.plugins.nexus.model.CheckRequest;
import com.kongrentian.plugins.nexus.model.ScanResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface SecurityClientAPI {

    @PUT("api/v1/check")
    Call<ScanResult> check(@Body CheckRequest body);

}

