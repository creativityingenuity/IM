package com.gxs.im.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxs.im.MainActivity;
import com.gxs.im.R;
import com.gxs.im.chattask.ChatActivity;
import com.gxs.im.adapter.ContactAdapter;
import com.gxs.im.base.BaseFragment;
import com.gxs.im.fragment.contacttask.ContactContract;
import com.gxs.im.fragment.contacttask.ContactPresenter;
import com.gxs.im.model.ContactUpdateEvent;
import com.gxs.im.utils.ToastUtils;
import com.gxs.im.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactContract.View {
    @Bind(R.id.contactLayout)
    ContactLayout mContactLayout;
    ContactContract.Presenter mPresenter;
    private ContactAdapter mContactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new ContactPresenter();
        mPresenter.attachView(this);
        mPresenter.initContact();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }

    @Override
    public void onInitContacts(List<String> mContactList) {
        mContactAdapter = new ContactAdapter(mContactList);
        mContactLayout.setRecyclerView(mContactAdapter);
        mContactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(final String contact, int position) {
                Snackbar.make(mContactLayout, "您和" + contact + "确定友尽了吗？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.deleteContact(contact);
                            }
                        }).show();
            }

            @Override
            public void onItemClick(String contact, int position) {
                MainActivity activity = (MainActivity) getActivity();
                activity.startActivity(ChatActivity.class,false,contact);
            }
        });
      /*  //设置mrc滑动删除
        ItemTouchHelper helper = new ItemTouchHelper(new MyCallBack());
        RecyclerView mRecyclerView = mContactLayout.getRecyclerView();
        helper.attachToRecyclerView(mRecyclerView);*/
    }

    /*public class MyCallBack extends ItemTouchHelper.Callback {
        *//**
         * 设置滑动或者拖拽的方向
         * drag - 表示拖拽的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
         * swipe - 表示滑动的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
         * 如果为0，则不执行拖动或者滑动
         *
         * @param recyclerView
         * @param viewHolder
         * @return
         *//*
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0, swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                //网格式布局有4个方向
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                //线性式布局有2个方向
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; //设置侧滑方向为从两个方向都可以
            }
            //makeMovementFlags./.
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        *//**
         * item拖拽时调用的方法
         *
         * @param recyclerView
         * @param viewHolder   拖动的ViewHolder
         * @param target       目标位置的ViewHolder
         * @return
         *//*
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        *//**
         * item滑动时调用的方法
         *
         * @param viewHolder 滑动的ViewHolder
         * @param direction  滑动的方向
         *//*
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            final String contact = mPresenter.getList().get(position);
            if (TextUtils.isEmpty(contact)) {
                Snackbar.make(mContactLayout, "您和" + contact + "确定友尽了吗？", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.deleteContact(contact);
                            }
                        }).show();
            }
        }

        *//**
         * 当item绘制的时候调用的方法
         *
         * @param c
         * @param recyclerView
         * @param viewHolder
         * @param dX
         * @param dY
         * @param actionState       三种状态SWIPE（滑动）、IDLE（静止）、DRAG（拖动）
         * @param isCurrentlyActive
         *//*
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }

        //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }

        //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

        }

        *//**
         * 是否可以长按拖拽排序
         *
         * @return
         *//*
        @Override
        public boolean isLongPressDragEnabled() {
            return super.isLongPressDragEnabled();
        }

        *//**
         * Item是否可以被滑动(H：左右滑动，V：上下滑动)
         *//*
        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }
    }*/

    @Override
    public void updateContacts(boolean success, String msg) {
        mContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(String contact, boolean success, String msg) {
        if (!success) {
            ToastUtils.showToast(getActivity(), "删除失败，要不再续前缘？");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnContactUpdateEvent(ContactUpdateEvent event) {
        mPresenter.updateContacts();
    }
}
