package com.shu.icpc.config.shiro;

import com.shu.icpc.entity.Student;
import com.shu.icpc.service.StudentService;
import com.shu.icpc.utils.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthFilter extends BasicHttpAuthenticationFilter {
    @Resource
    private StudentService studentService;

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;

//        String token = req.getHeader("Authorization");
        Session session = SecurityUtils.getSubject().getSession();
        Object stu = session.getAttribute(Constants.SESSION_USER);
        System.out.println("过滤器: " + stu);
        if (stu == null) {
            System.out.println("未登录");
            throw new UnauthenticatedException();
        }
        System.out.println("login attempt");
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession session = req.getSession();
        Student stu = (Student) session.getAttribute(Constants.SESSION_USER);

        System.out.println("拦截器：" + stu);

        return true;
    }
}
