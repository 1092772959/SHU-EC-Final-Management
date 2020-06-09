package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class School {

    public School() {
    }

    public School(@NotBlank String schoolName,
                  @NotBlank String chiefName, @NotBlank String phone) {
        this.schoolName = schoolName;
        this.chiefName = chiefName;
        this.phone = phone;
    }

    public School(Integer schoolId, @NotBlank String schoolName, @NotBlank String abbrName, @NotBlank String address,
                  @NotBlank String chiefName, @NotBlank String phone, @NotBlank String billEnterprise,
                  @NotBlank String postcode, @NotBlank String taxNum) {
        this.id = schoolId;
        this.schoolName = schoolName;
        this.abbrName = abbrName;
        this.address = address;
        this.chiefName = chiefName;
        this.phone = phone;
        this.billEnterprise = billEnterprise;
        this.postcode = postcode;
        this.taxNum = taxNum;
    }

    private Integer id;

    @NotBlank
    private String schoolName;

    @NotBlank
    private String abbrName;

    @NotBlank
    private String address;

    private Integer coachId;

    @NotBlank
    private String chiefName;

    @NotBlank
    private String phone;

    @NotBlank
    private String billEnterprise;

    @NotBlank
    private String postcode;

    @NotBlank
    private String taxNum;

    private Integer checkStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getChiefName() {
        return chiefName;
    }

    public void setChiefName(String chiefName) {
        this.chiefName = chiefName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBillEnterprise() {
        return billEnterprise;
    }

    public void setBillEnterprise(String billEnterprise) {
        this.billEnterprise = billEnterprise;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", abbrName='" + abbrName + '\'' +
                ", address='" + address + '\'' +
                ", coachId=" + coachId +
                ", chiefName='" + chiefName + '\'' +
                ", phone='" + phone + '\'' +
                ", billEnterprise='" + billEnterprise + '\'' +
                ", postcode='" + postcode + '\'' +
                ", taxNum='" + taxNum + '\'' +
                ", checkStatus=" + checkStatus +
                '}';
    }
}
