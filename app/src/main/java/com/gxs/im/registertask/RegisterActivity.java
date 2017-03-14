package com.gxs.im.registertask;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxs.im.R;
import com.gxs.im.logintask.LoginActivity;
import com.gxs.im.base.BaseActivity;
import com.gxs.im.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterContract.View, TextView.OnEditorActionListener {

    @Bind(R.id.et_username_register)
    EditText etUsername;
    @Bind(R.id.til_username_register)
    TextInputLayout tilUsername;
    @Bind(R.id.et_pwd_register)
    EditText etPwd;
    @Bind(R.id.til_pwd_register)
    TextInputLayout tilPwd;
    @Bind(R.id.btn_register)
    Button btnRegister;
    private RegisterContract.Presenter mPresenter;

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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        etPwd.setOnEditorActionListener(this);
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
    }


    @OnClick(R.id.btn_register)
    public void onClick() {
        regist();
    }

    private void regist() {
        String pwd = etPwd.getText().toString().trim();
        String userName = etUsername.getText().toString().trim();
        //1.校验
        if (checkInPut(userName, pwd)) {
            /**
             *  2.先注册bmob
             *      如果注册成功，再去注册环信
             *      如果bmob注册成功， 环信注册失败，则需要再把bmob上的数据删除掉
             */
            showDialog("正在注册...");
            mPresenter.register(userName, pwd);
        }
    }

    /**
     * 校验输入内容
     *
     * @param userName
     * @param pwd
     */
    private boolean checkInPut(String userName, String pwd) {
        if (!StringUtils.checkUserName(userName)) {
            tilUsername.setErrorEnabled(true);
            tilPwd.setError("用户名不合法");
            etUsername.requestFocus(View.FOCUS_RIGHT);
            return false;
        }
        tilUsername.setErrorEnabled(false);

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
        if (v.getId() == R.id.et_pwd && actionId == EditorInfo.IME_ACTION_DONE) {
            regist();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
   }

    @Override
    public void onRegist(String username, String pwd, boolean isSuccess, String s) {
        hideDialog();
        if (isSuccess){
            /**
             * 将注册成功的数据保存到本地
             *
             * 跳转到登录界面
             */
            saveUser(username, pwd);
            showToast("注册成功");
            startActivity(LoginActivity.class,true);
        }else {
            /**
             * 弹吐司，告诉用户失败了
             */
            showToast("注册失败："+s);
        }
    }
}
