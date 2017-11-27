package com.ygip.ipbase_android.mvp.universalModel.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Created by jinbin on 2017-08-09 17:20.
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class DynamicVo extends DataSupport implements Serializable{
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
