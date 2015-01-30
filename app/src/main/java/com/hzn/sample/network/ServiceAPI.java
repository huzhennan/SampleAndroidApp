package com.hzn.sample.network;

import com.hzn.sample.network.result.AuthResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by hzn on 15-1-30.
 */
public interface ServiceAPI {
    @FormUrlEncoded
    @POST("/oauth/token")
    AuthResult auth(@Field("grant_type") String grant_type,
                    @Field("username") String username,
                    @Field("password") String password,
                    @Field("client_id") String client_id,
                    @Field("client_secret") String client_secret);
}
