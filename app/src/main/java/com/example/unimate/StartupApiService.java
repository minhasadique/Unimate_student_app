package com.example.unimate;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface StartupApiService {

    @FormUrlEncoded
    @POST("post_startup.php")
    Call<ResponseModel> postStartup(
            @Field("title") String title,
            @Field("description") String description,
            @Field("author") String author,
            @Field("roles") String roles,
            @Field("contact") String contact
    );

    @Headers("Content-Type: application/json")
    @POST("post_startup.php")
    Call<ResponseModel> postStartup(@Body Startup startup);


}
