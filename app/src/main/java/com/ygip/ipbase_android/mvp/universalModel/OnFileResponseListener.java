package com.ygip.ipbase_android.mvp.universalModel;

import com.ygip.ipbase_android.mvp.universalModel.bean.FileResponseBean;

/**
 * Created by LockyLuo on 2017/9/20.
 * 成功则返回List<JsonObject>，否则返回Exception
 */

public interface OnFileResponseListener {
    void onFinish(FileResponseBean fileResponseBean, Exception e);
}
