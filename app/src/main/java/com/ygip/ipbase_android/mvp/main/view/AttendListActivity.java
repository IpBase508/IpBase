package com.ygip.ipbase_android.mvp.main.view;

import android.os.Bundle;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;

import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 签到情况列表
 */

public class AttendListActivity extends XActivity<MinePresenter> {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attend_list;
    }

    @Override
    public MinePresenter newP() {
        return new MinePresenter();
    }


    @Override
    protected void onDestroy() {
        if (getP() != null) {
            getP().onDestory();
        }
        super.onDestroy();
    }
}
