package com.ygip.ipbase_android.mvp.universalModel;

import lombok.Data;

/**
 * 用户实体类
 * Created by jinbin on 2017-08-04 17:36.
 */
@Data
public class UserVo {
    String userId;
    String memberName;
    String password;
    String phoneNumber;
    String department;
    String imageUrl;
    Long createTime;
    Long updateTime;
    Boolean delete;
    Integer userLevel;
}
