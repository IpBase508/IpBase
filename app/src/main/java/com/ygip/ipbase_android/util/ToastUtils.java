package com.ygip.ipbase_android.util;

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

    public static void showLong(String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
    }
}
