package com.ygip.ipbase_android.mvp.projects.model;

import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;

import lombok.Data;

import java.util.List;

/**
 * Created by jinbin on 2017-08-11 15:19.
 */
@Data
public class ProjectVo {
    String projectId;
    String projectName;
    String detail;
    String creatorId;
    UserVo announcer;//项目创建者
    Integer projectType;//项目类型：0为app，1为网站，2为其他
    Long createTime;
    Long updateTime;
    Long deadLine;
    List<String> projectUserIds;
    List<UserVo> userVoList;
    String imageUrl;
    Boolean delete;
}
