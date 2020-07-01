package com.mythmayor.mvparchitecture.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.mythmayor.mvparchitecture.R;
import com.mythmayor.mvparchitecture.base.BaseActivity;
import com.mythmayor.mvparchitecture.utils.IntentUtil;
import com.mythmayor.mvparchitecture.utils.PrefUtil;

/**
 * Created by mythmayor on 2020/6/30.
 * 启动页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isUserLogin = PrefUtil.getBoolean(this, PrefUtil.IS_USER_LOGIN, false);
        if (isUserLogin) {
            IntentUtil.startActivity(this, MainActivity.class);
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    IntentUtil.startActivity(SplashActivity.this, LoginActivity.class);
                    overridePendingTransition(R.anim.anim_static, R.anim.anim_static);
                    finish();
                }
            }, 1500);
        }
    }
}
