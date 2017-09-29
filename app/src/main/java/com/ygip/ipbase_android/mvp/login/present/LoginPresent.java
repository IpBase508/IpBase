package com.ygip.ipbase_android.mvp.login.present;

import android.app.Activity;

import com.google.gson.Gson;
import com.ygip.ipbase_android.mvp.login.view.LoginActivity;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.universalModel.AKey;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBeanList;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.AES;
import com.ygip.ipbase_android.util.SharedPrefUtils;
import com.ygip.ipbase_android.util.StartActivityUtil;
import com.ygip.ipbase_android.util.ToastUtils;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by XQM on 2017/7/22.
 */

public class LoginPresent extends XPresent<ICommon> {
    public UniversalModel universalModel=new UniversalModel();
    public Activity activity;
    public OnResponseListener responseListener=new OnResponseListener() {
        @Override
        public void onFinish(UniversalResponseBeanList responseBean, Exception e) {
            if(e!=null){//失败信息e
                activity.runOnUiThread(()->{
                    ToastUtils.show("登录失败 " + e.getMessage());
                });
                StartActivityUtil.start(activity, LoginActivity.class);
            }else {
                activity.runOnUiThread(()->{
                    try {
                        ToastUtils.show("欢迎 " + UniversalModel.getUser().getMemberName());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

                StartActivityUtil.start(activity, MainActivity.class);
            }
        }
    };

    public void login(Activity activity, LoginUser loginUser){
        this.activity=activity;
        if (loginUser==null) {
            universalModel.login(getUser(),responseListener);
        } else {
            universalModel.login(loginUser,responseListener);
        }
    }

    public LoginUser getUser(){
        UserVo userVo;
        String user=SharedPrefUtils.load("loginUser");
        if(user!=null){
            try {
                userVo=(new Gson()).fromJson(user,UserVo.class);
                LoginUser loginUser=new LoginUser();
                loginUser.setPhoneNumber(userVo.getPhoneNumber());
                loginUser.setPassword(AES.Decrypt(userVo.getPassword(), AKey.s));
                return loginUser;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return null;
    }
}
