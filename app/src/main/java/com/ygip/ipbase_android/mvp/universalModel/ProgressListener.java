package com.ygip.ipbase_android.mvp.universalModel;

/**
 * Created by LockyLuo on 2017/9/23.
 */

public interface ProgressListener {
    void onProgress(long progressLength, long totalLength, boolean done);
}
