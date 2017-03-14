package com.gxs.im.logintask;

import com.gxs.im.utils.CallBackListener;
import com.hyphenate.chat.EMClient;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;

    @Override
    public void attachView(LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void login(final String username, final String pwd) {
        //登录
        EMClient.getInstance().login(username, pwd, new CallBackListener() {
            @Override
            public void onMainSuccess() {
                mView.onLogin(username,pwd,true,null);
            }

            @Override
            public void onMainError(int i, String s) {
                mView.onLogin(username,pwd,false,s);
            }
        });
    }
}
