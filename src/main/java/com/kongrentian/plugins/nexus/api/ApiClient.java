package com.kongrentian.plugins.nexus.api;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ApiClient {

    private static <TEMPLATE> TEMPLATE create(Class<TEMPLATE> api,
                                              SecurityCapabilityConfiguration config,
                                              String baseUrl,
                                              String auth) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getHttpConnectionTimeout(), MILLISECONDS)
                .readTimeout(config.getHttpReadTimeout(), MILLISECONDS)
                .writeTimeout(config.getHttpWriteTimeout(), MILLISECONDS);

        try {
            if (!config.getHttpSSLVerify()) {
                buildUnsafeTrustManager(builder);
            }
        } catch (KeyManagementException | NoSuchAlgorithmException exception) {
            LOG.error("Could not build unsafe trust manager", exception);
        }

        builder.addInterceptor(
                new ApiClientServiceInterceptor(auth, config.getHttpUserAgent()));

        return new Retrofit
                .Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(
                        JacksonConverterFactory.create(
                                BundleHelper.jsonMapper))
                .build()
                .create(api);
    }

    public static MonitoringApi createMonitoringApi(
            BundleConfiguration bundleConfiguration,
            SecurityCapabilityConfiguration capabilityConfiguration) {
        return create(MonitoringApi.class,
                capabilityConfiguration,
                bundleConfiguration.getMonitoring().getBaseUrl(),
                bundleConfiguration.getMonitoring().getAuth());
    }

    public static RemoteScanApi createRemoteScanApi(
            BundleConfiguration bundleConfiguration,
            SecurityCapabilityConfiguration capabilityConfiguration) {
        return create(RemoteScanApi.class,
                capabilityConfiguration,
                bundleConfiguration.getScanners().getRemote().getBaseUrl(),
                bundleConfiguration.getScanners().getRemote().getAuth());
    }

    public static BundleConfigurationApi createBundleConfigurationApi(
            SecurityCapabilityConfiguration capabilityConfiguration) {
        return create(BundleConfigurationApi.class,
                capabilityConfiguration,
                capabilityConfiguration.getConfigUrlBase(),
                capabilityConfiguration.getConfigAuth());
    }

    private static void buildUnsafeTrustManager(OkHttpClient.Builder builder)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = ApiClientSSLConfiguration.buildUnsafeTrustManager();
        sslContext.init(null, trustManagers, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
    }

}
