package com.shu.icpc.service;


import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.ResultTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ContestService extends CoreService {

    public boolean addContest(Contest contest) {
        Contest tmp = contestDao.findByName(contest.getContestTitle());
        if (tmp != null) {
            return false;
        }
        contestDao.insert(contest);

        //设名额初值
        contest = contestDao.findByName(contest.getContestTitle());
        int id = contest.getId();
        //异步
        List<School> schools = schoolDao.findAll();
        for (School school : schools) {
            contestDao.insertQuota(id, school.getId(), 0);
        }
        return true;
    }

    public Integer setContest(Contest contest){
        if(contest.getId() == null){
            return Constants.CONTEST_NOT_EXISTS;
        }
        Date signEnd = contest.getSignEndTime(), signStart = contest.getSignStartTime();
        if (signEnd.before(signStart)) {
            return Constants.TIME_ERROR;
        }

        Date end = contest.getEndTime(), start = contest.getStartTime();
        if (end.before(start) || signEnd.after(start)) {
            return Constants.TIME_ERROR;
        }
        School school = schoolDao.findById(contest.getSchoolId());
        if(school == null){
            return Constants.SIGN_UP_NO_SCHOOL;
        }

        contestDao.update(contest);
        return 0;
    }

    public List<Contest> getAll() {
        List<Contest> contests = contestDao.findAll();
        /*
        for (Contest contest : contests) {
            //参加的全部队伍数
            Integer numTeamTotal = contestDao.findNumFactByContest(contest.getId());
            contest.setNumTeamTotal(numTeamTotal);
        }*/
        return contests;
    }

    //以本校身份查看竞赛
    public List<Contest> getAsCoach(Integer schoolId) {
        List<Contest> contests = contestDao.findAll();
        for (Contest ct : contests) {
//            System.out.println(m.get("id"));
            //本校参加的队伍
            int numTeamFact = contestDao.findByContestAndSchool(ct.getId(), schoolId);
//            System.out.println(numFact);
            //本校名额
            Integer numTeamMax = contestDao.findNumMaxByContestAndSchool(ct.getId(), schoolId);

            //参加的全部队伍数
            //Integer numTeamTotal = contestDao.findNumFactByContest(ct.getId());

            numTeamMax = numTeamMax == null ? 0 : numTeamMax;
            ct.setNumTeamMax(numTeamMax);
            ct.setNumTeamFact(numTeamFact);
            //ct.setNumTeamTotal(numTeamTotal);
        }
        return contests;
    }

    public List<Contest> getByStudent(Integer studentId) {
        return contestDao.findByStudentId(studentId);
    }

    //此处应该按并发处理，报名比赛, 考虑教练是否到场
    public boolean login(Integer contestId, Integer teamId, Integer isStarred, boolean onSite) {
        Contest ct = contestDao.findById(contestId);
        if (ct == null) {
            return false;
        }

        //在规定时间内报名
        Date t1 = ct.getSignStartTime(), t2 = ct.getSignEndTime(), now = new Date();
        if (now.before(t1) && now.after(t2)) {
            return false;
        }

        Team tm = teamDao.findById(teamId);
        if (tm == null) {
            return false;
        }

        //所属学校数量控制
        Integer haveLogin = contestDao.findByContestAndSchool(contestId, tm.getSchoolId());
        haveLogin = haveLogin == null ? 0 : haveLogin;
        Integer numFact = contestDao.findNumMaxByContestAndSchool(contestId, tm.getSchoolId());
        numFact = numFact == null ? 0 : numFact;
        if (haveLogin >= numFact) {
            return false;
        }

        //防止一个以两个队的身份参加同一场比赛 或者 同一个队重复参加一个比赛
        Integer stuA = tm.getStuAId(), stuB = tm.getStuBId(), stuC = tm.getStuCId();
        List<Map> teams = teamDao.findByContestId(contestId);
        for (Map team : teams) {
            Integer a = (Integer) team.get("stuAId"), b = (Integer) team.get("stuBId"), c = (Integer) team.get("stuCId");
            Set<Integer> set = new HashSet<>();
            set.add(a);
            set.add(b);
            set.add(c);
            //重复报名
            if (set.contains(stuA) || set.contains(stuB) || set.contains(stuC)) {
                return false;
            }
        }
        String tag = onSite ? "是" : "否";
        contestDao.signInContest(contestId, teamId, isStarred, tag);
        contestDao.updateNumTotal(contestId, ct.getNumTeamTotal() + 1);
        return true;
    }

    //
    public void signOff(Integer contestId, Integer teamId) {
        Contest ct = contestDao.findById(contestId);
        if(ct == null) return;
        int code = contestDao.signOffContest(contestId, teamId);
        if(code == 0) return;
        contestDao.updateNumTotal(contestId, ct.getNumTeamTotal() - 1);
    }

    public int deleteContest(Integer contestId) {
        //check if there is team signed up
        if(contestDao.hasContestRecord(contestId)){
            logger.info(String.format("Contest: %d has sign up record, cannot be deleted!", contestId));
            return Constants.CONTEST_DELETE_ERROR;
        }
        contestDao.delete(contestId);
        return 0;
    }

    //增加学校-竞赛限额
    public int setQuota(Integer contestId, Integer schoolId, Integer num) {
        School school = schoolDao.findById(schoolId);
        if (school == null) {
            return -1;
        }
        Contest contest = contestDao.findById(contestId);
        if (contest == null) {
            return -1;
        }

        //check if there is a piece of record in db
        List<Map> quota_record = contestDao.findQuotaByContestAndSchool(contestId, schoolId);
        if(quota_record.isEmpty()){
            contestDao.insertQuota(contestId, schoolId, num);
            return 0;
        }

        Integer lock = (Integer)quota_record.get(0).get("numLock");
        if(lock != 0){
            return -1;
        }

        Integer numTeamFact = contestDao.findByContestAndSchool(contestId, schoolId);
        numTeamFact = numTeamFact == null ? 0 : numTeamFact;
        if(numTeamFact > num){
            return -1;
        }
        contestDao.updateQuota(contestId, schoolId, num);
        return 0;
    }

    public int setQuotaNumLock(Integer contestId, Integer schoolId, Integer numLock){
        if(numLock !=0 && numLock != 1){
            return -1;
        }
        int code = contestDao.updateNumLock(contestId, schoolId, numLock);
        return 0;
    }

    //查看限额
    public List<Map> getQuotaByContest(Integer contestId) {
        return contestDao.findQuotaByContest(contestId);
    }

    public List<Map> getQuotaByContestAndSchool(Integer contestId, Integer schoolId) {
        return contestDao.findQuotaByContestAndSchool(contestId, schoolId);
    }

    public boolean setMealCoachNum(Integer contestId, Integer schoolId, Integer mealCoachNum) {
        Integer teamExists = contestDao.findByContestAndSchool(contestId, schoolId);
        if (teamExists == null || teamExists < mealCoachNum) {
            return false;
        }
        contestDao.updateMealNum(contestId, schoolId, mealCoachNum);
        return true;
    }

    //提供详细报告
    public List<Map> getDetailedInformation(Integer contestId) {
        List<Map> res = contestDao.findDetailedInfo(contestId);
        Integer ptr = 1;
        for (Map m : res) {
            m.put("sequence", "队伍" + ptr++);
        }
        return res;
    }
}
