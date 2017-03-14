package com.gxs.im.fragment.contacttask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface ContactContract {
    interface View extends BaseView{

        void onInitContacts(List<String> mContactList);

        void updateContacts(boolean success, String  msg);

        void onDelete(String contact, boolean success, String msg);
    }
    interface Presenter extends BasePresenter<View>{
        /**
         * 初始化联系人
         */
        void initContact();

        /**
         * 更新联系人
         */
        void updateContacts();

        /**
         * 删除联系人
         * @param contact
         */
        void deleteContact(String contact);
         List<String> getList();
    }
}
