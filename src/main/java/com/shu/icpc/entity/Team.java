package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Team {
    private Integer id;

    @NotBlank
    private String teamName;

    @NotBlank
    private String foreignName;

    @NotNull
    private Integer schoolId;

    //    @NotBlank
    private String schoolName;

    @NotNull
    private Integer coachId;

    @NotBlank
    private String coachName;

    @NotNull
    private Integer stuAId;

    @NotBlank
    private String stuAName;

    @NotNull
    private Integer stuBId;

    @NotBlank
    private String stuBName;

    @NotNull
    private Integer stuCId;

    @NotBlank
    private String stuCName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getStuAId() {
        return stuAId;
    }

    public void setStuAId(Integer stuAId) {
        this.stuAId = stuAId;
    }

    public String getStuAName() {
        return stuAName;
    }

    public void setStuAName(String stuAName) {
        this.stuAName = stuAName;
    }

    public Integer getStuBId() {
        return stuBId;
    }

    public void setStuBId(Integer stuBId) {
        this.stuBId = stuBId;
    }

    public String getStuBName() {
        return stuBName;
    }

    public void setStuBName(String stuBName) {
        this.stuBName = stuBName;
    }

    public Integer getStuCId() {
        return stuCId;
    }

    public void setStuCId(Integer stuCId) {
        this.stuCId = stuCId;
    }

    public String getStuCName() {
        return stuCName;
    }

    public void setStuCName(String stuCName) {
        this.stuCName = stuCName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", foreignName='" + foreignName + '\'' +
                ", schoolId=" + schoolId +
                ", schoolName='" + schoolName + '\'' +
                ", coachId=" + coachId +
                ", coachName='" + coachName + '\'' +
                ", stuAId=" + stuAId +
                ", stuAName='" + stuAName + '\'' +
                ", stuBId=" + stuBId +
                ", stuBName='" + stuBName + '\'' +
                ", stuCId=" + stuCId +
                ", stuCName='" + stuCName + '\'' +
                '}';
    }
}
