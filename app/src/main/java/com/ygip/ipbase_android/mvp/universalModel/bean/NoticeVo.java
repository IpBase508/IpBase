package com.ygip.ipbase_android.mvp.universalModel.bean;

import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jinbin on 2017-08-13 19:15.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeVo extends DataSupport implements Serializable,Comparable<NoticeVo>{
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

    @Override
    public int compareTo(@NonNull NoticeVo o) {
        return -this.createTime.compareTo(o.createTime);
    }
}
