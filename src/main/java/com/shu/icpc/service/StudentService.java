package com.shu.icpc.service;

import com.shu.icpc.entity.Student;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.PasswordGenerateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService extends CoreService {
    public Student getById(Integer id) {
        return studentDao.findById(id);
    }

    public Student getByPhone(String phone) {
        return studentDao.findByPhone(phone);
    }

    public List<Student> getAll() {
        return studentDao.findAll();
    }

    public List<Student> getBySchoolId(int schoolId) {
        return studentDao.findBySchoolId(schoolId);
    }

    //shiro
    public int setCheckStatus(int studentId, int statusCode) {
        Student stu = studentDao.findById(studentId);
        if (stu == null) {
            return Constants.FAIL;
        }
        studentDao.updateStatus(studentId, statusCode);
        return Constants.SUCCESS;
    }

    public void setStudent(Student student) {
        //有些值固定不变
        studentDao.update(student);
    }

    public Integer setPassword(Integer studentId, String origin, String newPassword) {
        Student student = studentDao.findById(studentId);
        if (student == null) {
            return Constants.FAIL;
        }
        origin = PasswordGenerateUtil.getPassword(origin, student.getPhone(), Constants.hashTime);
        String suppose = student.getPswd();
        if (!suppose.equals(origin)) {
            return Constants.FAIL;
        }

        String pswd = PasswordGenerateUtil.getPassword(newPassword, student.getPhone(), Constants.hashTime);

        studentDao.updatePswd(studentId, pswd);
        return Constants.SUCCESS;
    }

    public boolean hasStudent(Integer stuId) {
        Student stu = studentDao.findById(stuId);
        return stu != null;
    }
}
