package com.gxs.im.fragment.conversationtask;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvishew.xlog.XLog;
import com.gxs.im.MainActivity;
import com.gxs.im.R;
import com.gxs.im.adapter.ConversationAdapter;
import com.gxs.im.base.BaseFragment;
import com.gxs.im.chattask.ChatActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationContract.View, ConversationAdapter.OnItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ConversationContract.Presenter mPresenter;
    private ConversationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        mPresenter.initConversation();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        mPresenter.detachView();
        adapter = null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
   public void onMsgReceiverEvent(EMMessage emMessage){
        mPresenter.initConversation();
    }

    @Override
    public void onInitConversation(List<EMConversation> emConversations) {
        if(adapter==null){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ConversationAdapter(emConversations);
            adapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(adapter);
        }else {
            XLog.i("notifyDataSetChanged==============");
            adapter.notifyDataSetChanged();
        }

    }
    //i条目点击
    @Override
    public void onItemClick(EMConversation conversation) {
        String userName = conversation.getUserName();
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(ChatActivity.class,false,userName);
    }
    //
    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
