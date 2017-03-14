package com.gxs.im.addfriendtask;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.gxs.im.R;
import com.gxs.im.adapter.AddFriendAdapter;
import com.gxs.im.base.BaseActivity;
import com.gxs.im.model.UserInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddFriendActivity extends BaseActivity implements AddFriendContract.View{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.iv_nodata)
    ImageView ivNodata;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private AddFriendContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);
        mPresenter = new AddFriendPresenter();
        mPresenter.attachView(this);
        initToolBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initToolBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_toolbar_menu, menu);
        //初始化菜单中的searchView
        MenuItem item = menu.findItem(R.id.search);
        final SearchView mSearchView = (SearchView) item.getActionView();
        //设置提示
        mSearchView.setQueryHint("搜索");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)){
                    showToast("请输入内容");
                    return false;
                }
                mPresenter.searchFriend(query);
                //隐藏软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    showToast(newText);
//                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onSearchResult(List<UserInfo> list, List<String> contacts, boolean success, String msg) {
        if(success){
            ivNodata.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            AddFriendAdapter addFriendAdapter = new AddFriendAdapter(list, contacts);
            mRecyclerView.setAdapter(addFriendAdapter);
            addFriendAdapter.setOnAddFriendClickListener(new AddFriendAdapter.OnAddFriendClickListener() {
                @Override
                public void onAddClick(String username) {
                    mPresenter.addFriend(username);
                }
            });
        }else {
            ivNodata.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAddResult(String username, boolean success, String msg) {
        Snackbar.make(mToolbar,"添加"+username+(success?"成功":"失败"+msg),Snackbar.LENGTH_LONG).show();
    }
}
