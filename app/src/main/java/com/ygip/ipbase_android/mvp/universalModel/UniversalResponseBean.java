package com.ygip.ipbase_android.mvp.universalModel;
import com.google.gson.JsonArray;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的对象
 */

@Data
public class UniversalResponseBean{
    private String msg;
    private int code;
    private JsonArray data;//里面是jsonObject
    private boolean success;
    private String error;
}
