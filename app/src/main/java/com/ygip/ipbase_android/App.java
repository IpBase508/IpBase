package com.ygip.ipbase_android;

import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by XQM on 2017/7/14.
 */

public class App extends MultiDexApplication {
    private static App instance;
    public static AndroidLogAdapter androidLogAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
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
