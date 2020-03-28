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
            return Constants.FAIL;
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
        return this.soloContestDao.findByStudentId(stuId);
    }

    public List<SoloContest> getByTitle(String title){
        return this.soloContestDao.findByName(title);
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
            return Constants.FAIL;
        }

        Integer num = this.soloContestDao.findNumFactByContest(soloContestId);
        if(num >= sc.getNumMax()){
            return Constants.NUM_LIMIT;
        }

        this.soloContestDao.signInContest(stuId, soloContestId, isStarred);
        return Constants.SUCCESS;
    }

}
