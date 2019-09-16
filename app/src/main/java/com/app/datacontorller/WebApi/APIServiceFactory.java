package com.app.datacontorller.WebApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class APIServiceFactory {

    public static final String BASE_URL = "http://35.231.3.136:4000/";
    //    public static final String BASE_URL = "http://ec2-52-15-49-241.us-east-2.compute.amazonaws.com:3400/";
    public static final String ALTERNATIVE_URL = "http://tethr.it/tests/";
    public static final String BACKUP_URL = "http://106.51.133.175:5700/";


    public static Retrofit getRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            Response response = chain.proceed(request);

            return response;
        });

        OkHttpClient OkHttpClient = httpClient.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKUP_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getNewRtofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            Response response = chain.proceed(request);

            return response;
        });

        OkHttpClient OkHttpClient = httpClient.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ALTERNATIVE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getBackupRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            Response response = chain.proceed(request);

            return response;
        });

        OkHttpClient OkHttpClient = httpClient.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ALTERNATIVE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient)
                .build();

        return retrofit;
    }


}

