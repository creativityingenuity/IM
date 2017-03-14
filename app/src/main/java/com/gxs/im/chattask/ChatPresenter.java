package com.gxs.im.chattask;

import com.gxs.im.utils.CallBackListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();

    @Override
    public void attachView(ChatContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void initChat(String username) {
        /**
         * 如果曾经与此用户聊天过，那么获取最近的20条记录
         * 如果没有聊天过，返回一个空集合
         */
        updataChatData(username);
        mView.onInit(mEMMessageList);
    }

    @Override
    public void updateData(String username) {
        updataChatData(username);
        mView.onUpdate(mEMMessageList.size());
    }

    @Override
    public void sendMessage(String username,String msg) {
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg, username);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);
        mView.onUpdate(mEMMessageList.size());
        emMessage.setMessageStatusCallback(new CallBackListener() {
            @Override
            public void onMainSuccess() {
                mView.onUpdate(mEMMessageList.size());
            }

            @Override
            public void onMainError(int i, String s) {
                mView.onUpdate(mEMMessageList.size());
            }
        });
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void updataChatData(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation != null) {
            //需要将所有的未读消息标记为已读
            conversation.markAllMessagesAsRead();
            //曾经聊过
            EMMessage lastMessage = conversation.getLastMessage();
            int count = 19;
            if(mEMMessageList.size()>=19){
                count = mEMMessageList.size();
            }
            //获取最后一条消息之前的19条
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            Collections.reverse(emMessages);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(emMessages);
            Collections.reverse(mEMMessageList);
        } else {
            mEMMessageList.clear();
        }
    }
}
