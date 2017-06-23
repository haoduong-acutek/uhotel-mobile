package com.uhotel;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "http://lv-api.acuteksolutions.com/cxf/ws/messagebus/rest/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();



    public static <S> S createService(Class<S> serviceClass) {

        httpClient.connectTimeout(0, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(20, TimeUnit.SECONDS);
        httpClient.writeTimeout(20, TimeUnit.SECONDS);

        httpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);

        GsonBuilder builder = new GsonBuilder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(API_BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(builder.create()));


//setup cache
        File httpCacheDirectory = new File(MyApplicationContext.getInstance().getExternalCacheDir(), "retrofit2");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);


        OkHttpClient okHttpClient = httpClient.cache(cache).build();

        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();


        return retrofit.create(serviceClass);
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (Utility.isNetworkOn(MyApplicationContext.getInstance())) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
}