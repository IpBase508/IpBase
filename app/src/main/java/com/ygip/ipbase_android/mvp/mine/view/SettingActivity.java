package com.ygip.ipbase_android.mvp.mine.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.mine.adapter.MineAdapter;
import com.ygip.ipbase_android.mvp.mine.presenter.MineCommon;
import com.ygip.ipbase_android.mvp.mine.presenter.MinePresenter;
import com.ygip.ipbase_android.util.DialogUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class SettingActivity extends XActivity<MinePresenter> implements MineCommon{
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.titlebar_ll_left)
    LinearLayout titlebarLlLeft;
    @BindView(R.id.titlebar_iv_right)
    ImageView titlebarIvRight;
    @BindView(R.id.titlebar_ll_right)
    LinearLayout titlebarLlRight;
    @BindView(R.id.titlebar_tv_right)
    TextView titlebarTvRight;
    @BindView(R.id.recyclerView_setting)
    RecyclerView recyclerViewSetting;
    private MineAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();
    private boolean isChange=false;
    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        data=getP().getMineData(1);

        if (adapter == null) {
            adapter = new MineAdapter(data,MineAdapter.SETTING,context);
        } else {
            adapter.updateData(data);
        }

        recyclerViewSetting.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSetting.setAdapter(adapter);
        recyclerViewSetting.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSetting.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    void initView(){
        titlebarTvTitle.setText("设置中心");
        titlebarLlLeft.setVisibility(View.VISIBLE);
        titlebarLlRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        if (adapter == null) {
            adapter = new MineAdapter(data,MineAdapter.SETTING,context);
        } else {
            adapter.updateData(data);
        }
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public MinePresenter newP() {
        return new MinePresenter();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (isChange) {
                DialogUtils.dialogFinish(context);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.titlebar_tv_title)
    public void onTitlebarTvTitleClicked() {
    }

    @OnClick(R.id.titlebar_ll_left)
    public void onTitlebarLlLeftClicked() {
        finish();
    }

    @OnClick(R.id.titlebar_iv_right)
    public void onTitlebarIvRightClicked() {
    }

    @OnClick(R.id.titlebar_ll_right)
    public void onTitlebarLlRightClicked() {
    }

    @OnClick(R.id.titlebar_tv_right)
    public void onTitlebarTvRightClicked() {
    }

    @OnClick(R.id.recyclerView_setting)
    public void onRecyclerViewSettingClicked() {
    }
}
