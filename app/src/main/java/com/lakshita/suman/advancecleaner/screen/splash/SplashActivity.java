package com.lakshita.suman.advancecleaner.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.lakshita.suman.advancecleaner.CleanMasterApp;
import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.screen.BaseActivity;
import com.lakshita.suman.advancecleaner.screen.main.MainActivity;
import com.security.applock.utils.PreferencesHelper;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_1);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //  YoYo.with(Techniques.SlideInUp).duration(2000).playOn(tvContent);

        CleanMasterApp cleanMasterApp = (CleanMasterApp) this.getApplication();
        cleanMasterApp.cleanMasterConfig.getRemoteConfig(this);
        cleanMasterApp.cleanMasterConfig.OnSplashListener(() -> {
            handlerScreen();
        });
        String language = PreferencesHelper.getString(PreferencesHelper.KEY_LANGUAGE);
    }

    private void handlerScreen() {
        Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
        finish();
    }
}
