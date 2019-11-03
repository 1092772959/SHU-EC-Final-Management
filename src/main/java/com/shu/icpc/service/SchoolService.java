package com.shu.icpc.service;

import com.shu.icpc.entity.Bill;
import com.shu.icpc.entity.School;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.ResultTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SchoolService extends CoreService {

    public List<School> getAllSchools(){
        return schoolDao.findAll();
    }

    public List<Map> getAllIdAndNames(){
        return schoolDao.findIdAndName();
    }

    public School getByName(String name){
        return schoolDao.findByName(name);
    }

    public School getById(Integer schoolId){
        return schoolDao.findById(schoolId);
    }

    public Integer setCheckStatus(int schoolId, int statusCode){
        School school = schoolDao.findById(schoolId);
        if(school == null){
            return Constants.SIGN_UP_NO_SCHOOL;
        }
        schoolDao.updateCheckStatus(school.getId(), statusCode);
        coachDao.updateStatus(school.getCoachId(), statusCode);
        return Constants.SUCCESS;
    }

    public void setSchool(School school){
        schoolDao.update(school);
    }

    //---------------------税务

    public List<Bill> getAllBills(){
        return billDao.findAll();
    }

    public List<Bill> getBySchool(Integer schoolId){
        return billDao.findBySchoolId(schoolId);
    }

    public boolean addBill(Bill bill){
        Integer schoolId = bill.getSchoolId();
        School school = schoolDao.findById(schoolId);
        if(school == null){
            return false;
        }

        bill.setChecked(Constants.CHECK_STATUS_CHECKED);
        billDao.insert(bill);
        return true;
    }

    public boolean setBill(Bill bill){
        int checked = bill.getChecked();
        if(checked == Constants.CHECK_STATUS_PASS){                   //已开具发票,不可修改
            return false;
        }
        if(bill.getChecked().equals("普通发票")){
            bill.setAddress("");
            bill.setBank("");
            bill.setBankAccount("");
            bill.setTelephone("");
        }
        System.out.println(bill);
        billDao.update(bill);
        return true;
    }

    public boolean deleteBill(Integer billId){
        Bill bill = billDao.findById(billId);
        if(bill.getChecked() == Constants.CHECK_STATUS_PASS){
            return false;
        }
        billDao.delete(billId);
        return true;
    }

    //开具发票
    public void checkBill(Integer id){
        billDao.updateCheckedById(id, Constants.CHECK_STATUS_PASS);
    }

    public boolean checkBillType(Bill bill){
        String type = bill.getType();
        if(type.equals("增值税发票")){
            //以下四项不能为空
            if(bill.getAddress() == null || bill.getBank() == null || bill.getBankAccount() == null ||
                    bill.getTelephone() == null){
                return false;
            }
        }
        return true;
    }
}
