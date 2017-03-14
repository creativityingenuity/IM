package com.gxs.im.chattask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface ChatContract {
    interface View extends BaseView{

        void onInit(List<EMMessage> mEMMessageList);

        void onUpdate(int size);
    }
    interface Presenter extends BasePresenter<View>{
        void initChat(String username);
        /**
         * 更新数据
         * @param username
         */
        void updateData(String username);

        /**
         * 发送消息
         * @param s
         * @param username
         */
        void sendMessage(String s, String username);
    }
}
