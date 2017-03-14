package com.gxs.im.registertask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface RegisterContract {
    interface View extends BaseView{

        void onRegist(String username, String pwd, boolean b, String s);
    }
    interface Presenter extends BasePresenter<View>{
        void register(String name,String pwd);
    }
}
