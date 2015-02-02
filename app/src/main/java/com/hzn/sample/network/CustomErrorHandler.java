package com.hzn.sample.network;

import java.net.ConnectException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hzn on 15-2-2.
 */
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError cause) {
        switch (cause.getKind()) {
            case NETWORK:
                throw new ConnectNetworkError();

            case HTTP:
                Response response = cause.getResponse();
                if (response != null && response.getStatus() == 401) {
                    return new UnauthorizedException();
                }
        }

        return cause;
    }

}
