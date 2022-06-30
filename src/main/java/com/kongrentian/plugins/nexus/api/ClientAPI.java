package com.kongrentian.plugins.nexus.api;

import com.kongrentian.plugins.nexus.model.ScanResult;
import org.sonatype.nexus.common.collect.AttributesMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;

public interface ClientAPI {

    @HTTP(method = "GET", path = "api/v1/check", hasBody = true)
    Call<ScanResult> check(@Body AttributesMap securityAttributes);

}

