package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.Data;

/**
 * Created by jinbin on 2017-08-13 19:15.
 */
@Data
public class NoticeVo {
    String noticeId;
    String senderId;
    UserVo sender;
    String receiverId;
    UserVo receiver;
    String title;
    String summary;
    Boolean read;
    Long createTime;
    Long updateTime;
}
