package com.hzn.sample.network;

import com.hzn.sample.network.result.AuthResult;

import retrofit.RestAdapter;

/**
 * Created by hzn on 15-1-30.
 */
public class DataService {
    private static final String API_URL = "http://www.ibridgelearn.com";

    private static RestAdapter sAdapter;

    static {
        sAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static ServiceAPI getServiceAPI() {
        return sAdapter.create(ServiceAPI.class);
    }

    public static String auth(String username, String password) {
        AuthResult result = getServiceAPI().auth("password",
                username, password,
                "325183e9e2aa1c054f10885b47a1663db43b65ce972e2004886f91e386cfcd3f",
                "94d835eec88320e95026596ce4c2db4f552368af2dd5f35e39c28dec903b814c");

        return result.access_token;
    }
}
