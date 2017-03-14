package com.gxs.im.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxs.im.R;
import com.gxs.im.adapter.ContactAdapter;
import com.gxs.im.utils.StringUtils;

import java.util.List;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ContactLayout extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private TextView mtv_float;
    private QuickIndexBar mSlideBar;
    private LinearLayoutManager manager;

    public ContactLayout(Context context) {
        super(context);
        init(context);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.contactlayout,null);
        addView(inflate);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_contract);
        mtv_float = (TextView) inflate.findViewById(R.id.tv_float);
        mSlideBar = (QuickIndexBar) inflate.findViewById(R.id.slideBar);
        //字母改变监听
        mSlideBar.setOnTouchLetterChangeListener(new QuickIndexBar.OnTouchLetterChangeListner() {
            @Override
            public void onLetterChange(String letter) {
                mtv_float.setVisibility(VISIBLE);
                mtv_float.setText(letter);
                List<String> list = ((ContactAdapter) mRecyclerView.getAdapter()).getList();
                for (int i = 0; i < list.size(); i++) {
                    String initial = StringUtils.getInitial(list.get(i));
                    if(initial.equalsIgnoreCase(letter)){
                        moveToPosition(i);
                    }
                }
            }

            @Override
            public void onTouchUp() {
                mtv_float.setVisibility(GONE);
            }
        });
    }
    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
        }
    }
    /**
     * 设置RecyclerVIew
     */
    public void setRecyclerView(RecyclerView.Adapter adapter){
        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
