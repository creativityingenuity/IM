package com.gxs.im.fragment.contacttask;

import com.gxs.im.db.DBUtils;
import com.gxs.im.utils.ThreadPoolUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.gxs.im.utils.ThreadPoolUtils.runOnSubThread;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ContactPresenter implements ContactContract.Presenter {
    private  ContactContract.View mView;
    private List<String> mContactList = new ArrayList<>();
    @Override
    public void attachView(ContactContract.View view) {
       this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
    public List<String> getList(){
        return mContactList;
    }
    @Override
    public void initContact() {
        //1. 首先访问本地的缓存联系人
        String currentUser = EMClient.getInstance().getCurrentUser();
        List<String> contacts = DBUtils.getContacts(currentUser);
        mContactList.clear();
        mContactList.addAll(contacts);
        mView.onInitContacts(mContactList);
        //2. 然后开辟子线程去环信后台获取当前用户的联系人
        updateContactFromServer(currentUser);
    }

    @Override
    public void updateContacts() {
        updateContactFromServer(EMClient.getInstance().getCurrentUser());
    }

    @Override
    public void deleteContact(final String contact) {
         ThreadPoolUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    afterDelete(contact, true,null);
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    afterDelete(contact,false,e.toString());
                }

            }
        });
    }
    private void afterDelete(final String contact, final boolean success, final String msg) {
        ThreadPoolUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mView.onDelete(contact, success, msg);
            }
        });
    }
    private void updateContactFromServer(final String currentUser) {
        runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //排序
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //3. 更新本地的缓存，刷新UI
                    DBUtils.updateContacts(currentUser,allContactsFromServer);
                    mContactList.clear();
                    mContactList.addAll(allContactsFromServer);
                    ThreadPoolUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateContacts(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadPoolUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateContacts(false,e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
