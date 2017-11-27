package com.ygip.ipbase_android.mvp.main.present;





/**
 * Created by LockyLuo on 2017/10/14.
 */

public interface OnLoadListener<T> {
    void onStart();
    void onFinish(T data,Exception e);

}
