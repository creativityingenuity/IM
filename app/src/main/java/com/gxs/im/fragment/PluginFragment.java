package com.gxs.im.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gxs.im.MainActivity;
import com.gxs.im.R;
import com.gxs.im.base.BaseFragment;
import com.gxs.im.logintask.LoginActivity;
import com.gxs.im.utils.CallBackListener;
import com.gxs.im.utils.ToastUtils;
import com.hyphenate.chat.EMClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment {
    @Bind(R.id.btn_logout)
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        //点击退出当前帐号
        EMClient.getInstance().logout(true, new CallBackListener() {
            @Override
            public void onMainSuccess() {
                ((MainActivity) getActivity()).startActivity(LoginActivity.class,true);
            }

            @Override
            public void onMainError(int i, String s) {
                ToastUtils.showToast(getContext(),s);
            }
        });
    }
}
