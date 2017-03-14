package com.gxs.im.fragment.conversationtask;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;
    private List<EMConversation> emConversations = new ArrayList<>();

    @Override
    public void attachView(ConversationContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void initConversation() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();

        emConversations.clear();
        emConversations.addAll(allConversations.values());
        //pai xu  zuijin de shi jian  zai zui shang mian
        Collections.sort(emConversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
            }
        });
        mView.onInitConversation(emConversations);
    }
}
