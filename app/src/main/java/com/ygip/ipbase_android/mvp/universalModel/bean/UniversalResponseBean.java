package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的对象
 */

@Data
public class UniversalResponseBean<T>{
    private String msg;//信息
    private int code;
    private T data;//里面是T泛型数据
    private Boolean success;
    private String error;
}
