package com.ygip.ipbase_android.mvp.universalModel.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by jinbin on 2017-09-18 17:39.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class ProjectTeamVo extends DataSupport implements Serializable {
    String id;
    String memberId;
    UserVo userVo;
    String projectId;
    Long createTime;
}
