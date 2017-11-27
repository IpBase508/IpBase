package com.ygip.ipbase_android.mvp.universalModel;


import com.ygip.ipbase_android.mvp.universalModel.bean.UniversalResponseBean;

/**
 * Created by LockyLuo on 2017/9/20.
 * 成功则返回T，否则返回Exception
 */

public interface OnResponseListener<T> {
    void onFinish(UniversalResponseBean<T> responseBean, Exception e);
}


