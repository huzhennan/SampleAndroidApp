package com.hzn.sample.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hzn.sample.fragments.LoginFragment;

/**
 * Created by hzn on 15-1-14.
 */
public class LoginActivity extends SingleFragmentActivity {
    public static final String PARAM_FROM_INTERNAL = "com.hzn.sample.activitys.internal";

    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance(getIntent().getExtras());
    }

    public static void startLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(PARAM_FROM_INTERNAL, true);
        context.startActivity(intent);
    }
}
