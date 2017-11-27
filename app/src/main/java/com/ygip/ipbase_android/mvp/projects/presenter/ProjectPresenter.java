package com.ygip.ipbase_android.mvp.projects.presenter;


import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.projects.adapter.ProjectAdapter;
import com.ygip.ipbase_android.mvp.projects.view.ProjectsFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.ToastUtils;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;


/**
 * Created by lockyluo on 2017/7/28.
 */

public class ProjectPresenter extends XPresent<ProjectsFragment> {

    private static List<ProjectVo> projects;
    private UniversalModel universalModel;
    private Gson gson = UniversalModel.getGson();

    public static List<ProjectVo> getProjects() {
        return projects;
    }

    private OnResponseListener responseListener = new OnResponseListener<ProjectVo>() {
        @Override
        public void onFinish(UniversalResponseBean responseBean, Exception e) {

        }
    };

    public void loadData() {
        universalModel = new UniversalModel<List<Object>>();
        projects = new ArrayList<ProjectVo>();
        universalModel.getData(ApiUrl.Get.GET_PROJECT_URL, new String[]{"all=true"}, new OnResponseListener<List<Object>>() {
            @Override
            public void onFinish(UniversalResponseBean<List<Object>> responseBean, Exception e) {
                if (e == null) {
                    try {
                        String json = gson.toJson(responseBean.getData());

                        Type type = new TypeToken<ArrayList<ProjectVo>>() {
                        }.getType();

                        projects = gson.fromJson(json, type);
                        getV().setProjects(projects);
                        save2db(projects);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else {
                    getV().getActivity().runOnUiThread(()-> ToastUtils.show("刷新失败"));
                    getV().setProjects(getLocalProjects());
                    Logger.d(e.getMessage());
                }
            }
        });

    }

    public void save2db(List<ProjectVo> projects) {

        try {
            DataSupport.deleteAllAsync(ProjectVo.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataSupport.saveAll(projects);
                        }
                    }, 80);
                }
            });

            Logger.i("---renew db---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ProjectVo> getLocalProjects(){
        projects=DataSupport.findAll(ProjectVo.class);
        return projects;
    }




}
