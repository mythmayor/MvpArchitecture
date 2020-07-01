package com.mythmayor.mvparchitecture.presenter;

import com.mythmayor.mvparchitecture.base.BasePresenter;
import com.mythmayor.mvparchitecture.contract.LoginContract;
import com.mythmayor.mvparchitecture.itype.NetCallback;
import com.mythmayor.mvparchitecture.model.LoginModel;
import com.mythmayor.mvparchitecture.request.LoginRequest;
import com.mythmayor.mvparchitecture.response.LoginResponse;
import com.mythmayor.mvparchitecture.utils.LogUtil;
import com.mythmayor.mvparchitecture.utils.net.NetUtil;
import com.mythmayor.mvparchitecture.utils.net.RxScheduler;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.Call;

/**
 * Created by mythmayor on 2020/6/30.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginContract.Model mModel;

    public LoginPresenter() {
        mModel = new LoginModel();
    }

    @Override
    public void login(LoginRequest request) {
        //View是否绑定，如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        useRetrofit(request);
        //useOkHttpUtils(request);
    }

    private void useRetrofit(LoginRequest request) {
        mModel.login(request)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new Observer<LoginResponse>() {//这里需要在build.gradle中指定jdk版本，否则会报错
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull LoginResponse bean) {
                        mView.onSuccess(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    private void useOkHttpUtils(LoginRequest request) {
        mView.showLoading();
        NetUtil.login2(request, new NetCallback() {
            @Override
            public void onSuccess(String response, int id) {
                mView.hideLoading();
                LogUtil.i(response);
                LoginResponse resp = NetUtil.mGson.fromJson(response, LoginResponse.class);
                mView.onSuccess(resp);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                mView.hideLoading();
                mView.onError(e.getMessage());
            }
        });
    }
}
