package com.shu.icpc.dao;

import com.shu.icpc.entity.TeamCredential;

import java.util.List;

public interface TeamCredentialDao {
    List<TeamCredential>  findByContest(Integer contestId);

    List<TeamCredential> findByContestAndSchool(Integer contestId, Integer schoolId);

    int insert(TeamCredential teamCredential);
}
