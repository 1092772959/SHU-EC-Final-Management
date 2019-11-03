package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Contest {

    private Integer id;

    @NotBlank
    private String contestTitle;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private Date signStartTime;

    @NotNull
    private Date signEndTime;

    @NotNull
    private Integer schoolId;

    @NotBlank
    private String chief;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

//    @NotNull
//    private Integer numMax;

    private Integer numTeamFact;

    private Integer numTeamMax;

    private Integer numTeamTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContestTitle() {
        return contestTitle;
    }

    public void setContestTitle(String contestTitle) {
        this.contestTitle = contestTitle;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSignStartTime() {
        return signStartTime;
    }

    public void setSignStartTime(Date signStartTime) {
        this.signStartTime = signStartTime;
    }

    public Date getSignEndTime() {
        return signEndTime;
    }

    public void setSignEndTime(Date signEndTime) {
        this.signEndTime = signEndTime;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer hostSchoolId) {
        this.schoolId = hostSchoolId;
    }

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Integer getNumMax() {
//        return numMax;
//    }
//
//    public void setNumMax(Integer numMax) {
//        this.numMax = numMax;
//    }


    public Integer getNumTeamMax() {
        return numTeamMax;
    }

    public void setNumTeamMax(Integer numTeamMax) {
        this.numTeamMax = numTeamMax;
    }

    public Integer getNumTeamFact() {
        return numTeamFact;
    }

    public void setNumTeamFact(Integer numTeamFact) {
        this.numTeamFact = numTeamFact;
    }

    public Integer getNumTeamTotal() {
        return numTeamTotal;
    }

    public void setNumTeamTotal(Integer numTeamTotal) {
        this.numTeamTotal = numTeamTotal;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", contestTitle='" + contestTitle + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", signStartTime=" + signStartTime +
                ", signEndTime=" + signEndTime +
                ", schoolId=" + schoolId +
                ", chief='" + chief + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", numTeamFact=" + numTeamFact +
                ", numTeamMax=" + numTeamMax +
                ", numTeamTotal=" + numTeamTotal +
                '}';
    }
}
