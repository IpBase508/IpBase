package com.ygip.ipbase_android.mvp.universalModel.bean;

import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 * Created by jinbin on 2017-08-04 17:36.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends DataSupport implements Serializable,Comparable<UserVo> {
    int id;
    String userId;
    String memberName;
    String password;
    String phoneNumber;
    String department;
    String imageUrl;
    String grade;
    Long createTime=0L;
    Long updateTime=0L;
    Boolean delete;
    Integer userLevel=0;

    @Override
    public int compareTo(@NonNull UserVo o) {
        return this.grade.compareTo(o.grade);
    }
}
