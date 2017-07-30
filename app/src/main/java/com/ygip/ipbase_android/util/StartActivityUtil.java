package com.ygip.ipbase_android.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.Serializable;



/**
 * Created by lockyluo on 2017/7/29.
 * 减少代码量
 */

public class StartActivityUtil {
    private static Intent intent;
    private static AndroidLogAdapter androidLogAdapter;

    public static void start(Context context, Class clazz) {
        intent = new Intent(context, clazz);

        if(androidLogAdapter==null){
            androidLogAdapter=new AndroidLogAdapter();
            Logger.addLogAdapter(androidLogAdapter);
        }
        Logger.d("startActivity without data");
        context.startActivity(intent);

    }

    public static void start(Context context, Class clazz, Serializable data) {
        intent = new Intent(context, clazz);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        intent.putExtras(bundle);

        if(androidLogAdapter==null){
            androidLogAdapter=new AndroidLogAdapter();
            Logger.addLogAdapter(androidLogAdapter);
        }
        Logger.d("startActivity with data");
        context.startActivity(intent);
    }
}
