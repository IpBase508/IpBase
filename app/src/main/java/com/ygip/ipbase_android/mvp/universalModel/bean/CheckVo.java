package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到实体类
 * @author jinbin
 * @date 2017-11-03 22:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckVo {
    String id;
    String userId;
    UserVo userVo;
    String attendId;
    AttendVo attendVo;
    String checkMemberName;
    Long checkTime;
    String checkIp;
}
