package com.shu.icpc.dao;

import com.shu.icpc.entity.Contest;
import com.shu.icpc.entity.SoloContest;

import java.util.List;
import java.util.Map;

public interface SoloContestDao {

    List<SoloContest> findAll();

    SoloContest findById(Integer id);

    List<SoloContest> findByName(String contestTitle);

    //查看该校报名该场比赛的队伍数
    Integer findByContestAndSchool(Integer soloContestId, Integer schoolId);

    Integer findNumFactByContest(Integer soloContestId);

    //查看该生参加的比赛
    List<SoloContest> findByStudentId(Integer studentId);

    //学生报名比赛
    int signInContest(Integer studentId, Integer soloContestId, Integer isStarred);

    //sign off
    int signOffContest(Integer studentId, Integer soloContestId);

    //添加一场比赛
    int insert(SoloContest soloContest);

    int delete(Integer id);

    //int update(Contest contest);

    List<Map> findDetailedInfo(Integer soloContestId);

}
