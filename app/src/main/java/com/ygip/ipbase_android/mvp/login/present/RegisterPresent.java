package com.ygip.ipbase_android.mvp.login.present;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.login.view.RegisterActivity;
import com.ygip.ipbase_android.mvp.universalModel.ApiUrl;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by XQM on 2017/9/24.
 */

public class RegisterPresent extends XPresent<RegisterActivity> {
    private UserVo userVo;
    private UniversalModel universalModel;
    private Gson gson = UniversalModel.getGson();

    public void register(String memberName, String password, String phoneNumber, String department,String grade) {
        Map<String, String> registermap = new HashMap<>();
        registermap.put("memberName", memberName);
        registermap.put("password", password);
        registermap.put("phoneNumber", phoneNumber);
        registermap.put("department", department);
        registermap.put("grade", grade);
        Logger.d(registermap);
        universalModel = new UniversalModel();

        universalModel.postData(ApiUrl.Post.POST_USER_URL, registermap, new OnResponseListener() {
            @Override
            public void onFinish(UniversalResponseBean responseBean, Exception e) {
                if (e == null && responseBean != null) {
                    try {
                        Logger.d(responseBean.getMsg());
                        userVo = gson.fromJson(gson.toJson(responseBean.getData()), UserVo.class);
                        if (userVo != null) {
                            getV().showSuccess("注册成功", userVo);
                        } else
                            getV().showFail(responseBean!=null?responseBean.getMsg():"");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        getV().showFail(responseBean!=null?responseBean.getMsg():"");
                    }
                } else {
                    getV().showFail(e != null ? e.getMessage() : "未知错误");
                }
            }
        });

    }
}
