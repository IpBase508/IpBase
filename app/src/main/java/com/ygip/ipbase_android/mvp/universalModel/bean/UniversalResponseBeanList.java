package com.ygip.ipbase_android.mvp.universalModel.bean;
import com.google.gson.JsonObject;

import java.util.List;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的list对象
 */

@Data
public class UniversalResponseBeanList {
    private String msg;//信息
    private int code;
    private List<JsonObject> data;//里面是jsonObjects数据
    private Boolean success;
    private String error;
}
