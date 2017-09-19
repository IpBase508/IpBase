package com.ygip.ipbase_android.mvp.universalModel;

import lombok.Data;

/**
 * Created by LockyLuo on 2017/9/17.
 * 登录返回的数据
 */
@Data
public class LoginResponseBean {
    UserVo user;
    String token;
}
