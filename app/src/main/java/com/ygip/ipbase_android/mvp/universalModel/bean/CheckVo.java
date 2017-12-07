package com.ygip.ipbase_android.mvp.universalModel.bean;

import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 签到实体类
 * @author jinbin
 * @date 2017-11-03 22:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CheckVo extends DataSupport implements Serializable,Comparable<CheckVo>{
    String id;
    String userId;
    UserVo userVo;
    String attendId;
    AttendVo attendVo;
    String checkMemberName;
    Long checkTime;
    String checkIp;

    @Override
    public int compareTo(@NonNull CheckVo o) {
        return -this.checkTime.compareTo(o.checkTime);
    }
}
