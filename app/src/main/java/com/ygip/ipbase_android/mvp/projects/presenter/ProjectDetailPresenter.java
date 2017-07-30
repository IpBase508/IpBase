package com.ygip.ipbase_android.mvp.projects.presenter;

import com.ygip.ipbase_android.mvp.projects.view.ProjectDetailActivity;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by lockyluo on 2017/7/29.
 */

public class ProjectDetailPresenter extends XPresent<ProjectDetailActivity> {
    public void loadData(){
        getV();
    }
}
