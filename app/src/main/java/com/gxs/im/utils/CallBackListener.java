package com.gxs.im.utils;

import com.hyphenate.EMCallBack;


public abstract class CallBackListener implements EMCallBack {

    public  abstract void onMainSuccess();

    public abstract void onMainError(int i, String s);

    @Override
    public void onSuccess() {
        ThreadPoolUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadPoolUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainError(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {
    }
}
