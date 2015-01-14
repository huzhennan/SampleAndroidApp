package com.hzn.sample.activitys;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.hzn.sample.R;
import com.hzn.sample.auth.AccountAuthenticator;

/**
 * Created by hzn on 15-1-14.
 */
public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccountAuthenticator.getDefaultAccount(this) == null) {
            LoginActivity.startLogin(this);
            finish();
        }
    }
}
