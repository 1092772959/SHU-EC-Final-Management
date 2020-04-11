package com.shu.icpc.dao;

import com.shu.icpc.entity.TeamCredential;

import java.util.List;

public interface TeamCredentialDao {
    TeamCredential findById(Integer id);

    List<TeamCredential>  findByContest(Integer contestId);

    List<TeamCredential> findByContestAndSchool(Integer contestId, Integer schoolId);

    TeamCredential findByContestAndTeam(Integer contestId, Integer teamId);

    int insert(TeamCredential teamCredential);
}
