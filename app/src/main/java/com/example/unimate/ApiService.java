package com.example.unimate;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {

    // Personal Chat
    @FormUrlEncoded
    @POST("send_message.php")
    Call<ResponseModel> sendMessage(
            @Field("sender_id") String senderId,
            @Field("receiver_id") String receiverId,
            @Field("message") String message
    );

    @GET("fetch_messages.php")
    Call<List<MessageModel>> getMessages(
            @Query("sender_id") String senderId,
            @Query("receiver_id") String receiverId
    );

    // Group Chat
    @GET("get_groups.php")
    Call<List<GroupModel>> getGroups();

    @FormUrlEncoded
    @POST("create_group.php")
    Call<ResponseModel> createGroup(
            @Field("group_name") String groupName,
            @Field("creator_id") String creatorId
    );

    @FormUrlEncoded
    @POST("join_group.php")
    Call<ResponseModel> joinGroup(
            @Field("group_id") String groupId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("send_group_message.php")
    Call<ResponseModel> sendGroupMessage(
            @Field("group_id") String groupId,
            @Field("sender_id") String senderId,
            @Field("message") String message
    );

    @GET("fetch_group_messages.php")
    Call<List<MessageModel>> getGroupMessages(
            @Query("group_id") String groupId
    );
}
