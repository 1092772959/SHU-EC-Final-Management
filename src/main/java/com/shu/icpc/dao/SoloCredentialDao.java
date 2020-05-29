package com.shu.icpc.dao;

import com.shu.icpc.entity.SoloCredential;
import com.shu.icpc.entity.TeamCredential;

import java.util.List;

public interface SoloCredentialDao {
    SoloCredential findById(Integer id);

    List<SoloCredential> findBySoloContest(Integer soloContestId);

    List<SoloCredential> findBySoloContestAndSchool(Integer soloContestId, Integer schoolId);

    SoloCredential findBySoloContestAndStudent(Integer soloContestId, Integer studentId);

    int insert(SoloCredential soloCredential);
}
