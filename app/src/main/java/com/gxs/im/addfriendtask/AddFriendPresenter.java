package com.gxs.im.addfriendtask;

import com.gxs.im.db.DBUtils;
import com.gxs.im.model.UserInfo;
import com.gxs.im.utils.ThreadPoolUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class AddFriendPresenter implements AddFriendContract.Presenter {
    private AddFriendContract.View mView;

    @Override
    public void attachView(AddFriendContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void searchFriend(String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        BmobQuery<UserInfo> query = new BmobQuery();
        query.addWhereStartsWith("username", keyword)
                .addWhereNotEqualTo("username", currentUser)
                .findObjects(new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null && list != null && list.size() > 0) {
                            List<String> contacts = DBUtils.getContacts(currentUser);
                            //获取到数据
                            mView.onSearchResult(list, contacts, true, null);
                        } else {
                            //没有找到数据
                            if (e == null) {
                                mView.onSearchResult(null, null, false, "没有找到对应的用户。");
                            } else {
                                mView.onSearchResult(null, null, false, e.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void addFriend(final String username) {
        ThreadPoolUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"想加您为好友，一起写代码！");
                    onAddResult(username,true,null);
                } catch (HyphenateException e) {
                    //添加失败
                    e.printStackTrace();
                    onAddResult(username,false,e.getMessage());

                }
            }
        });
    }
    private void onAddResult(final String username, final boolean success, final String msg) {
        ThreadPoolUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mView.onAddResult(username, success, msg);
            }
        });
    }
}
