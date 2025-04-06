package com.example.unimate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartupRetrofitClient {
    private static final String BASE_URL = "http://172.20.10.2/StudentAppBackend/api/"; // Update with your actual API URL
    private static Retrofit retrofit = null;

    public static StartupApiService getStartupApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(StartupApiService.class);
    }
}
