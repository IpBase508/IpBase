package com.ygip.ipbase_android;

import android.support.multidex.MultiDexApplication;

import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.ygip.ipbase_android.mvp.universalModel.AKey;

import org.litepal.LitePalApplication;

/**
 * Created by XQM on 2017/7/14.
 */

public class App extends LitePalApplication {
    private static App instance;
    public static AndroidLogAdapter androidLogAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MobSDK.init(instance, AKey.s1,AKey.s2);//短信sdk
        if(androidLogAdapter==null){
            androidLogAdapter=new AndroidLogAdapter(getFormatStrategy());
            Logger.addLogAdapter(androidLogAdapter);
        }
    }

    private FormatStrategy getFormatStrategy(){
        FormatStrategy formatStrategy= PrettyFormatStrategy
                .newBuilder()
                .tag("locky Log").build();
        return  formatStrategy;
    }
    public static AndroidLogAdapter getAndroidLogAdapter() {
        return androidLogAdapter;
    }

    public static App getInstance(){
        return instance;
    }
}
