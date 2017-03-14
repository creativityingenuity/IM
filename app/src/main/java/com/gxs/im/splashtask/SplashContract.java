package com.gxs.im.splashtask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;

/**
 * Call:vipggxs@163.com
 * Created by YT
 * splash功能合同类
 */

public interface SplashContract {
    interface View extends BaseView{
        void enterMainActivity(boolean b);
    }

    interface Presenter extends BasePresenter<View>{
        void checkLogin();
    }
}
