package com.ygip.ipbase_android.mvp.projects.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.member.present.MemberPresenter;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.ProjectVo;

import org.litepal.crud.DataSupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by LockyLuo on 2017/10/13.
 */

public class FindProjectAsync extends AsyncTask<OnLoadListener, Integer, List<ProjectVo>> {
    private OnLoadListener onLoadListener;
    private static List<ProjectVo> data;
    private int condition = -1;
    private String param;


    private Context context;

    public static final int DEFAULT = 0;
    public static final int FIND_BY_NUMBER = 1;
    public static final int FIND_BY_STRING = 2;

    @IntDef({DEFAULT, FIND_BY_NUMBER, FIND_BY_STRING})
    @Retention(RetentionPolicy.SOURCE)
    @interface Condition {
    }

    public void initTask(Context context, @Condition int condition, String param) {
        this.context = context;
        this.condition = condition;
        this.param = param;
    }

    public void find() {
        if (TextUtils.isEmpty(param))
            return;
        switch (condition) {
            case FIND_BY_NUMBER: {
                try {
                    Set<ProjectVo> set=new HashSet<>();
                    data = DataSupport.where("projectType like\'%" + param + "%\'").find(ProjectVo.class);
                    set.addAll(data);

                    data.clear();
                    data.addAll(set);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.d(data);
                break;
            }

            case FIND_BY_STRING: {
                try {
                    Set<ProjectVo> set=new HashSet<>();
                    data = DataSupport.where("projectName like\'%" + param + "%\'").find(ProjectVo.class);

                    set.addAll(data);
                    data.clear();
                    data.addAll(set);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.d(data);
                break;
            }
        }
    }


    @Override
    protected List<ProjectVo> doInBackground(OnLoadListener... onLoadListeners) {
        if (onLoadListeners[0] != null) {
            onLoadListener = onLoadListeners[0];
        }
        if (context == null)
        {
            Logger.d("context is null");
            return ProjectPresenter.getProjects();
        }

        find();


        Logger.d("initData finish");
        return data;
    }

    @Override
    protected void onPostExecute(List<ProjectVo> ProjectVos) {
        if (onLoadListener != null) {
            onLoadListener.onFinish(ProjectVos,null);
        } else {
            Logger.d("onLoadListener is null");
        }
        super.onPostExecute(ProjectVos);
    }
}
