package com.kongrentian.plugins.nexus.api;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Client {

    private static final String DEFAULT_BASE_URL = "https://localhost/api/v1/";
    private static final String DEFAULT_USER_AGENT = "";
    private static final long DEFAULT_CONNECTION_TIMEOUT = 30_000L;
    private static final long DEFAULT_READ_TIMEOUT = 60_000L;
    private static final long DEFAULT_WRITE_TIMEOUT = 60_000L;

    private final Retrofit retrofit;

    public Client(Config config) throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, MILLISECONDS);

        if (config.trustAllCertificates) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = SSLConfiguration.buildUnsafeTrustManager();
            sslContext.init(null, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
        } else if (config.sslCertificatePath != null && !config.sslCertificatePath.isEmpty()) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = SSLConfiguration.buildCustomTrustManager(config.sslCertificatePath);
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }

        builder.addInterceptor(new ServiceInterceptor(config.auth, config.userAgent));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // to deserialize joda datetime
        objectMapper.registerModule(new JodaModule());
        // No serializer found for class com.google.common.hash.HashCode$BytesHashCode
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // Direct self-reference leading to cycle
        objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);

        retrofit = new Retrofit.Builder().client(builder.build())
                .baseUrl(config.baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    public ClientAPI buildSync() {
        return retrofit.create(ClientAPI.class);
    }

    public static final class Config {
        String baseUrl;
        String auth;
        String userAgent;
        boolean trustAllCertificates;
        String sslCertificatePath;

        public Config(String token) {
            this(DEFAULT_BASE_URL, token);
        }

        public Config(String baseUrl, String auth) {
            this(baseUrl, auth, DEFAULT_USER_AGENT);
        }

        public Config(String baseUrl, String auth, String userAgent) {
            this(baseUrl, auth, userAgent, false);
        }

        public Config(String baseUrl, String auth, String userAgent, boolean trustAllCertificates) {
            this(baseUrl, auth, userAgent, trustAllCertificates, "");
        }

        public Config(String baseUrl, String auth, String userAgent, boolean trustAllCertificates, String sslCertificatePath) {
            this.baseUrl = baseUrl;
            this.auth = auth;
            this.userAgent = userAgent;
            this.trustAllCertificates = trustAllCertificates;
            this.sslCertificatePath = sslCertificatePath;
        }
    }
}
