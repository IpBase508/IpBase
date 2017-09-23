package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;

/**
 * Created by jinbin on 2017-08-13 23:15.
 */
@Data
public class RecycleVo {
    String recyclerId;
    Integer recyclerType;
    String dynamicId;
    DynamicVo dynamic;
    String projectId;
    ProjectVo project;
    String operatorId;
    UserVo operator;
    Long createTime;
    Long updateTime;
}
