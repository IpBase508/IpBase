package com.ygip.ipbase_android.mvp.projects.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.projects.listener.ProjectDataListener;
import com.ygip.ipbase_android.util.SharedPrefUtils;
import com.ygip.ipbase_android.util.ToastUtils;
import com.ygip.ipbase_android.util.ViewDelegateByLocky;

import cn.droidlover.xdroidmvp.kit.Kits;

/**
 * Created by lockyluo on 2017/8/3.
 * 封装通过url传输javaBean的方法
 */

public class ProjectModel {

    private static Thread thread;
    private static ProjectModel projectModel;

    public static ProjectModel getInstance() {
        if (projectModel == null) {
            projectModel = new ProjectModel();
        }
        return projectModel;
    }


    public Project getData(Context context, ProjectDataListener projectDataListener) {
        Project project = new Project();
        if (projectDataListener != null) {
            projectDataListener.onStart();
        }
        try {

            String data = SharedPrefUtils.load("project_local");
            Logger.d(data);
            if (data.equals("")) {
                return null;
            }
            project = (new Gson()).fromJson(data, Project.class);

        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            if (projectDataListener != null) {
                projectDataListener.onFinish();
            }
            return project;
        }
    }

    private void saveLocalData(Project project, Context context, @Nullable ProjectDataListener projectDataListener) {
        if (projectDataListener != null) {
            projectDataListener.onStart();
        }

        String data = (new Gson()).toJson(project);
        SharedPrefUtils.save("project_local", data);
        if (projectDataListener != null) {
            projectDataListener.onFinish();
        }
    }

    public void UploadData(Project project, Context context, @Nullable ProjectDataListener projectDataListener) {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(() ->
                saveLocalData(project, context, projectDataListener)
        );
        thread.start();

    }


}
