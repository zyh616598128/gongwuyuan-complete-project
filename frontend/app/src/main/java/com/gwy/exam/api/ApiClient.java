package com.gwy.exam.api;

import android.content.Context;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.gwy.exam.utils.SharedPreferenceUtil;

public class ApiClient {
    private static final String BASE_URL = "http://172.25.175.205:8081/"; // 本地后端地址 (for Android emulator)
// For direct device connection, use your local IP: "http://<your_local_ip>:8081/"
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // Add JWT token interceptor
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                // Get token from SharedPreferenceUtil
                String token = null;
                try {
                    token = SharedPreferenceUtil.getInstance().getToken();
                } catch (IllegalStateException e) {
                    // SharedPreferenceUtil not initialized yet
                }

                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}