package com.ygip.ipbase_android.mvp.projects.view;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.projects.model.Project;
import com.ygip.ipbase_android.mvp.projects.presenter.ProjectDetailPresenter;

import butterknife.BindView;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public class ProjectDetailActivity extends XActivity<ProjectDetailPresenter> {
    private Project project;

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

    @Override
    public void initData(Bundle savedInstanceState) {
        project=(Project) getIntent().getSerializableExtra("data");
        if(Kits.Empty.check(project)){
            titlebarTvTitle.setText("项目详情");
        }else {
            titlebarTvTitle.setText(project.getProjectName()+"项目");
        }

        titlebarLlLeft.setVisibility(View.VISIBLE);
        titlebarLlRight.setVisibility(View.VISIBLE);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public ProjectDetailPresenter newP() {
        return new ProjectDetailPresenter();
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
}
