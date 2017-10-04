package com.ygip.ipbase_android.mvp.mine.presenter;

import android.app.Activity;

import com.ygip.ipbase_android.mvp.login.present.LoginPresent;

import cn.droidlover.xdroidmvp.mvp.IView;

/**
 * Created by LockyLuo on 2017/9/28.
 */

public interface MineCommon extends IView<MinePresenter> {
    Activity getActivity();
    void startActivity(Class clazz);
}
