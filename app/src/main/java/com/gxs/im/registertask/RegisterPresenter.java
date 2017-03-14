package com.gxs.im.registertask;

import com.gxs.im.model.UserInfo;
import com.gxs.im.utils.ThreadPoolUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View mView;

    /**
     * 2.先注册bmob
     * 如果注册成功，再去注册环信
     * 如果bmob注册成功， 环信注册失败，则需要再把bmob上的数据删除掉
     */
    @Override
    public void register(final String name, final String pwd) {
        final BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.signUp(new SaveListener<UserInfo>() {
            //Bmob中的回调方法都是在主线程中被调用的
            @Override
            public void done(UserInfo o, BmobException e) {
                if (e == null) {
                    //bmob注册成功，再去注册环信
                    //注册失败会抛出HyphenateException
                    ThreadPoolUtils poolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 0);
                    poolUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(name, pwd);//同步方法
                                ThreadPoolUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onRegist(name, pwd, true, null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                //将Bmob上注册的user给删除掉
                                user.delete();
                                //环信注册失败了
                                ThreadPoolUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onRegist(name, pwd, false, e1.toString());
                                    }
                                });
                            }
                        }
                    });
                } else {
                    //失败了，将结果告诉Activity
                    mView.onRegist(name, pwd, false, e.getMessage());
                }
            }
        });
    }

    @Override
    public void attachView(RegisterContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
