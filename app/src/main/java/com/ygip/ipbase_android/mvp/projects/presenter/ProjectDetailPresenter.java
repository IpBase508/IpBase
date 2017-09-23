package com.ygip.ipbase_android.mvp.projects.presenter;

import com.ygip.ipbase_android.mvp.projects.model.Project;
import com.ygip.ipbase_android.mvp.projects.view.ProjectDetailActivity;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by lockyluo on 2017/7/29.
 */

public class ProjectDetailPresenter extends XPresent<ProjectDetailActivity> {
    public void loadData(){//加载上一次保存的新建项目数据
        Project project=getV().getProject();
        getV().toast("已加载上次编辑数据");
    }
}
