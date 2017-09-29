package com.ygip.ipbase_android.mvp.login.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.login.present.ICommon;
import com.ygip.ipbase_android.mvp.login.present.LoginPresent;
import com.ygip.ipbase_android.util.GlideCircleTransform;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;



public class SplashActivity extends XActivity<LoginPresent> implements ICommon{

    @BindView(R.id.logo)
    ImageView logo;


    @Override
    public void initData(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        Glide.with(this)
                .load(R.drawable.jidilogo)
                .transform(new GlideCircleTransform(this))
                .crossFade(300)
                .into(logo);
        (new Handler(Looper.getMainLooper())).postDelayed(()->{
            getP().login(context,null);
        },350);
    }


    @Override
    public Activity getActivity() {
        return context;
    }

    @Override
    public LoginPresent newP() {
        return new LoginPresent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}