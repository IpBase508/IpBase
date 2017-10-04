package com.ygip.ipbase_android.mvp.login.present;

import android.app.Activity;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.ygip.ipbase_android.mvp.login.view.LoginActivity;
import com.ygip.ipbase_android.mvp.login.view.SplashActivity;
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
                getV().startActivity(LoginActivity.class);

            }else {
                activity.runOnUiThread(()->{
                    try {
                        ToastUtils.show("欢迎 " + UniversalModel.getUser().getMemberName());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
                getV().startActivity(MainActivity.class);
            }
        }
    };

    public void login(Activity activity, LoginUser loginUser){
        this.activity=activity;
        if (loginUser==null) {
            loginUser=getUser();
            if (loginUser!=null)
            {
                Logger.d(loginUser);
                universalModel.login(getUser(),responseListener);
            }else
                getV().startActivity(LoginActivity.class);
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
                if(userVo==null)
                    return null;
                loginUser.setPhoneNumber(userVo.getPhoneNumber());
                loginUser.setMemberName(userVo.getMemberName());

                if(loginUser.getPhoneNumber()!=null){
                    loginUser.setMemberName(null);
                }else if(loginUser.getMemberName()!=null){
                    loginUser.setPhoneNumber(null);
                }

                loginUser.setPassword(AES.Decrypt(userVo.getPassword(), AKey.s));
                return loginUser;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return null;
    }
}
