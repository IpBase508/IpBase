package com.ygip.ipbase_android.mvp.member.present;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.ArraySet;

import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.main.present.OnLoadListener;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;

import org.litepal.crud.DataSupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by LockyLuo on 2017/10/13.
 */

public class FindMemberAsync extends AsyncTask<OnLoadListener, Integer, List<UserVo>> {
    private OnLoadListener onLoadListener;
    private List<UserVo> data;
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
                    Set<UserVo> set = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        set = new ArraySet<>();
                    }else {
                        set = new HashSet<>();
                    }
                    data = DataSupport.where("grade like\'%" + param + "%\'").order("memberName asc").find(UserVo.class);
                    set.addAll(data);
                    data = DataSupport.where("phoneNumber like\'%" + param + "%\'").order("grade asc").find(UserVo.class);
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
                    Set<UserVo> set = new HashSet<>();
                    data = DataSupport.where("department like\'%" + param + "%\'").order("grade asc").find(UserVo.class);
                    set.addAll(data);
                    data = DataSupport.where("memberName like\'%" + param + "%\'").order("grade asc").find(UserVo.class);
                    set.addAll(data);
                    data.clear();
                    data.addAll(set);
                    Collections.sort(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.d(data);
                break;
            }
        }
    }

    private void getDetailList() {

    }

    @Override
    protected List<UserVo> doInBackground(OnLoadListener... onLoadListeners) {
        if (onLoadListeners[0] != null) {
            onLoadListener = onLoadListeners[0];
        }
        if (context == null)
        {
            Logger.d("context is null");
            return MemberPresenter.getMemberList();
        }

        find();//真正的工作


        Logger.d("initData finish");
        return data;
    }

    @Override
    protected void onPostExecute(List<UserVo> userVos) {
        if (onLoadListener != null) {
            onLoadListener.onFinish(userVos, null);
        } else {
            Logger.d("onLoadListener is null");
        }
        super.onPostExecute(userVos);
    }
}
