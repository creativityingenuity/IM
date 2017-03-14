package com.gxs.im.addfriendtask;

import com.gxs.im.base.BasePresenter;
import com.gxs.im.base.BaseView;
import com.gxs.im.model.UserInfo;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface AddFriendContract {
    interface View extends BaseView{

        void onSearchResult(List<UserInfo> list, List<String> contacts, boolean success, String msg);

        void onAddResult(String username, boolean success, String msg);
    }
    interface Presenter extends BasePresenter<View>{
        /**
         * 搜索指定好友
         * @param query
         */
        void searchFriend(String query);

        void addFriend(String username);
    }
}
