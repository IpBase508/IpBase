package com.ygip.ipbase_android.mvp.projects.listener;

import com.ygip.ipbase_android.util.ViewDelegateByLocky;

/**
 * Created by LockyLuo on 2017/9/7.
 */

public interface ProjectDataListener {
    void onStart();
    void onProgress(int progress);
    void onFinish();
}
