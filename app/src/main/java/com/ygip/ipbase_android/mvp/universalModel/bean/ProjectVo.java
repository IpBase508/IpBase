package com.ygip.ipbase_android.mvp.universalModel.bean;


import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jinbin on 2017-08-11 15:19.
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class ProjectVo extends DataSupport implements Serializable,Comparable<ProjectVo> {
    int id;
    String projectId;
    String projectName;
    String detail;
    String creatorId;
    UserVo announcer;//项目创建者
    Integer projectType;//项目类型：0为app，1为网站，2为科研类项目，3为其他
    Long createTime;
    Long updateTime;
    Long deadLine;
    UserVo userVo;
    List<String> imageUrl;
    List<ProjectImageVo> projectImageVo;
    Boolean delete;
    List<String> userIds;
    List<ProjectTeamVo> projectTeamVoList;

    @Override
    public int compareTo(@NonNull ProjectVo o) {
        return -this.createTime.compareTo(o.createTime);
    }
}
