package com.mythmayor.mvparchitecture.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.mythmayor.mvparchitecture.R;
import com.mythmayor.mvparchitecture.base.BaseActivity;
import com.mythmayor.mvparchitecture.base.BaseDialog;
import com.mythmayor.mvparchitecture.receiver.NetworkBroadcastReceiver;
import com.mythmayor.mvparchitecture.ui.dialog.LogoutDialog;
import com.mythmayor.mvparchitecture.utils.IntentUtil;
import com.mythmayor.mvparchitecture.utils.LogUtil;
import com.mythmayor.mvparchitecture.utils.PrefUtil;

/**
 * Created by mythmayor on 2020/6/30.
 * 主页面
 */
public class MainActivity extends BaseActivity implements NetworkBroadcastReceiver.NetworkListener {

    private TextView tvlogout;
    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;//网络连接状态监听

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).statusBarColor(R.color.color_white).fitsSystemWindows(true).init();
        tvlogout = (TextView) findViewById(R.id.tv_logout);
        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver();
    }

    @Override
    protected void initEvent() {
        tvlogout.setOnClickListener(this);
    }

    @Override
    protected void initData(Intent intent) {
        PrefUtil.putBoolean(this, PrefUtil.IS_USER_LOGIN, true);
        //注册网络连状态监听
        IntentFilter networkFilter = new IntentFilter();
        networkFilter.addAction(NetworkBroadcastReceiver.ACTION_CONNECTIVITY_CHANGE);
        registerReceiver(mNetworkBroadcastReceiver, networkFilter);
        mNetworkBroadcastReceiver.addNetworkListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                showLogoutDialog();
                break;
            default:
                break;
        }
    }

    private void showLogoutDialog() {
        String content = "确定要退出登录么？";
        final LogoutDialog dialog = new LogoutDialog(this, content, "取消", "确定");
        dialog.setNoOnclickListener(new BaseDialog.onNoOnclickListener() {
            @Override
            public void onNoClick(Object o) {
                dialog.dismiss();
            }
        });
        dialog.setYesOnclickListener(new BaseDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(Object o) {
                dialog.dismiss();
                PrefUtil.clear(MainActivity.this);
                IntentUtil.startActivityClearTask(MainActivity.this, LoginActivity.class);
            }
        });
        dialog.show();
        //ProjectUtil.setDialogWindowAttr(dialog);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitApp();
    }

    private void exitApp() {
        mNetworkBroadcastReceiver.removeNetworkListener(this);
        if (mNetworkBroadcastReceiver != null) {
            unregisterReceiver(mNetworkBroadcastReceiver);
        }
    }

    @Override
    public void onNetworkListener(int status) {
        if (status == NetworkBroadcastReceiver.NETWORK_NONE) {//无网络连接
            LogUtil.d("无网络连接");
        } else if (status == NetworkBroadcastReceiver.NETWORK_MOBILE) {//移动网络连接
            LogUtil.d("移动网络连接");
        } else if (status == NetworkBroadcastReceiver.NETWORK_WIFI) {//无线网络连接
            LogUtil.d("无线网络连接");
        }
    }
}
