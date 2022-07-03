package com.kongrentian.plugins.nexus.api;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.kongrentian.plugins.nexus.capability.CapabilityConfiguration;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Client {

    private final ClientAPI api;
    private final CapabilityConfiguration configuration;

    public Client(CapabilityConfiguration config) throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectionTimeout(), MILLISECONDS)
                .readTimeout(config.getReadTimeout(), MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), MILLISECONDS);

        if (config.getApiTrustAllCertificates()) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = SSLConfiguration.buildUnsafeTrustManager();
            sslContext.init(null, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
        }

        builder.addInterceptor(new ServiceInterceptor(config.getApiAuth(),
                config.getUserAgent()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // to deserialize joda datetime
        mapper.registerModule(new JodaModule());

        Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                .baseUrl(config.getApiUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        api = retrofit.create(ClientAPI.class);
        configuration = config;
    }

    public ClientAPI getApi() {
        return api;
    }

    public CapabilityConfiguration getConfiguration() {
        return configuration;
    }
}
