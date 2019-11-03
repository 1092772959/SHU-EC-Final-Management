package com.shu.icpc.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Bill {
    private Integer id;

    @NotNull
    private Integer schoolId;

    @NotBlank
    private String billEnterprise;

    @NotBlank
    private String taxNum;

    private String address;

    private String telephone;

    private String bank;

    private String bankAccount;

    @NotBlank
    private String content;

    @NotBlank
    private String type;

    @NotNull
    private Long money;

    private Integer checked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getBillEnterprise() {
        return billEnterprise;
    }

    public void setBillEnterprise(String billEnterprise) {
        this.billEnterprise = billEnterprise;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", schoolId=" + schoolId +
                ", billEnterprise='" + billEnterprise + '\'' +
                ", taxNum='" + taxNum + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", bank='" + bank + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", money=" + money +
                ", checked=" + checked +
                '}';
    }
}
