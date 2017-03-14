package com.gxs.im.fragment.conversationtask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface ConversationContract {
    interface View extends BaseView{

        void onInitConversation(List<EMConversation> emConversations);
    }
    interface Presenter extends BasePresenter<View>{
        /**
         * 初始化回话界面
         */
        void initConversation();
    }
}
