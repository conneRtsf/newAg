package com.example.newag.di.module;

import com.example.newag.mvp.model.api.HttpClientUtils;
import com.example.newag.mvp.model.api.ProjectApi;
import com.example.newag.mvp.model.api.StringConverterFactory;
import com.example.newag.mvp.model.api.support.LoggingInterceptor;
import com.example.newag.mvp.model.api.support.LoggingProduction;
import com.example.newag.mvp.model.api.support.LoggingSale;

import java.util.concurrent.TimeUnit;


import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class AquApiModule {

    @Provides
    @Named("ok-0")
    public OkHttpClient provideOkHttpClient0() {
        LoggingInterceptor logging = new LoggingInterceptor(new HttpClientUtils.MyLog());
        logging.setLevel(LoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发 关闭
                .addInterceptor(logging)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    @Provides
    @Named("ok-1")
    public OkHttpClient provideOkHttpClient1() {
        LoggingProduction logging = new LoggingProduction(new HttpClientUtils.MyLogProduction());
        logging.setLevel(LoggingProduction.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发 关闭
                .addInterceptor(logging)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    @Provides
    @Named("ok-2")
    public OkHttpClient provideOkHttpClient2() {
        LoggingSale logging = new LoggingSale(new HttpClientUtils.MyLogSale());
        logging.setLevel(LoggingSale.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发 关闭
                .addInterceptor(logging)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    @Provides
    @Named("covert-g")
    public Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Named("covert-s")
    public Converter.Factory provideStringConverter() {
        return StringConverterFactory.create();
    }

    @Provides
    @Named("api-0")
    protected ProjectApi provideReadService0(@Named("ok-0") OkHttpClient okHttpClient, @Named("covert-g") Converter.Factory converterFactory) {
        return ProjectApi.getInstance0(okHttpClient, converterFactory);
    }

    @Provides
    @Named("api-1")
    protected ProjectApi provideReadService1(@Named("ok-1") OkHttpClient okHttpClient, @Named("covert-g") Converter.Factory converterFactory) {
        return ProjectApi.getInstance1(okHttpClient, converterFactory);
    }

    @Provides
    @Named("api-2")
    protected ProjectApi provideReadService2(@Named("ok-2") OkHttpClient okHttpClient, @Named("covert-g") Converter.Factory converterFactory) {
        return ProjectApi.getInstance2(okHttpClient, converterFactory);
    }

    @Provides
    @Named("api-stringconver")
    protected ProjectApi provideReadService3(@Named("ok-0") OkHttpClient okHttpClient, @Named("covert-s") Converter.Factory converterFactory) {
        return ProjectApi.getInstance3(okHttpClient, converterFactory);
    }

    @Provides
    @Named("api-4")
    protected ProjectApi provideReadService4(@Named("ok-1") OkHttpClient okHttpClient, @Named("covert-s") Converter.Factory converterFactory) {
        return ProjectApi.getInstance4(okHttpClient, converterFactory);
    }

    @Provides
    @Named("api-5")
    protected ProjectApi provideReadService5(@Named("ok-2") OkHttpClient okHttpClient, @Named("covert-s") Converter.Factory converterFactory) {
        return ProjectApi.getInstance5(okHttpClient, converterFactory);
    }

}
