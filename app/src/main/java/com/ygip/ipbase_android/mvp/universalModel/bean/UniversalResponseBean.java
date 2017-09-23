package com.ygip.ipbase_android.mvp.universalModel.bean;
import com.google.gson.JsonObject;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的对象
 */

@Data
public class UniversalResponseBean {
    private String msg;//信息
    private int code;
    private JsonObject data;//里面是jsonObject数据
    private Boolean success;
    private String error;
}
