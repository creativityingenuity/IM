package com.gxs.im.splashtask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.gxs.im.MainActivity;
import com.gxs.im.R;
import com.gxs.im.logintask.LoginActivity;
import com.gxs.im.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private static final long DURATION = 3000;
    @Bind(R.id.iv_splash)
    ImageView iv_Splash;
    private SplashContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initPresenter();

    }

    private void initPresenter() {
        mPresenter = new SplashPresenter();
        mPresenter.attachView(this);
        mPresenter.checkLogin();
    }

    /**
     * 1. 判断是否已经登录了
     *
     * 2. 如果登录了，直接进入MainActivity
     *
     * 3. 否则闪屏2秒后（渐变动画），进入LoginActivity
     */
    @Override
    public void enterMainActivity(boolean isLogin) {
        if(isLogin){
            startActivity(MainActivity.class,true);
        }else {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_Splash, "alpha", 1, 0).setDuration(DURATION);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class,true);
                }
            });
        }
    }
}
