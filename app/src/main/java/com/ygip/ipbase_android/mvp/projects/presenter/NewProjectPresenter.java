package com.ygip.ipbase_android.mvp.projects.presenter;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.projects.model.ProjectModel;
import com.ygip.ipbase_android.mvp.projects.model.ProjectUpload;
import com.ygip.ipbase_android.mvp.projects.view.NewProjectActivity;
import com.ygip.ipbase_android.mvp.projects.view.ProjectsFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnFileResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.ProgressListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by lockyluo on 2017/8/3.
 */

public class NewProjectPresenter extends XPresent<NewProjectActivity> {

    private ProjectModel projectModel;
    private UniversalModel<FileResponseBean> fileModel = new UniversalModel<>();
    private UniversalModel universalModel = new UniversalModel();
    private int count = 0;
    private int total = 0;
    public List<String> imagesUploaded = new ArrayList<>();

    public ProjectUpload loadEditData() {

        if (projectModel == null) {
            projectModel = ProjectModel.getInstance();
        }
        return projectModel.getData();
    }

    public void uploadProject(ProjectUpload project) {
        if (projectModel == null) {
            projectModel = ProjectModel.getInstance();
        }
        saveLocal(project);
        uploadImages(project);
        Logger.d(project);

    }

    public void uploadImages(ProjectUpload project) {
        List<String> imagesLocal = project.getImageUrl();
        imagesUploaded.clear();
        count = 0;
        total = imagesLocal.size();
//        imagesUploaded =new ArrayList<>();

        if (imagesLocal == null) {
            Logger.e("uploadImages null");
            return;
        }
        for (int i = 0; i < imagesLocal.size(); i++) {
            File file = null;
            try {
                file = new File(imagesLocal.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file == null) {
                Logger.e("file err", imagesLocal);
                return;
            }
            fileModel.postFileData(ApiUrl.Post.POST_FILE_URL, file, new ProgressListener() {
                @Override
                public void onProgress(long progressLength, long totalLength, boolean done) {
                    getV().setProgress(progressLength, totalLength);
                }
            }, new OnFileResponseListener() {
                @Override
                public void onFinish(FileResponseBean fileResponseBean, Exception e) {
                    count++;
                    try {
                        imagesUploaded.add(fileResponseBean.getFile_path());
                        Logger.d("count " + "total " + total);
                        Logger.d(imagesLocal);
                        if (count == total) {
                            project.setImageUrl(imagesUploaded);
                            uploadAll(project);
                        }
                    } catch (Exception e1) {
                        getV().runOnUiThread(() -> ToastUtils.show("上传失败"));
                        e1.printStackTrace();
                    }
                }
            });
        }
    }

    private void uploadAll(ProjectUpload project) {
        Logger.d(UniversalModel.getGson().toJson(project));
        universalModel.postData(ApiUrl.Post.POST_PROJECT_URL, project, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                if (e == null) {

                    if (responseBean.getCode() == 200) {
                        getV().toast(responseBean.getMsg(), true);
                        ProjectsFragment.requireRefresh = true;
                    } else {
                        getV().toast(responseBean.getMsg(), false);
                    }
                } else {
                    getV().toast(e.getMessage(), false);
                }
            }
        });
    }

    public void saveLocal(ProjectUpload project) {
        if (projectModel == null) {
            projectModel = ProjectModel.getInstance();
        }
        projectModel.saveLocalData(project);
    }

    public void onDestory() {
        if (universalModel != null) {
            universalModel.cancelTask();
        }
    }
}
