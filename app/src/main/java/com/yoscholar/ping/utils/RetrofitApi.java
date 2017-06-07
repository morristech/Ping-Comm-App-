package com.yoscholar.ping.utils;

import com.yoscholar.ping.retrofitPojo.conversation.Conversation;
import com.yoscholar.ping.retrofitPojo.conversations.ConversationData;
import com.yoscholar.ping.retrofitPojo.login.Login;
import com.yoscholar.ping.retrofitPojo.passwordSet.PasswordSet;
import com.yoscholar.ping.retrofitPojo.refreshToken.RefrehToken;
import com.yoscholar.ping.retrofitPojo.tokenDetails.TokenDetails;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class RetrofitApi {

    public static String baseUrl = "http://staging1.schoolsaamaan.website";
    public static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static ApiInterface getApiInterfaceInstance() {
        return getRetrofitInstance().create(ApiInterface.class);
    }

    public interface ApiInterface {

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=login")
        Call<Login> login(
                @Field("email") String email,
                @Field("password") String password,
                @Field("device_id") String deviceId,
                @Field("fcm_token") String fcmToken
        );

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=refresh_token")
        Call<RefrehToken> refreshToken(
                @Field("email") String email,
                @Field("user_id") String userId
        );

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=conversations")
        Call<ConversationData> conversations(
                @Field("email") String email,
                @Field("token") String token
        );

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=conversation_messages")
        Call<Conversation> conversationMessages(
                @Field("child_id") String childId,
                @Field("provider_id") String providerId,
                @Field("token") String token
        );

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=generate_otp")
        Call<TokenDetails> generateOTP(
                @Field("mobile") String mobile
        );

        @FormUrlEncoded
        @POST("agrimtool2/CommApp/CommAppAPI.php?function=change_password")
        Call<PasswordSet> setPassword(
                @Field("token_id") String tokenId,
                @Field("password") String password
        );
    }
}