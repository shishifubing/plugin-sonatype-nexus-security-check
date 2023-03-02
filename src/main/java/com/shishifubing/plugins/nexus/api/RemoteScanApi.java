package com.shishifubing.plugins.nexus.api;

import com.shishifubing.plugins.nexus.model.information.request.RequestInformation;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface RemoteScanApi {

    @PUT("api/v1/check")
    Call<ScanResult> check(@Body RequestInformation body);

}

