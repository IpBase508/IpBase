package com.ygip.ipbase_android.mvp.universalPresenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.mvp.universalView.CheckPhoneNumActivity;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

/**
 * Created by LockyLuo on 2017/10/29.
 */

public class UniversalPresenter {
    private Context context;
    public static void showPhoneVaildate(Activity activity, @Nullable String title,int requestCode){
        if (title!=null) {
            CheckPhoneNumActivity.setTitle(title);
        }
        StartActivityUtil.startForResult(activity,CheckPhoneNumActivity.class,requestCode);
    }

    public UniversalPresenter(Context context){
        this.context=context;
    }

    public static void showPhoneVaildate(Activity activity,@Nullable String title, @Nullable String phone,int requestCode){
        if (title!=null) {
            CheckPhoneNumActivity.setTitle(title);
        }
        if (phone!=null){
            CheckPhoneNumActivity.setPhoneNumber(phone);
        }
        StartActivityUtil.startForResult(activity,CheckPhoneNumActivity.class,requestCode);
    }

    public static OnResponseListener onPutMemberListener = new OnResponseListener() {
        @Override
        public void onFinish(UniversalResponseBean responseBean, Exception e) {
            try {
                if (e == null) {
                    if (responseBean != null) {
                        if (responseBean.getData() != null) {

                            new Handler(Looper.getMainLooper()).post(() -> {
                                ToastUtils.show("修改成功");
                            });

                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                ToastUtils.show(responseBean.getMsg());
                            });
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ToastUtils.show("修改失败");
                        });
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ToastUtils.show(e.getMessage());
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    };

    public static JsonObject User2JsonObject(UserVo userVo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userVo.getUserId());
        if (userVo.getMemberName() != null)
            jsonObject.addProperty("memberName", userVo.getMemberName());
        if (userVo.getPhoneNumber() != null)
            jsonObject.addProperty("phoneNumber", userVo.getPhoneNumber());
        if (userVo.getDepartment() != null)
            jsonObject.addProperty("department", userVo.getDepartment());
        if (userVo.getImageUrl() != null)
            jsonObject.addProperty("imageUrl", userVo.getImageUrl());
        if (userVo.getDelete() != null)
            jsonObject.addProperty("delete", userVo.getDelete());
        if (userVo.getGrade() != null)
            jsonObject.addProperty("grade", userVo.getGrade());
        if (userVo.getUserLevel() != 0)
            jsonObject.addProperty("userLevel", userVo.getUserLevel());
        return jsonObject;
    }

    public void putUserData(UserVo userVo) {//更新user信息
        UniversalModel universalModel = new UniversalModel();
        JsonObject jsonObject = User2JsonObject(userVo);
        Logger.d(jsonObject);
        universalModel.putData(ApiUrl.Put.PUT_USER_URL, jsonObject, onPutMemberListener);
    }
}
