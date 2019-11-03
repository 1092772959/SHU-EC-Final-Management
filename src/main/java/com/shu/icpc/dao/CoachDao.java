package com.shu.icpc.dao;

import com.shu.icpc.entity.Coach;

import java.util.List;

public interface CoachDao {

    List<Coach> findAll();

    List<Coach> findBySchoolId(Integer schoolId);

    Coach findById(Integer id);

    Coach findByPhone(String phone);

    Coach findByEmail(String email);

    String findRetrieveCodeByEmail(String email);

    int insert(Coach coach);

    int updateRetrieveCode(String email, String code);

    int update(Coach coach);

    int updatePswd(Integer id, String pswd);

    int updateStatus(Integer id, Integer status);

    int delete(Integer id);
}
