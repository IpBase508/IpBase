package com.ygip.ipbase_android.mvp.projects.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lockyluo on 2017/7/28.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProjectUpload implements Serializable {
    String projectName;
    String detail;
    Integer projectType;//项目类型：0为app，1为网站，2为科研类项目，3为其他
    Long deadLine;
    List<String> imageUrl;
    List<String> userIds;
}
