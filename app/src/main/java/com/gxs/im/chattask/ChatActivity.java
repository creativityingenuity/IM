package com.gxs.im.chattask;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxs.im.R;
import com.gxs.im.adapter.ChatAdapter;
import com.gxs.im.base.BaseActivity;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements TextWatcher, ChatContract.View {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.et_msg)
    EditText etMsg;
    @Bind(R.id.btn_send)
    Button btnSend;
    private ChatContract.Presenter mPresenter;
    private String username;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回键

        mPresenter = new ChatPresenter();
        mPresenter.attachView(this);

        username = getIntent().getStringExtra("username");
        if(TextUtils.isEmpty(username)){
            finish();
        }
        tvTitle.setText("与"+ username +"聊天中");
        //设置文本输入监听
        etMsg.addTextChangedListener(this);
        String msg = etMsg.getText().toString().trim();
        if(!TextUtils.isEmpty(msg)){
            btnSend.setEnabled(true);
        }
        /**
         * 获取最近的20条聊天记录，然后显示
         */
        mPresenter.initChat(username);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //判断这个消息是不是正在聊天的用户发的
        if (emMessage.getFrom().equals(username)) {
            mPresenter.updateData(username);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        String msg = etMsg.getText().toString();
        mPresenter.sendMessage(username,msg);
        etMsg.getText().clear();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().length()==0){
            btnSend.setEnabled(false);
        }else {
            btnSend.setEnabled(true);
        }
    }

    @Override
    public void onInit(List<EMMessage> mEMMessageList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(mEMMessageList);
        mRecyclerView.setAdapter(chatAdapter);
        if(mEMMessageList.size()!=0){
            mRecyclerView.scrollToPosition(mEMMessageList.size()-1);
        }
    }

    @Override
    public void onUpdate(int size) {
        chatAdapter.notifyDataSetChanged();
        if(size!=0){
            mRecyclerView.smoothScrollToPosition(size-1);
        }
    }
}
