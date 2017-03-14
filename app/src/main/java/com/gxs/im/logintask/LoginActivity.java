package com.gxs.im.logintask;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxs.im.MainActivity;
import com.gxs.im.R;
import com.gxs.im.base.BaseActivity;
import com.gxs.im.registertask.RegisterActivity;
import com.gxs.im.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener, LoginContract.View {

    private static final int REQUEST_SDCARD = 1;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.tll_username)
    TextInputLayout tllUsername;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.til_pwd)
    TextInputLayout tilPwd;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.tv_newuser)
    TextView tvNewuser;
    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT); 设置底部栏颜色
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        etUsername.setText(getUserName());
        etPwd.setText(getPwd());
        etPwd.setOnEditorActionListener(this);
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        etUsername.setText(getUserName());
        etPwd.setText(getPwd());
    }

    @OnClick({R.id.bt_login, R.id.tv_newuser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_newuser:
                //打开注册页面
                startActivity(RegisterActivity.class,false);
                break;
        }
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (checkInPut(username, pwd)) {
            /**
             *  2.先注册bmob
             *      如果注册成功，再去注册环信
             *      如果bmob注册成功， 环信注册失败，则需要再把bmob上的数据删除掉
             */
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_SDCARD);
                return;
            }
            showDialog("正在登录...");
            mPresenter.login(username,pwd);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_SDCARD){
            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                //被授权了
                login();
            }
        }
    }

    private boolean checkInPut(String userName, String pwd) {
        if (!StringUtils.checkUserName(userName)) {
            tllUsername.setErrorEnabled(true);
            tilPwd.setError("用户名不合法");
            etUsername.requestFocus(View.FOCUS_RIGHT);
            return false;
        }
        tllUsername.setErrorEnabled(false);

        if (!StringUtils.checkPwd(pwd)) {
            tilPwd.setErrorEnabled(true);
            tilPwd.setError("密码不合法");
            etPwd.requestFocus(View.FOCUS_RIGHT);
            return false;
        }
        tilPwd.setErrorEnabled(false);
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getId()==R.id.et_pwd){
            if(actionId== EditorInfo.IME_ACTION_DONE){
                login();
            }
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onLogin(String username, String pwd, boolean success, String msg) {
        hideDialog();
        if(success){
            /**
             * 1.保存用户
             * 2. 跳转到主界面
             */
            saveUser(username, pwd);
            startActivity(MainActivity.class,true);
        }else {
            /**
             * 1.Toast
             */
            showToast("登录失败了："+msg);
        }
    }
}
