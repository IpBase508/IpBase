package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;


/**
 * Created by jinbin on 2017-08-09 17:20.
 */
@Data
public class DynamicVo {
    String dynamicId;
    String title;
    String content;
    String imageUrl;
    String announcerId;
    Long createTime;
    Long updateTime;
    UserVo announcer;//动态发布者
    Boolean delete;
}
