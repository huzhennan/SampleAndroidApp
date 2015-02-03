package com.hzn.sample.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hzn.sample.R;
import com.hzn.sample.test.HomeForLoginTestActivity;
import com.hzn.sample.auth.AccountAuthenticator;
import com.hzn.sample.network.ConnectNetworkError;
import com.hzn.sample.network.DataService;
import com.hzn.sample.network.UnauthorizedException;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    public static final String PARAM_FROM_INTERNAL = "com.hzn.sample.activitys.internal";

    @InjectView(R.id.et_username)
    EditText mNameText;
    @InjectView(R.id.et_password)
    EditText mPasswordText;

    private boolean mFromInternal;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFromInternal = getArguments().getBoolean(PARAM_FROM_INTERNAL, false);

        // for test
        mNameText.setText("fengdechuan");
        mPasswordText.setText("12345678");
    }

    @OnClick(R.id.bt_login)
    public void login(View view) {
        String username = mNameText.getText().toString();
        String password = mPasswordText.getText().toString();
        // TODO:验证参数的合理性

        // 请求获取auth
        AppObservable.bindFragment(this,
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String token = DataService.auth(username, password);
                        Log.e(TAG, "debug get token:" + token);
                        subscriber.onNext(token);
                        subscriber.onCompleted();
                    }
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String token) {
                        final AccountManager accountManager = AccountManager.get(getActivity());
                        final Account account = new Account(username, AccountAuthenticator.ACCOUNT_TYPE);
                        accountManager.addAccountExplicitly(account, password, null);
                        accountManager.setAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE, token);

                        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_LONG).show();

                        if (mFromInternal) {
                            Intent intent = new Intent(getActivity(), HomeForLoginTestActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            getActivity().finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ConnectNetworkError) {
                            Toast.makeText(getActivity(), R.string.connect_network_error, Toast.LENGTH_LONG)
                                    .show();
                        } else if (throwable instanceof UnauthorizedException) {
                            Toast.makeText(getActivity(), R.string.username_or_password_is_error,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.expected_happened, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "expected happened.", throwable);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public static LoginFragment newInstance(Bundle args) {
        LoginFragment fragment = new LoginFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }
}
