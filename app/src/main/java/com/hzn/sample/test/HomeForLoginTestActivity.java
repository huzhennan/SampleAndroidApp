package com.hzn.sample.test;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hzn.sample.R;
import com.hzn.sample.activitys.BaseActivity;
import com.hzn.sample.activitys.LoginActivity;
import com.hzn.sample.auth.AccountAuthenticator;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hzn on 15-1-14.
 */
public class HomeForLoginTestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccountAuthenticator.getDefaultAccount(this) == null) {
            LoginActivity.startLogin(this);
            finish();
        }
    }

    @OnClick(R.id.bt_get_token)
    public void getToken(View view) {
        AccountManager am = AccountManager.get(this);
        Account account = am.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE)[0];

        AppObservable.bindActivity(this,
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            String token = am.blockingGetAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE, true);
                            subscriber.onNext(token);
                            subscriber.onCompleted();
                        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String token) {
                        AccountManager am = AccountManager.get(HomeForLoginTestActivity.this);
                        am.invalidateAuthToken(AccountAuthenticator.ACCOUNT_TYPE, token);
                        Toast.makeText(HomeForLoginTestActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        // am.blockingGetAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE, true);
    }

    @OnClick(R.id.bt_invalidate_token)
    void OnInValidateTokenButton(View view) {
        AccountManager am = AccountManager.get(this);
        am.invalidateAuthToken(AccountAuthenticator.ACCOUNT_TYPE, null);
    }
}
