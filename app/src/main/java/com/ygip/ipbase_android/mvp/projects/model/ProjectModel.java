package com.ygip.ipbase_android.mvp.projects.model;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.projects.listener.ProjectDataListener;
import com.ygip.ipbase_android.util.SharedPrefUtils;

/**
 * Created by lockyluo on 2017/8/3.
 * 封装通过url传输javaBean的方法
 */

public class ProjectModel {

    private static ProjectModel projectModel;

    public static ProjectModel getInstance() {
        if (projectModel == null) {
            projectModel = new ProjectModel();
        }
        return projectModel;
    }


    public ProjectUpload getData() {
        ProjectUpload project = new ProjectUpload();

        try {

            String data = SharedPrefUtils.load("project_local");
            Logger.d(data);
            if (data.equals("")) {
                return null;
            }
            project = (new Gson()).fromJson(data, ProjectUpload.class);

        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            return project;
        }
    }

    public void saveLocalData(ProjectUpload project) {


        try {
            String data = (new Gson()).toJson(project);
            SharedPrefUtils.save("project_local", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void UploadData(ProjectUpload project, Context context, @Nullable ProjectDataListener projectDataListener) {





    }


}
