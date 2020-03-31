package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class SoloContest {

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

    @NotNull
    private Integer numMax;

    //temporary variable, not persisted in database
    private Integer numSignedIn;

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

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
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

    public Integer getNumMax() {
        return numMax;
    }

    public void setNumMax(Integer numMax) {
        this.numMax = numMax;
    }

    public Integer getNumSignedIn() {
        return numSignedIn;
    }

    public void setNumSignedIn(Integer numSignedIn) {
        this.numSignedIn = numSignedIn;
    }
}
