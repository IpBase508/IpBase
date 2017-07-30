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
    public static void show(String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可覆盖前一条消息显示的toast工具
     */
    private static Toast mToast;//控制toast时间

    public static void show(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        Log.i("lockyToast", text);
    }

    public static void showLong(String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
    }
}
