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
    private UniversalModel<List<ProjectVo>> universalModel;
    private Gson gson = UniversalModel.getGson();

    public static List<ProjectVo> getProjects() {
        return projects;
    }


    public void loadData() {
        Type type = new TypeToken<List<ProjectVo>>() {
        }.getType();
        universalModel = new UniversalModel<>(type);
        projects = new ArrayList<ProjectVo>();
        universalModel.getData(ApiUrl.Get.GET_PROJECT_URL, new String[]{"all=true"}, new OnResponseListener<List<ProjectVo>>() {
            @Override
            public void onFinish(UniversalResponseBean<List<ProjectVo>> responseBean, Exception e) {
                if (e == null) {
                    try {


                        projects =responseBean.getData();
                        getV().setProjects(projects);
                        save2db(projects);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else {
                    ToastUtils.show("刷新失败，显示缓存内容\n"+(e==null?" ":e.getMessage()));
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

    public void onDestory(){
        universalModel.cancelTask();
    }
}
