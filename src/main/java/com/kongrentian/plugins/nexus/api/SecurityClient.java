package com.kongrentian.plugins.nexus.api;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SecurityClient {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityClient.class);

    private static <TEMPLATE> TEMPLATE create(Class<TEMPLATE> api,
                                              SecurityCapabilityConfiguration config,
                                              String baseUrl,
                                              String auth) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getHttpConnectionTimeout(), MILLISECONDS)
                .readTimeout(config.getHttpReadTimeout(), MILLISECONDS)
                .writeTimeout(config.getHttpWriteTimeout(), MILLISECONDS);

        try {
            if (!config.httpSSLVerify()) {
                buildUnsafeTrustManager(builder);
            }
        } catch (KeyManagementException | NoSuchAlgorithmException exception) {
            LOG.error("Could not build unsafe trust manager", exception);
        }

        builder.addInterceptor(new ServiceInterceptor(auth, config.getHttpUserAgent()));

        return new Retrofit
                .Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(
                        JacksonConverterFactory.create(
                                SecurityCapabilityHelper.jsonMapper))
                .build()
                .create(api);
    }

    public static MonitoringApi createMonitoringApi(SecurityCapabilityConfiguration config) {
        return create(MonitoringApi.class,
                config,
                config.getMonitoringUrl(),
                config.getMonitoringAuth());
    }

    public static RemoteScanApi createSecurityClientApi(SecurityCapabilityConfiguration config) {
        return create(RemoteScanApi.class,
                config,
                config.getScanRemoteUrl(),
                config.getScanRemoteAuth());
    }

    private static void buildUnsafeTrustManager(OkHttpClient.Builder builder)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = SSLConfiguration.buildUnsafeTrustManager();
        sslContext.init(null, trustManagers, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
    }

}
