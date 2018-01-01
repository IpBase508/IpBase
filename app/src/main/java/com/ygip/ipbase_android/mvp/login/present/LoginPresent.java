package com.ygip.ipbase_android.mvp.login.present;

import android.app.Activity;

import com.google.gson.Gson;
import com.ygip.ipbase_android.mvp.login.view.LoginActivity;
import com.ygip.ipbase_android.mvp.main.view.MainActivity;
import com.ygip.ipbase_android.mvp.universalModel.AKey;
import com.ygip.ipbase_android.mvp.universalModel.OnResponseListener;
import com.ygip.ipbase_android.mvp.universalModel.UniversalModel;
import com.ygip.ipbase_android.mvp.universalModel.bean.LoginUser;
import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;
import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;
import com.ygip.ipbase_android.util.AES;
import com.ygip.ipbase_android.util.SharedPrefUtils;
import com.ygip.ipbase_android.util.ToastUtils;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by XQM on 2017/7/22.
 */

public class LoginPresent extends XPresent<ICommon> {
    public UniversalModel universalModel = new UniversalModel();
    public Activity activity;
    public OnResponseListener responseListener = new OnResponseListener() {
        @Override
        public void onFinish(UniversalResponseBean responseBean, Exception e) {
            if (e != null) {//失败信息e
                getV().show(e == null ? "登录失败" : "登录失败\n" + e.getMessage());
                if (UniversalModel.getFirstLogin()) {
                    getV().startActivity(LoginActivity.class);
                } else {
                    getV().startActivity(MainActivity.class);
                }

            } else {
                try {
                    getV().show("欢迎 " + UniversalModel.getUser().getMemberName());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                getV().startActivity(MainActivity.class);
                activity.finish();
            }
        }
    };

    public void login(Activity activity, LoginUser loginUser) {
        this.activity = activity;
        if (loginUser == null) {
            loginUser = getLocalUser();
            if (loginUser != null) {
                universalModel.login(getLocalUser(), responseListener);
            } else
                getV().startActivity(LoginActivity.class);
        } else {
            universalModel.login(loginUser, responseListener);
        }
    }

    public LoginUser getLocalUser() {
        UserVo userVo;
        String user = SharedPrefUtils.load("loginUser");
        if (user != null) {
            try {
                userVo = (new Gson()).fromJson(user, UserVo.class);
                LoginUser loginUser = new LoginUser();
                if (userVo == null)
                    return null;
                loginUser.setPhoneNumber(userVo.getPhoneNumber());
                loginUser.setMemberName(userVo.getMemberName());

//                if(loginUser.getPhoneNumber()!=null){
//                    loginUser.setMemberName(null);
//                }else
                if (loginUser.getMemberName() != null) {
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

    public void onDestory() {
        if (universalModel != null) {
            universalModel.cancelTask();
        }
    }
}
