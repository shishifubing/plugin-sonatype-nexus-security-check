package com.kongrentian.plugins.nexus.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceInterceptor implements Interceptor {

    private final String auth;
    private final String userAgent;

    public ServiceInterceptor(@Nullable String auth, String userAgent) {
        this.auth = auth;
        this.userAgent = userAgent;
    }

    @Nonnull
    @Override
    public Response intercept(@Nonnull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        builder.addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", this.userAgent);
        if (this.auth != null && !this.auth.isEmpty()) {
            builder.addHeader("Authorization", "Basic " + this.auth);
        }

        return chain.proceed(builder.build());
    }
}
