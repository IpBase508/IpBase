package com.ygip.ipbase_android.mvp.universalModel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jinbin on 2017-09-17 22:44.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImageVo {
    String id;
    String image;
    String projectId;
    Long createTime;
}
