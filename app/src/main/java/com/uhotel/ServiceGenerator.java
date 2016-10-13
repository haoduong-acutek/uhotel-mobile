package com.uhotel;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    public static final String API_BASE_URL = "http://lv-api.acuteksolutions.com/cxf/ws/messagebus/rest/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create());


    public static <S> S createService(Class<S> serviceClass) {

        httpClient.connectTimeout(0, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(20, TimeUnit.SECONDS);
        httpClient.writeTimeout(20, TimeUnit.SECONDS);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(WrapperInterface.class   , new IWrapperAdapter());
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(API_BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(builder.create()));

        OkHttpClient okHttpClient = httpClient.build();

        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();

        return retrofit.create(serviceClass);
    }

}