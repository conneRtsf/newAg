package com.example.newag.mvp.model.api;

import android.util.Log;

import com.example.newag.base.Constant;
import com.example.newag.mvp.model.api.support.LoggingInterceptor;
import com.example.newag.mvp.model.api.support.LoggingProduction;
import com.example.newag.mvp.model.api.support.LoggingSale;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClientUtils {
    /**
     * 获取okhttpClick
     *
     * @return
     */
    public static OkHttpClient getOkhttpClick() {
        LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
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

    public static Retrofit getRetrofitWithStringAdapter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(StringConverterFactory.create()) // 添加String转换器
                .client(getOkhttpClick())
                .build();
        return retrofit;
    }

    public static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Log.e("HttpMessage", message);
//            WriteLogUtil.log("HttpMessage" +  message);
        }
    }

    public static class MyLogProduction implements LoggingProduction.Logger {
        @Override
        public void log(String message) {
            Log.e("HttpMessage", message);
//            WriteLogUtil.log("HttpMessage" +  message);
        }
    }

    public static class MyLogSale implements LoggingSale.Logger {
        @Override
        public void log(String message) {
            Log.e("HttpMessage", message);
//            WriteLogUtil.log("HttpMessage" +  message);
        }
    }

    public static Retrofit getRetrofitWithGsonAdapter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(getOkhttpClick())
                .build();
        return retrofit;
    }

    public static OkHttpClient getOkhttpClickNan() {
        LoggingProduction logging = new LoggingProduction(new MyLogProduction());
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

    /**
     * 男频网络请求
     *
     * @return
     */
    public static Retrofit getRetrofitWithGsonNanAdapter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(getOkhttpClickNan())
                .build();
        return retrofit;
    }

    public static OkHttpClient getOkhttpClickNv() {
        LoggingSale logging = new LoggingSale(new MyLogSale());
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

}
