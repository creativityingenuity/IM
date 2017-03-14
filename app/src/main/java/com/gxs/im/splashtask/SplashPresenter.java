package com.gxs.im.splashtask;

import com.hyphenate.chat.EMClient;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;

    @Override
    public void checkLogin() {
        mView.enterMainActivity(EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected());
    }

    @Override
    public void attachView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
