package com.shu.icpc.service;


import com.shu.icpc.entity.Contest;
import com.shu.icpc.entity.School;
import com.shu.icpc.entity.Team;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.TimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ContestService extends CoreService{

    public boolean addContest(Contest contest){
        Contest tmp = contestDao.findByName(contest.getContestTitle());
        if(tmp != null){
            return false;
        }
        contestDao.insert(contest);

        //设名额初值
        contest = contestDao.findByName(contest.getContestTitle());
        int id = contest.getId();
        List<School> schools = schoolDao.findAll();
        for(School school: schools){
            contestDao.insertQuota(id, school.getId(), 0);
        }
        return true;
    }

    public List<Contest> getAll(){
        List<Contest> contests = contestDao.findAll();
        for(Contest contest: contests){
            //参加的全部队伍数
            Integer numTeamTotal = contestDao.findNumFactByContest(contest.getId());
            contest.setNumTeamTotal(numTeamTotal);
        }
        return contests;
    }

    //以本校身份查看竞赛
    public List<Contest> getAsCoach(Integer schoolId){
        List<Contest> contests = contestDao.findAll();
        for(Contest ct : contests){
//            System.out.println(m.get("id"));
            //本校参加的队伍
            int numTeamFact = contestDao.findByContestAndSchool(ct.getId(), schoolId);
//            System.out.println(numFact);
            //本校名额
            Integer numTeamMax = contestDao.findNumMaxByContestAndSchool(ct.getId(), schoolId);

            //参加的全部队伍数
            Integer numTeamTotal = contestDao.findNumFactByContest(ct.getId());

            numTeamMax = numTeamMax == null ? 0: numTeamMax;
            ct.setNumTeamMax(numTeamMax);
            ct.setNumTeamFact(numTeamFact);
            ct.setNumTeamTotal(numTeamTotal);
        }
        return contests;
    }

    public List<Contest> getByStudent(Integer studentId){
        return contestDao.findByStudentId(studentId);
    }

    //此处应该按并发处理，报名比赛, 考虑教练是否到场
    public boolean login(Integer contestId, Integer teamId, Integer isStarred, boolean onSite){
        Contest ct = contestDao.findById(contestId);
        if(ct == null){
            return false;
        }

        //在规定时间内报名
        Date t1 = ct.getSignStartTime(), t2 = ct.getSignEndTime(), now = new Date();
        if(now.before(t1) && now.after(t2)){
            return false;
        }

        Team tm = teamDao.findById(teamId);
        if(tm == null){
            return false;
        }

        //数量控制
        Integer haveLogin = contestDao.findByContestAndSchool(contestId, tm.getSchoolId());
        haveLogin = haveLogin == null? 0: haveLogin;
        Integer numFact = contestDao.findNumMaxByContestAndSchool(contestId, tm.getSchoolId());
        numFact = numFact == null? 0: numFact;
        if(haveLogin >= numFact){
            return false;
        }

        //防止一个以两个队的身份参加同一场比赛 或者 同一个队重复参加一个比赛
        Integer stuA = tm.getStuAId(), stuB = tm.getStuBId(), stuC = tm.getStuCId();
        List<Map> teams = teamDao.findByContestId(contestId);
        for(Map team : teams) {
            Integer a = (Integer)team.get("stuAId"), b = (Integer)team.get("stuBId"), c = (Integer)team.get("stuCId");
            Set<Integer> set = new HashSet<>();
            set.add(a);
            set.add(b);
            set.add(c);
            if(set.contains(stuA) || set.contains(stuB) || set.contains(stuC)){
                System.out.println("重复报名");
                System.out.println(team);
                System.out.println(tm);
                return false;
            }
        }
        String tag = onSite ? "是":"否";
        contestDao.signInContest(contestId, teamId, isStarred, tag);
//        if(onSite){
//            Integer coachId = teamDao.findById(teamId).getCoachId();
//            Integer exists = contestDao.findOnSiteByContestAndCoach(contestId, coachId);
//            if(exists == null){
//                contestDao.insertOnSiteCoach(contestId, coachId);
//            }
//        }


        return true;
    }

    //
    public void signOff(Integer contestId, Integer teamId){
        contestDao.signOffContest(contestId, teamId);
    }

    public void deleteContest(Integer contestId){
        contestDao.delete(contestId);
    }

    //增加学校-竞赛限额
    public boolean addQuota(Integer contestId, Integer schoolId, Integer num){
        School school = schoolDao.findById(schoolId);
        if(school == null){
            return false;
        }
        Contest contest = contestDao.findById(contestId);
        if(contest == null){
            return false;
        }
        System.out.println(school);
        System.out.println(contest);
        Integer haveNum = contestDao.findNumMaxByContestAndSchool(contestId, schoolId);
        if(haveNum == null){
            contestDao.insertQuota(contestId, schoolId, num);
            return true;
        }
        return false;
    }

    //查看限额
    public List<Map> getQuotaByContest(Integer contestId){
        return contestDao.findQuotaByContest(contestId);
    }

    public List<Map> getQuotaByContestAndSchool(Integer contestId, Integer schoolId){
        return contestDao.findQuotaByContestAndSchool(contestId, schoolId);
    }

    public int setQuota(Integer contestId, Integer schoolId, Integer num){
        Integer numTeamFact = contestDao.findNumMaxByContestAndSchool(contestId, schoolId);
        if(numTeamFact == null){
            contestDao.insertQuota(contestId, schoolId, num);
            return 1;
        }
        if(numTeamFact > num){
            return 0;
        }
        contestDao.updateQuota(contestId, schoolId, num);
        return 1;
    }

    public boolean setMealCoachNum(Integer contestId, Integer schoolId, Integer mealCoachNum){
        Integer teamExists = contestDao.findByContestAndSchool(contestId, schoolId);
        if(teamExists == null || teamExists < mealCoachNum){
            return false;
        }
        contestDao.updateMealNum(contestId, schoolId, mealCoachNum);
        return true;
    }

    //提供详细报告
    public List<Map> getDetailedInformation(Integer contestId){
        List<Map> res = contestDao.findDetailedInfo(contestId);
        Integer ptr = 1;
        for(Map m: res){
            m.put("sequence","队伍"+ptr++);
        }
        return res;
    }


    //上传文件
    String basePath;

    {
        try {
            basePath = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Integer uploadFile(MultipartFile file, Integer contestId, Integer teamId) throws IOException {
        if(file.isEmpty() || contestDao.findById(contestId) == null || teamDao.findById(teamId) == null){
            return Constants.FAIL;
        }
        //时间戳
        String timeStamp = TimeUtil.getMillPrimaryKey();
        String name = timeStamp + file.getName();


        //使用项目跟路径
        String path = basePath + contestId + name;
        System.out.println(name);

        File serverFile = new File(path);
        if(!serverFile.exists()){
            boolean createStatus = serverFile.mkdirs();
            if(!createStatus){
                //文件已存在
                return Constants.FAIL;
            }
        }

        file.transferTo(serverFile);
        contestDao.updateAwardFilePath(contestId, teamId, path);


        return Constants.SUCCESS;
    }
}
