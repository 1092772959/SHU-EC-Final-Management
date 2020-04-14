package com.shu.icpc.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class SoloCredential {
    private Integer id;

    @NotNull
    private String name;

    private String bucket;

    private Integer soloContestId;

    @NotNull
    private Integer studentId;

    private Date uploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Integer getSoloContestId() {
        return soloContestId;
    }

    public void setSoloContestId(Integer soloContestId) {
        this.soloContestId = soloContestId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
