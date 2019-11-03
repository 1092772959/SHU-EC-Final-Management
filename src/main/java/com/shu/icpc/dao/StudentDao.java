package com.shu.icpc.dao;

import com.shu.icpc.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentDao {
    List<Student> findAll();

    Student findById(Integer id);

    Student findByPhone(String phone);

    List<Student> findBySchoolId(Integer schoolId);

    Student findByEmail(String email);

//    List<Student> findAllWithSchoolName();

    int insert(Student student);

    String findRetrieveCodeByEmail(String email);

    int updateRetrieveCode(String email, String code);

    int delete(Integer id);

    int update(Student student);

    int updatePswd(Integer id, String pswd);

    int updateStatus(Integer id, Integer status);

    List<Map> find();
}
