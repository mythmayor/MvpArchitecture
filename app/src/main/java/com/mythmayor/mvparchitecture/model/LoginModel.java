package com.mythmayor.mvparchitecture.model;

import com.mythmayor.mvparchitecture.contract.LoginContract;
import com.mythmayor.mvparchitecture.request.LoginRequest;
import com.mythmayor.mvparchitecture.response.LoginResponse;
import com.mythmayor.mvparchitecture.utils.net.RetrofitClient;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by mythmayor on 2020/6/30.
 * 登录Model
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<LoginResponse> login(LoginRequest request) {
        return RetrofitClient.getInstance().getApi().login(request.getUsername(), request.getPassword());
    }
}
