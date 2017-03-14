package com.gxs.im.adapter;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

public class MessageListenerAdapter implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> list) {
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
    }
}
