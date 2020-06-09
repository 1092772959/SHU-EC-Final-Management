package com.shu.icpc.service;

import com.shu.icpc.entity.Coach;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.PasswordGenerateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoachService extends CoreService {
    public Coach getByPhone(String phone) {
        return coachDao.findByPhone(phone);
    }

    public List<Coach> getBySchoolId(Integer schoolId) {
        return coachDao.findBySchoolId(schoolId);
    }

    public Coach getById(Integer coachId) {
        return coachDao.findById(coachId);
    }

    //shiro
    public int setCheckStatus(int coachId, int statusCode) {
        Coach coach = coachDao.findById(coachId);
        if (coach == null) {
            return Constants.FAIL;
        }
        coachDao.updateStatus(coachId, statusCode);
        return Constants.SUCCESS;
    }


    public void setCoach(Coach coach) {
        Coach c = coachDao.findById(coach.getId());

        //有些值固定不变
        coach.setPswd(c.getPswd());
        coach.setSchoolId(c.getSchoolId());
        coach.setPhone(c.getPhone());
        coach.setCheckStatus(c.getCheckStatus());

        coachDao.update(coach);
    }

    public Integer setPassword(Integer coachId, String origin, String newPassword) {
        Coach coach = coachDao.findById(coachId);
        if (coach == null) {
            return Constants.FAIL;
        }
        origin = PasswordGenerateUtil.getPassword(origin, coach.getPhone(), Constants.hashTime);
        String suppose = coach.getPswd();
        if (!suppose.equals(origin)) {
            return Constants.FAIL;
        }

        String pswd = PasswordGenerateUtil.getPassword(newPassword, coach.getPhone(), Constants.hashTime);
        coachDao.updatePswd(coachId, pswd);
        return Constants.SUCCESS;
    }

    public List<Coach> getAll() {
        return coachDao.findAll();
    }
}
