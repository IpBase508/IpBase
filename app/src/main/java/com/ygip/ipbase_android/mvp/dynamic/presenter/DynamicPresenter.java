package com.ygip.ipbase_android.mvp.dynamic.presenter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.dynamic.view.DynamicFragment;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.DynamicVo;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.util.ToastUtils;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by LockyLuo on 2017/11/2.
 */

public class DynamicPresenter extends XPresent<DynamicFragment> {
    private UniversalModel<List<DynamicVo>> universalModel;
    private Gson gson = UniversalModel.getGson();
    private static List<DynamicVo> dynamics;
    private String dbPath;
    private File dbFile;
    private SQLiteDatabase database;

    public static List<DynamicVo> getDynamics() {
        return dynamics;
    }

    public void loadDynamics(Boolean callRefreshView) {
        Type type = new TypeToken<List<DynamicVo>>() {
        }.getType();
        universalModel = new UniversalModel<>(type);
        dynamics = new ArrayList<DynamicVo>();
        universalModel.getData(ApiUrl.Get.GET_DYNAMIC_URL, new String[]{"all=true"}, new OnResponseListener<List<DynamicVo>>() {
            @Override
            public void onFinish(UniversalResponseBean<List<DynamicVo>> responseBean, Exception e) {
                if (e == null) {
                    try {
                        dynamics = responseBean.getData();
                        getV().setDynamics(dynamics);
                        save2db(dynamics);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    ToastUtils.show("刷新失败，显示缓存内容\n" + (e == null ? " " : e.getMessage()));
                    getV().setDynamics(getLocaldynamics());
                    Logger.d(e.getMessage());
                }
            }
        });

    }


    public void save2db(List<DynamicVo> dynamics) {

        try {
            DataSupport.deleteAllAsync(DynamicVo.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataSupport.saveAll(dynamics);
                        }
                    }, 80);
                }
            });

            Logger.i("---renew db---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DynamicVo> getLocaldynamics() {
        dynamics = DataSupport.findAll(DynamicVo.class);
        return dynamics;
    }

    public void onDestory() {
        if (universalModel != null) {
            universalModel.cancelTask();
        }
    }
}
