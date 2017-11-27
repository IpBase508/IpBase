package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的对象
 */

@Data
public class LoginResponseBean {
    private String msg;//信息
    private int code;
    private LoginBean data;
    private Boolean success;
    private String error;
}
