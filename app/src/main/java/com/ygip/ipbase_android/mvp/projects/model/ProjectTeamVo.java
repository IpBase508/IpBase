package com.ygip.ipbase_android.mvp.projects.model;

import com.ygip.ipbase_android.mvp.universalModel.bean.UserVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jinbin on 2017-09-18 17:39.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTeamVo {
    String id;
    String memberId;
    UserVo userVo;
    String projectId;
    Long createTime;
}
