package com.gxs.im.logintask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface LoginContract {
    interface View extends BaseView{

        void onLogin(String username, String pwd, boolean success, String msg);
    }
    interface Presenter extends BasePresenter<View>{
        /**
         * deng lu
         * @param username
         * @param pwd
         */
        void login(String username, String pwd);
    }
}
