package com.gxs.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxs.im.R;
import com.gxs.im.utils.StringUtils;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<String> list;

    public ContactAdapter(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final String text = list.get(position);
        holder.mTvUsername.setText(text);
        String initial = StringUtils.getInitial(text);
        holder.mTvSection.setText(initial);
        if (position == 0) {
            holder.mTvSection.setVisibility(View.VISIBLE);
        } else {
            String preContact = list.get(position - 1);
            String preInitial = StringUtils.getInitial(preContact);
            if (preInitial.equals(initial)) {
                holder.mTvSection.setVisibility(View.GONE);
            } else {
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }
        //设置item点击  长按事件
        if(mOnItemClickListener!=null){
            final int layoutPosition = holder.getLayoutPosition();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(text,layoutPosition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(text,layoutPosition);
                    return false;
                }
            });
        }
    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemLongClick(String contact,int position);
        void onItemClick(String contact,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView mTvSection;
        TextView mTvUsername;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTvSection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }
}
