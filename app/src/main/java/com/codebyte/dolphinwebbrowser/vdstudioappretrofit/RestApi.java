package com.codebyte.dolphinwebbrowser.vdstudioappretrofit;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
    public static String BASE_URL = "http://google.com/complete/";
    public static String BASE_URL_news = "https://cloud.feedly.com/v3/search/";
    private static Retrofit retrofit;
    private static Retrofit retrofit1;

    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()).retryOnConnectionFailure(true).connectTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES).build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(buildClient()).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        }
        return retrofit;
    }

    public static Retrofit getClient1() {
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder().baseUrl(BASE_URL_news).client(buildClient()).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        }
        return retrofit1;
    }
}
