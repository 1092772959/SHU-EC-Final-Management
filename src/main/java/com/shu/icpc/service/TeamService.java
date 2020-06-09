package com.shu.icpc.service;

import com.shu.icpc.entity.Team;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeamService extends CoreService {
    public boolean add(Team team) {
        Integer idA = team.getStuAId(), idB = team.getStuBId(), idC = team.getStuCId();
        if (idA == idB || idB == idC || idA == idC) {
            return false;
        }


        team.setSchoolName(schoolDao.findById(team.getSchoolId()).getSchoolName());
        teamDao.insert(team);
        return true;
    }

    public List<Team> getByStudent(Integer studentId) {
        return teamDao.findByStudentId(studentId);
    }

    public List<Map> getByContestId(Integer contestId) {
        return teamDao.findByContestId(contestId);
    }

    public List<Team> getBySchoolId(Integer schoolId) {
        return teamDao.findBySchoolId(schoolId);
    }

    public List<Map> getBySchoolAndContest(Integer schoolId, Integer contestId) {
        return teamDao.findBySchoolAndContest(schoolId, contestId);
    }

    public boolean delete(Integer teamId) {
        List<Map> map = teamDao.findCheckHasContest(teamId);
        if (!map.isEmpty()) {
            return false;
        }
        teamDao.delete(teamId);
        return true;
    }
}
