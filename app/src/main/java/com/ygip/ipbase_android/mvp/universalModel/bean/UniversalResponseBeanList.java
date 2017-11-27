package com.ygip.ipbase_android.mvp.universalModel.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/18.
 * get返回的list对象
 */

@Data
public class UniversalResponseBeanList<T> {
    private String msg;//信息
    private int code;
    private List<T> data;//里面是jsonObjects数据
    private Boolean success;
    private String error;
}
