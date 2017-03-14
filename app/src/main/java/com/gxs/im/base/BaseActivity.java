package com.gxs.im.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gxs.im.IMApplication;
import com.gxs.im.R;
import com.gxs.im.utils.ToastUtils;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private IMApplication imApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        imApplication = (IMApplication) getApplication();
        imApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imApplication.removeActivity(this);
    }

    public void saveUser(String username, String pwd){
        mSharedPreferences.edit()
                .putString("username",username)
                .putString("pwd",pwd)
                .commit();
    }

    public String getUserName(){
        return mSharedPreferences.getString("username","");
    }
    public String getPwd(){
        return mSharedPreferences.getString("pwd","");
    }
    public void showDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    public void startActivity(Class clazz, boolean isFinish) {
        startActivity(clazz,isFinish,null);
    }
    public void startActivity(Class clazz, boolean isFinish, String contact) {
        Intent intent = new Intent(this,clazz);
        if (contact!=null){
            intent.putExtra("username",contact);
        }
        startActivity(intent);
        if (isFinish){
            finish();
        }
        overridePendingTransition(R.anim.anim_activity_next_enter, R.anim.anim_activity_next_out);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_activity_pre_enter, R.anim.anim_activity_pre_out);
    }
    public void showToast(String msg){
        ToastUtils.showToast(this,msg);
    }
}
