package com.mythmayor.mvparchitecture.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.textfield.TextInputEditText;
import com.gyf.immersionbar.ImmersionBar;
import com.mythmayor.mvparchitecture.R;
import com.mythmayor.mvparchitecture.base.BaseMvpActivity;
import com.mythmayor.mvparchitecture.contract.LoginContract;
import com.mythmayor.mvparchitecture.presenter.LoginPresenter;
import com.mythmayor.mvparchitecture.receiver.NetworkBroadcastReceiver;
import com.mythmayor.mvparchitecture.request.LoginRequest;
import com.mythmayor.mvparchitecture.response.LoginResponse;
import com.mythmayor.mvparchitecture.utils.IntentUtil;
import com.mythmayor.mvparchitecture.utils.LogUtil;
import com.mythmayor.mvparchitecture.utils.ProgressDlgUtil;
import com.mythmayor.mvparchitecture.utils.ToastUtil;

/**
 * Created by mythmayor on 2020/6/30.
 * 登录页面
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View, NetworkBroadcastReceiver.NetworkListener {

    private TextInputEditText etusername;
    private TextInputEditText etpassword;
    private Button btnlogin;
    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;//网络连接状态监听

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBarMarginTop(R.id.view_blank).init();
        etusername = (TextInputEditText) findViewById(R.id.et_username);
        etpassword = (TextInputEditText) findViewById(R.id.et_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        mNetworkBroadcastReceiver = new NetworkBroadcastReceiver();
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                LogUtil.d(event.name());
            }
        });
    }

    @Override
    protected void initEvent() {
        btnlogin.setOnClickListener(this);
    }

    @Override
    protected void initData(Intent intent) {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        //注册网络连状态监听
        IntentFilter networkFilter = new IntentFilter();
        networkFilter.addAction(NetworkBroadcastReceiver.ACTION_CONNECTIVITY_CHANGE);
        registerReceiver(mNetworkBroadcastReceiver, networkFilter);
        mNetworkBroadcastReceiver.addNetworkListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String username = getUsername();
                String password = getPassword();
                LogUtil.i("username=" + username + ", password=" + password);
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(this, "请输入账号和密码");
                    return;
                }
                LoginRequest request = new LoginRequest(username, password);
                mPresenter.login(request);
                break;
            default:
                break;
        }
    }

    //获取账号
    private String getUsername() {
        return etusername.getText().toString().trim();
    }

    //获取密码
    private String getPassword() {
        return etpassword.getText().toString().trim();
    }

    @Override
    public void onSuccess(LoginResponse resp) {
        if (resp.getErrorCode() == 0) {//登录成功
            ToastUtil.showToast(this, "登录成功: " + resp.getData().getUsername());
            IntentUtil.startActivity(this, MainActivity.class);
            finish();
        } else {//登录失败
            ToastUtil.showToast(this, resp.getErrorMsg());
        }
    }

    @Override
    public void showLoading() {
        ProgressDlgUtil.show(this, "正在登录，请稍后...");
    }

    @Override
    public void hideLoading() {
        ProgressDlgUtil.dismiss();
    }

    @Override
    public void onError(String errMessage) {
        ToastUtil.showToast(this, errMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkBroadcastReceiver.removeNetworkListener(this);
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
