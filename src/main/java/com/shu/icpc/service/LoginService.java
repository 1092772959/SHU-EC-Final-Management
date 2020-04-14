package com.shu.icpc.service;

import com.shu.icpc.config.shiro.ShiroToken;
import com.shu.icpc.entity.Admin;
import com.shu.icpc.entity.Coach;
import com.shu.icpc.entity.School;
import com.shu.icpc.entity.Student;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class LoginService extends CoreService {

    public Result login(String phone, String pswd) {
        Coach ch = coachDao.findByPhone(phone);
        if (ch != null) {
            Subject currentUser = SecurityUtils.getSubject();
            ShiroToken token = new ShiroToken(phone, pswd, ShiroToken.Type.Coach);
            currentUser.login(token);
            Session session = currentUser.getSession();
            session.setAttribute(Constants.SESSION_USER, ch);
            session.setTimeout(Constants.COOKIE_TIMEOUT);


            School school = schoolDao.findById(ch.getSchoolId());
            ch.setPswd("");
            ch.setSchoolName(school.getSchoolName());
            if(school.getCoachId() == ch.getId()){
                return ResultTool.resp(Constants.LOGIN_SUCCESS_CHIEF_CODE,ch);      //负责人教练
            }
            return ResultTool.resp(Constants.LOGIN_SUCCESS_COACH_CODE, ch);          //普通教练
        }

        Student stu = studentDao.findByPhone(phone);
        int status;
        if (stu != null) {
            //shiro
            Subject currentUser = SecurityUtils.getSubject();

            //ShiroRealm中的doGetAuthenticationInfo方法将数据库中的值与该值进行比对，若正确测放行，错误则抛出异常
            ShiroToken token = new ShiroToken(phone, pswd, ShiroToken.Type.Student);
            currentUser.login(token);//调用doGetAuthenticationInfo

            Session session = currentUser.getSession();
            session.setAttribute(Constants.SESSION_USER, stu);
            session.setTimeout(Constants.COOKIE_TIMEOUT);

            stu.setPswd("");
            School school = schoolDao.findById(stu.getSchoolId());
            stu.setSchoolName(school.getSchoolName());
            return ResultTool.resp(Constants.LOGIN_SUCCESS_CODE,stu);
        }

        return ResultTool.resp(Constants.LOGIN_NO_ACCOUNT_CODE);           //用户不存在
    }

    public Result adminLogin(String phone,String pswd) {
        Subject currentUser = SecurityUtils.getSubject();

        Admin admin = adminDao.findByPhone(phone);
        if(admin == null) {
            return ResultTool.resp(Constants.LOGIN_NO_ACCOUNT_CODE);
        }

        admin.setPswd("");
        Session session = currentUser.getSession();
        session.setAttribute(Constants.SESSION_USER, admin);
        session.setTimeout(Constants.COOKIE_TIMEOUT);

        ShiroToken token = new ShiroToken(phone, pswd, ShiroToken.Type.Admin);
        currentUser.login(token);
        return ResultTool.resp(Constants.SUCCESS, admin);
    }

    public Result logout() {
        //shiro登出
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            Session session = subject.getSession();
            session.removeAttribute(Constants.SESSION_USER);
            subject.logout();
            return ResultTool.resp(Constants.LOGIN_SUCCESS_CODE);
        }
        return ResultTool.resp(Constants.FAIL);
    }

    public Object getUserFromSession(){
        Subject user = SecurityUtils.getSubject();
        Session session = user.getSession();
        return session.getAttribute(Constants.SESSION_USER);
    }
}
