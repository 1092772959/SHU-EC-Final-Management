package com.shu.icpc.service;

import com.shu.icpc.Component.MailService;
import com.shu.icpc.Component.mq.Mail;
import com.shu.icpc.Component.mq.RabbitMQConfig;
import com.shu.icpc.entity.Coach;
import com.shu.icpc.entity.School;
import com.shu.icpc.entity.Student;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.FileUtil;
import com.shu.icpc.utils.PasswordGenerateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SignService extends CoreService {

    @Resource
    private MailService mailService;

    @Resource
    private RabbitMQConfig mqConfig;

    public Integer schoolSignUp(School school, Coach chief) {
        School t = schoolDao.findByName(school.getSchoolName());
        if (t != null) {
            return Constants.SIGN_UP_SCHOOL_EXISTS;
        }

        boolean exists = checkExists(chief.getPhone());
        //该手机号已注册
        if (exists)
            return Constants.SIGN_UP_ACCOUNT_EXISTS;

        school.setChiefName(chief.getCoachName());
        school.setPhone(chief.getPhone());
        school.setCheckStatus(Constants.CHECK_STATUS_CHECKED);
        schoolDao.insert(school);

        //设置外键
        chief.setSchoolId(school.getId());
        //密码加密
        String password = PasswordGenerateUtil.getPassword(chief.getPswd(), chief.getPhone(), Constants.hashTime);
        chief.setPswd(password);

        chief.setCheckStatus(Constants.CHECK_STATUS_CHECKED);
        coachDao.insert(chief);

        //增加权限
        int roleId = roleDao.findRoleIdByRoleName(Constants.CHIEF);
        roleDao.insertUserRole(chief.getPhone(), roleId);
        roleId = roleDao.findRoleIdByRoleName(Constants.COACH);
        roleDao.insertUserRole(chief.getPhone(), roleId);

        //更新school的外键
        t.setCoachId(chief.getId());
        schoolDao.update(t);

        return Constants.SIGN_UP_SUCCESS_CODE;
    }

    public Integer studentSignUp(Student student) {
        boolean res = checkSchoolPass(student.getSchoolId());
        if (!res)
            return Constants.SIGN_UP_NO_SCHOOL;
        boolean exists = checkExists(student.getPhone());
        if (exists)
            return Constants.SIGN_UP_ACCOUNT_EXISTS;

        String password = PasswordGenerateUtil.getPassword(student.getPswd(), student.getPhone(), Constants.hashTime);
        student.setPswd(password);
        student.setCheckStatus(Constants.CHECK_STATUS_CHECKED);
        studentDao.insert(student);
        //增加权限
        int stuRoleId = roleDao.findRoleIdByRoleName(Constants.STU);
        roleDao.insertUserRole(student.getPhone(), stuRoleId);
        return Constants.SIGN_UP_SUCCESS_CODE;
    }

    public Integer coachSignUp(Coach coach) {
        boolean res = checkSchoolPass(coach.getSchoolId());
        if (!res)
            return Constants.SIGN_UP_NO_SCHOOL;

        boolean exists = checkExists(coach.getPhone());
        if (exists)
            return Constants.SIGN_UP_ACCOUNT_EXISTS;

        String password = PasswordGenerateUtil.getPassword(coach.getPswd(), coach.getPhone(), Constants.hashTime);
        coach.setPswd(password);
        coach.setCheckStatus(Constants.CHECK_STATUS_CHECKED);
        coachDao.insert(coach);
        //增加权限
        int roleId = roleDao.findRoleIdByRoleName(Constants.COACH);
        roleDao.insertUserRole(coach.getPhone(), roleId);
        return Constants.SIGN_UP_SUCCESS_CODE;
    }

    //检测账户是否已经被教练或学生注册，一个电话只能对应教练或学生，不能重复
    public boolean checkExists(String phone) {
        Student student = studentDao.findByPhone(phone);
        if (student != null) {
            return true;
        }
        Coach coach = coachDao.findByPhone(phone);
        if (coach != null) {
            return true;
        }
        return false;
    }

    //队员和教练注册需先申请学校且学校已通过审核
    public boolean checkSchoolExists(Integer schoolId) {
        School school = schoolDao.findById(schoolId);
        if (school == null) {
            return false;
        }
        return true;
    }

    public boolean checkSchoolPass(Integer schoolId) {
        School school = schoolDao.findById(schoolId);
        if (school == null || school.getCheckStatus() != Constants.CHECK_STATUS_PASS) {
            return false;
        }
        return true;
    }

    //找回密码
    public boolean sendRetrieveEmail(String email) {
        Coach ch = coachDao.findByEmail(email);
        String code = null, title = "SHU-ECFINAL 找回密码", baseContent = "验证码：";
        if (ch != null) {
            getAndSendCheckCode(email, 1);
            return true;
        }
        Student stu = studentDao.findByEmail(email);
        if (stu != null) {
            getAndSendCheckCode(email, 2);
            return true;
        }
        System.out.println(email + "not found");
        return false;
    }

    /**
     * 生成校验码并放入缓存，时效10分钟,tag标记身份 1:教练, 2:学生
     *
     * @param email
     * @param tag
     */
    private void getAndSendCheckCode(String email, int tag) {
        String code = generateCode(), title = "SHU-ECFINAL 找回密码", baseContent = "验证码：";
        StringBuilder content = new StringBuilder(baseContent);
        content.append(code);
        content.append("\n有效时间为10分钟。");
        Mail m = new Mail();
        m.setEmail(email);
        m.setTitle(title);
        m.setContent(content.toString());
        //缓存内过期时间为10分钟
        redisTemplate.opsForValue().set(email, code+tag, 10, TimeUnit.MINUTES);

        //send to mail service; using rabbit mq to decouple

        //serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtil.close(oos, baos);
        //send
        System.out.println("produce message");
        rabbitTemplate.convertAndSend(mqConfig.getExchange(), mqConfig.getRouteKey(), baos.toByteArray());
        //mailService.sendSimpleMail(email,title, baseContent+code+"\n有效时间为10分钟。");
    }

    //确认并找回
    public boolean checkAndRetrieve(String email, String code, String newPassword) {
        String supposedCode = null;
        Integer tag;
        supposedCode = (String) redisTemplate.opsForValue().get(email);
        if (supposedCode != null) {
            tag = Integer.parseInt(supposedCode.substring(6, 7));
            supposedCode = supposedCode.substring(0, 6);
            if (!supposedCode.equals(code)) {

                //验证码不正确
                return false;
            }
            if (tag == 1) {       //教练
                Coach ch = coachDao.findByEmail(email);
                String pswd = PasswordGenerateUtil.getPassword(newPassword, ch.getPhone(), Constants.hashTime);
                coachDao.updatePswd(ch.getId(), pswd);
            } else if (tag == 2) {      //学生
                Student stu = studentDao.findByEmail(email);
                String pswd = PasswordGenerateUtil.getPassword(newPassword, stu.getPhone(), Constants.hashTime);
                studentDao.updatePswd(stu.getId(), pswd);
            }
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }

    //随机生成校验码
    private String generateCode() {
//        StringBuffer code = new StringBuffer();
        Integer num = (int) (Math.random() * 1000000);
        return num.toString();
    }

}
