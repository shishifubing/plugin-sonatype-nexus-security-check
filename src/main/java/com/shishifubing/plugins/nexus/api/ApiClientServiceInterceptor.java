package com.shishifubing.plugins.nexus.api;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ApiClientServiceInterceptor implements Interceptor {

    private final String[] auth;
    private final String userAgent;

    public ApiClientServiceInterceptor(String auth, String userAgent) {
        this.auth = auth.split(":");
        this.userAgent = userAgent;
    }


    @Nonnull
    @Override
    public Response intercept(@Nonnull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        builder.addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", userAgent);
        if (auth.length == 1) {
            builder.addHeader("Authorization", "Bearer " + auth[0]);
        } else if (auth.length > 1) {
            builder.addHeader("Authorization", Credentials.basic(auth[0], auth[1]));
        }
        return chain.proceed(builder.build());
    }
}
