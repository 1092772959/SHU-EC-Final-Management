package com.shu.icpc.dao;

import com.shu.icpc.entity.Contest;

import java.util.List;
import java.util.Map;

public interface ContestDao {

    List<Contest> findAll();

    List<Map> findAsCoach(Integer schoolId);       //以教练报名参赛的身份查看比赛

    Contest findById(Integer id);

    Contest findByName(String contestTitle);

    //查看该校报名该场比赛的队伍数
    Integer findByContestAndSchool(Integer contestId, Integer schoolId);

    //查看这个学校对于这场比赛的名额
    Integer findNumMaxByContestAndSchool(Integer contestId, Integer schoolId);

    Integer findNumFactByContest(Integer contestId);

    //查看该生参加的比赛
    List<Contest> findByStudentId(Integer studentId);

    //查看这场比赛所有学校的名额
    List<Map> findQuotaByContest(Integer contestId);

    //查看一场比赛某所学校的名额
    List<Map> findQuotaByContestAndSchool(Integer contestId, Integer schoolId);

    //锁定或解锁名额
    int updateNumLock(Integer contestId, Integer schoolId, Integer numLock);

//    Integer findTeamExistsByContestAndSchool(Integer contestId, Integer schoolId);

    //教练报名比赛
    int signInContest(Integer contestId, Integer teamId, Integer isStarred, String coachOnSite);

    //delete
    int signOffContest(Integer contestId, Integer teamId);

    Boolean hasContestRecord(Integer contestId);

    //添加一场比赛
    int insert(Contest contest);

    int update(Contest contest);

    int updateNumTotal(Integer id, Integer numTeamTotal);

    int delete(Integer id);

    //添加竞赛-学校名额
    int insertQuota(Integer contestId, Integer schoolId, Integer num);

    //更新竞赛-学校名额
    int updateQuota(Integer contestId, Integer schoolId, Integer num);

    //进餐人数
    int updateMealNum(Integer contestId, Integer schoolId, Integer mealNum);

    int insertOnSiteCoach(Integer contestId, Integer coachId);

    Integer findOnSiteByContest(Integer contestId);

    Integer findOnSiteByContestAndCoach(Integer contestId, Integer coachId);

    List<Map> findDetailedInfo(Integer contestId);

}
