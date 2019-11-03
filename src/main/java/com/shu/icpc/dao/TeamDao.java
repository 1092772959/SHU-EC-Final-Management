package com.shu.icpc.dao;

import com.shu.icpc.entity.Team;

import java.util.List;
import java.util.Map;

public interface TeamDao {
    List<Team> findBySchoolId(Integer schoolId);

    Team findById(Integer id);

    List<Team> findByStudentId(Integer studentId);

    List<Map> findByContestId(Integer contestId);

    //查看该队是否报名比赛
    List<Map> findCheckHasContest(Integer teamId);

    //查看该学校报名该场比赛的队伍
    List<Map> findBySchoolAndContest(Integer schoolId, Integer contestId);

    Map findByTeamAndContest(Integer contestId, Integer teamId);

    int insert(Team team);

    int delete(Integer id);
}
