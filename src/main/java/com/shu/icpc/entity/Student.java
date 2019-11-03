package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


public class Student {

    private Integer id;

    @NotBlank
    private String stuId;

    @NotBlank
    private String stuName;

    @NotBlank
    private String familyName;

    @NotBlank
    private String firstName;

    @NotNull
    private Integer schoolId;

    @NotBlank
    private String college;

    @NotBlank
    private String major;

    @NotNull
    private Integer enrollYear;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    private String sex;

    private String size;

    @NotBlank
    private String pswd;

    private Integer checkStatus;

    private String schoolName;        //school表数据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getEnrollYear() {
        return enrollYear;
    }

    public void setEnrollYear(Integer enroll_year) {
        this.enrollYear = enroll_year;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", stuId='" + stuId + '\'' +
                ", stuName='" + stuName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", schoolId=" + schoolId +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", size=" + size +
                ", checkStatus=" + checkStatus +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }
}
