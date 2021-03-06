package com.ygip.ipbase_android.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ygip.ipbase_android.App;

/**
 * Toast工具类
 * Created by XQM on 2017/7/14.
 */

public class ToastUtils {
    /**
     * 可覆盖前一条消息显示的toast工具
     */
    private static Toast mToast;//控制toast时间

    public static void show(String text) {
        if (text==null)
            text="";
        if (mToast == null) {
            mToast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        Log.i("Toast", "--------------------------------");
        Log.i("Toast", text);
    }


    public static void show(Context context, String text) {
        if (text==null)
            text="";
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        Log.i("lockyToast", "--------------------------------");
        Log.i("lockyToast", text);
    }
    public static void showLong(String text) {
        if (text==null)
            text="";
        if (mToast == null) {
            mToast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        Log.i("Toast", "--------------------------------");
        Log.i("Toast", text);
    }

    public static void showLong(Context context, String text) {
        if (text==null)
            text="";
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        Log.i("lockyToast", "--------------------------------");
        Log.i("lockyToast", text);
    }
}
