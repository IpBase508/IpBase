package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;


/**
 * Created by jinbin on 2017-08-12 10:57.
 */
@Data
public class AttendVo {
    String attendId;
    Long checkTime;
    Long attendTime;
    Long createTime;
    Long updateTime;
    String creatorId;
    UserVo announcer;//值班创建者
    String attendMemberId;
    UserVo userVo;
    String editorId;
    UserVo editor;
}
