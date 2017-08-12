package com.ygip.ipbase_android.mvp.projects.presenter;

import android.content.Context;

import com.ygip.ipbase_android.mvp.projects.model.Project;
import com.ygip.ipbase_android.mvp.projects.model.ProjectModel;
import com.ygip.ipbase_android.mvp.projects.view.NewProjectActivity;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by lockyluo on 2017/8/3.
 */

public class NewProjectPresenter extends XPresent<NewProjectActivity> {

    ProjectModel projectModel;

    public Project loadData(Context context){

        if(projectModel==null)
        {
            projectModel=ProjectModel.getInstance();
        }
        return projectModel.getData(context,getV().getvDelegate());
    }

    public void save(Project project){



        if(projectModel==null)
        {
            projectModel=ProjectModel.getInstance();
        }
        projectModel.UploadData(project,getV().getvDelegate());
    }
}
