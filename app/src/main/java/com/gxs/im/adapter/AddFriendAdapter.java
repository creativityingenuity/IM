package com.gxs.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gxs.im.R;
import com.gxs.im.model.UserInfo;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddViewHolder> {
    List<UserInfo> list;
    List<String> contacts;

    public AddFriendAdapter(List<UserInfo> list, List<String> contacts) {
        this.list = list;
        this.contacts = contacts;
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(AddViewHolder holder, int position) {
        UserInfo user = list.get(position);
        final String username = user.getUsername();
        holder.mTvUsername.setText(username);
        holder.mTvTime.setText(user.getCreatedAt());
        //判断当前username是不是已经在好友列表中了
        if (!contacts.contains(username)) {
            holder.mBtnAdd.setText("添加");
            holder.mBtnAdd.setEnabled(true);
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddFriendClickListener != null) {
                    mOnAddFriendClickListener.onAddClick(username);
                }
            }
        });
    }

    public interface OnAddFriendClickListener {
        void onAddClick(String username);
    }

    private OnAddFriendClickListener mOnAddFriendClickListener;

    public void setOnAddFriendClickListener(OnAddFriendClickListener addFriendClickListener) {
        this.mOnAddFriendClickListener = addFriendClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUsername;
        TextView mTvTime;
        Button mBtnAdd;

        public AddViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
