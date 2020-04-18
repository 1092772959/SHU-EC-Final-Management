package com.shu.icpc.service;

import com.shu.icpc.entity.Contest;
import com.shu.icpc.entity.SoloContest;
import com.shu.icpc.entity.Student;
import com.shu.icpc.utils.Constants;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SoloContestService extends CoreService{

    public Integer addSoloContest(SoloContest soloContest){
        Date signEnd = soloContest.getSignEndTime(), signStart = soloContest.getSignStartTime();
        if(signEnd.before(signStart)) {
            return Constants.TIME_ERROR;
        }

        Date end = soloContest.getEndTime(), start = soloContest.getStartTime();
        if(end.before(start) || signEnd.after(start)){
            return Constants.TIME_ERROR;
        }

        if(soloContest.getNumMax() <=0 ){
            return Constants.NUM_ERROR;
        }

        soloContestDao.insert(soloContest);
        return Constants.SUCCESS;
    }

    public boolean hasSoloContest(Integer id){
        SoloContest sc = soloContestDao.findById(id);
        return sc != null;
    }

    public List<SoloContest> getAll(){
        return this.soloContestDao.findAll();
    }

    public SoloContest getById(Integer id){
        return this.soloContestDao.findById(id);
    }

    public List<SoloContest> getByStuId(Integer stuId){
        List<SoloContest> res = soloContestDao.findByStudentId(stuId);

        //also need the number of students who have already signed in
        for(int i = 0;i < res.size();++i){
            SoloContest sc = res.get(i);
            Integer cnt = this.soloContestDao.findNumFactByContest(sc.getId());
            sc.setNumSignedIn(cnt);
        }
        return res;
    }

    public List<SoloContest> getByTitle(String title){
        return this.soloContestDao.findByName(title);
    }

    //for coach
    public List<Map> getDetailsBySchool(Integer soloContestId, Integer schoolId){
        return this.soloContestDao.findDetailsByContestAndSchool(soloContestId, schoolId);
    }

    //for admin
    public List<Map> getDetailsByContest(Integer soloContestId){
        return this.soloContestDao.findDetailsByContest(soloContestId);
    }

    public Integer signIn(Integer stuId, Integer soloContestId, Integer isStarred){
        if(!this.studentService.hasStudent(stuId)){
            return Constants.STUDENT_NOT_EXISTS;
        }

        SoloContest sc = this.soloContestDao.findById(soloContestId);
        if(sc == null){
            return Constants.SOLO_CONTEST_NOT_EXISTS;
        }
        Date time = new Date();
        if(time.before(sc.getSignStartTime()) || time.after(sc.getSignEndTime())){
            return Constants.REGISTER_TIME_ERROR;
        }

        Integer stuIdCheck = soloContestDao.findSignedIn(stuId, soloContestId);
        if(stuIdCheck != null){
            return Constants.DUPLICATION_SIGN_IN;
        }

        Integer num = this.soloContestDao.findNumFactByContest(soloContestId);
        if(num >= sc.getNumMax()){
            return Constants.REGISTER_TIME_ERROR;
        }

        this.soloContestDao.signInContest(stuId, soloContestId, isStarred);
        //TODO: send message to student

        return Constants.SUCCESS;
    }

    public Integer signOff(Integer stuId, Integer soloContestId){
        Student stu = this.studentDao.findById(stuId);
        if(stu == null){
            return Constants.STUDENT_NOT_EXISTS;
        }
        SoloContest sc = this.soloContestDao.findById(soloContestId);
        if(sc == null){
            return Constants.SOLO_CONTEST_NOT_EXISTS;
        }

        Date time = new Date();
        if(time.before(sc.getSignStartTime()) || time.after(sc.getSignEndTime())){
            return Constants.REGISTER_TIME_ERROR;
        }

        int code = this.soloContestDao.signOffContest(stuId, soloContestId);
        if(code == 0){
            return Constants.SOLO_NO_RECORD;
        }

        //TODO send message to student
        return Constants.SUCCESS;
    }
}
