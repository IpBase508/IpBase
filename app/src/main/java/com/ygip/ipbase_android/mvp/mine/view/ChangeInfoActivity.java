package com.ygip.ipbase_android.mvp.mine.view;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class ChangeInfoActivity extends XActivity<MinePresenter> implements MineCommon {


    private boolean phoneNumberChecked;
    private EventHandler eventHandler;

    @Override
    public void initData(Bundle savedInstanceState) {
        eventHandler=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable)data;
                    String msg = throwable.getMessage();
                    ToastUtils.show(msg);
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 处理
                        phoneNumberChecked=true;
                        Logger.d(data);
                    }
                }
            }
        };

        getP().showPhoneVaildate();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_info;
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
