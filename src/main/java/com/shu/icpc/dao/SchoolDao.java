package com.shu.icpc.dao;

import com.shu.icpc.entity.School;

import java.util.List;
import java.util.Map;

public interface SchoolDao {
    List<School> findAll();

    List<Map> findIdAndName();

    School findByName(String name);

    School findById(Integer id);

    Integer findChiefId(Integer id);

    int delete(Integer id);

    int insert(School school);

    int update(School school);

    int updateCheckStatus(Integer id, Integer status);
}
