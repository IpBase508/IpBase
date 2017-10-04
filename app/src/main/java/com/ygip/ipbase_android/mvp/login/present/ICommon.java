package com.ygip.ipbase_android.mvp.login.present;

import android.app.Activity;


import cn.droidlover.xdroidmvp.mvp.IView;

/**
 * Created by LockyLuo on 2017/9/28.
 */

public interface ICommon extends IView<LoginPresent> {
    Activity getActivity();
    void startActivity(Class clazz);
}
