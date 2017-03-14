package com.gxs.im;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.gxs.im.addfriendtask.AddFriendActivity;
import com.gxs.im.base.BaseActivity;
import com.gxs.im.base.BaseFragment;
import com.gxs.im.fragment.FragmentFactory;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.gxs.im.R.id.toolbar;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    //toolbar 标题
    private int[] titleIds = {R.string.conversation, R.string.contact, R.string.plugin};
    private BadgeItem mBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initBottomNavigation();
        //Activity异常终止 Fragment显示异常 解决
        if (savedInstanceState == null) {
            initFirstFragment();
        }
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgReceiverMainEvent(EMMessage emMessage){
        updateConversation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateConversation();
    }

    private void updateConversation() {
        //获取所有的未读消息
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount>99){
            mBadgeItem.setText("99+");
            mBadgeItem.show(true);
        }else if (unreadMsgsCount>0){
            mBadgeItem.setText(unreadMsgsCount+"");
            mBadgeItem.show(true);
        }else{
            mBadgeItem.hide(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initFirstFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, FragmentFactory.getFragment(0), "0").commit();
    }

    private void initBottomNavigation() {
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
        mBadgeItem = new BadgeItem();
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.setBackgroundColor("#ff0000");
        mBadgeItem.setText("5");
        mBadgeItem.show();
        conversationItem.setBadgeItem(mBadgeItem);

        mBottomNavigationBar.addItem(conversationItem);

        BottomNavigationItem contactItem = new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人");
        mBottomNavigationBar.addItem(contactItem);

        BottomNavigationItem pluginItem = new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态");
        mBottomNavigationBar.addItem(pluginItem);

        mBottomNavigationBar.setActiveColor(R.color.btn_normal);
        mBottomNavigationBar.setInActiveColor(R.color.inActive);

        mBottomNavigationBar.initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.cela);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tvTitle.setText(titleIds[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
                startActivity(AddFriendActivity.class, false);
                break;
            case R.id.menu_scan:
                showToast("分享好友");
                break;
            case R.id.menu_about:
                showToast("关于我们");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        tvTitle.setText(titleIds[position]);
        BaseFragment fragment = FragmentFactory.getFragment(position);
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_content, fragment, "" + position);
        }
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
    }

    @Override
    public void onTabReselected(int position) {

    }
}
