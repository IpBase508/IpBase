package com.ygip.ipbase_android.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.util.listener.OnActionListener;
import com.ygip.ipbase_android.util.listener.OnDialogListener;

/**
 * Created by lockyluo on 2017/8/4.
 * 对话框工具
 */

public class DialogUtils {

    /**
     * 自定义界面的对话框
     * @param context
     * @param message
     * @param view
     * @param onDialogListener
     */
    public static void dialogWithView(Context context, String message, View view, OnDialogListener onDialogListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setView(view);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 点击“确认”后的操作
            if (onDialogListener!=null) {
                onDialogListener.positive();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 点击“取消”后的操作
            if (onDialogListener!=null) {
                onDialogListener.negative();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }



    public static void dialogFinish(Context context) //返回/退出确认对话框
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(" ");
        builder.setMessage("确认直接返回？");

        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 点击“确认”后的操作
            ((Activity)context).finish();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 点击“取消”后的操作
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    /**
     * 两个按钮的默认对话框
     * @param context
     * @param message
     * @param onDialogListener
     */
    public static void dialogDefault(Context context,String message,OnDialogListener onDialogListener) //返回确认对话框
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("操作确认");
        builder.setMessage(message);

        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 点击“确认”后的操作
            if (onDialogListener!=null) {
                onDialogListener.positive();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 点击“取消”后的操作
            if (onDialogListener!=null) {
                onDialogListener.negative();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
}
