package com.ygip.ipbase_android.mvp.universalModel;

import com.google.gson.JsonArray;

/**
 * Created by LockyLuo on 2017/9/20.
 * 成功则返回JsonArray，否则返回Exception
 */

public interface OnResponseListener {
    void onFinish(JsonArray jsonElements,Exception e);
}
