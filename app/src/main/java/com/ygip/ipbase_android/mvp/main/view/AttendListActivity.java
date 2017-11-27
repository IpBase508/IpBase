package com.ygip.ipbase_android.mvp.main.view;

import android.os.Bundle;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;

import cn.droidlover.xdroidmvp.mvp.XActivity;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_list);
    }
}
