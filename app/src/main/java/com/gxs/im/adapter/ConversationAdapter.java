package com.gxs.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxs.im.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
    private List<EMConversation> emConversations;
    public ConversationAdapter(List<EMConversation> emConversations) {
        this.emConversations = emConversations;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        final EMConversation emConversation = emConversations.get(position);
        //liao tian de dui fang ming cheng
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        EMMessage lastMessage = emConversation.getLastMessage();
        long msgTime = lastMessage.getMsgTime();
        EMTextMessageBody lastMessageBody = (EMTextMessageBody) lastMessage.getBody();
        String lastMessageBodyMessage = lastMessageBody.getMessage();

        holder.mTvMsg.setText(lastMessageBodyMessage);
        holder.mTvUsername.setText(emConversation.getUserName());
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if(unreadMsgCount>99){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText("99+");
        }else if(unreadMsgCount>0){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText(unreadMsgCount+"");
        }else {
            holder.mTvUnread.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(emConversation);
                }
            }
        });
    }
    public interface OnItemClickListener{
        void onItemClick(EMConversation conversation);
    }
    private OnItemClickListener mOnItemClickListener;
    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return emConversations==null?0:emConversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{

        TextView mTvUsername;
        TextView mTvTime;
        TextView mTvMsg;
        TextView mTvUnread;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }
    }
}
