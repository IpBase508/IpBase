package com.ygip.ipbase_android.mvp.projects.model;

import java.io.Serializable;

/**
 * Created by lockyluo on 2017/7/28.
 */

public class Project implements Serializable{
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String[] getScreenShots() {
        return screenShots;
    }

    public void setScreenShots(String[] screenShots) {
        this.screenShots = screenShots;
    }

    private String logo;
    private String projectName;
    private String[] members;
    private String finishedDate;
    private String[] screenShots;
}
