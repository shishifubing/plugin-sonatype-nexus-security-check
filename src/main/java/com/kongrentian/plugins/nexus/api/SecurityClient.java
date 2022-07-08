package com.kongrentian.plugins.nexus.api;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SecurityClient {

    private final SecurityClientAPI api;
    private final SecurityCapabilityConfiguration configuration;
    private final ObjectMapper mapper;
    public SecurityClient(SecurityCapabilityConfiguration config) throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getHttpConnectionTimeout(), MILLISECONDS)
                .readTimeout(config.getHttpReadTimeout(), MILLISECONDS)
                .writeTimeout(config.getHttpWriteTimeout(), MILLISECONDS);

        if (!config.isHttpSSLVerify()) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = SSLConfiguration.buildUnsafeTrustManager();
            sslContext.init(null, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
        }

        builder.addInterceptor(new ServiceInterceptor(config.getScanRemoteToken(),
                config.getHttpUserAgent()));
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // to deserialize joda datetime
        mapper.registerModule(new JodaModule());

        Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                .baseUrl(config.getScanRemoteUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        api = retrofit.create(SecurityClientAPI.class);
        configuration = config;
    }

    public SecurityClientAPI getApi() {
        return api;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public SecurityCapabilityConfiguration getConfiguration() {
        return configuration;
    }
}
