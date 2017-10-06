package com.ygip.ipbase_android.mvp.mine.view;

import android.app.Activity;
import android.os.Bundle;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.util.StartActivityUtil;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.gui.RegisterPage;

public class ChangePasswordActivity extends XActivity<MinePresenter> implements MineCommon {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public Activity getActivity() {
        return context;
    }

    @Override
    public void startActivity(Class clazz) {
        StartActivityUtil.start(context,clazz);
    }

    @Override
    public MinePresenter newP() {
        return new MinePresenter();
    }


}
