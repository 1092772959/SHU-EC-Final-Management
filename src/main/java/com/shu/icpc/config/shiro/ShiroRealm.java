package com.shu.icpc.config.shiro;

import com.shu.icpc.dao.RoleDao;
import com.shu.icpc.entity.Admin;
import com.shu.icpc.entity.Coach;
import com.shu.icpc.entity.Student;
import com.shu.icpc.service.AdminService;
import com.shu.icpc.service.CoachService;
import com.shu.icpc.service.StudentService;
import com.shu.icpc.utils.Constants;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;


public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private StudentService studentService;

    @Resource
    private CoachService coachService;

    @Resource
    private AdminService adminService;

    @Resource
    private RoleDao roleDao;

    /**
     * 权限认证，即登录过后，每个身份不一定，对应能访问的接口也不同
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String phone = (String) principalCollection.getPrimaryPrincipal();

        List<String> roles = roleDao.findByPhone(phone);
        info.addRoles(roles);

        return info;
    }

    /**
     * 身份认证。即登录通过账号和密码验证登陆人的身份信息。
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        ShiroToken shiroToken = (ShiroToken) token;
        String phone = (String) shiroToken.getPrincipal();

        ShiroToken.Type type = shiroToken.getType();
        int status;
        String realmName = getName();
        ByteSource credentialsSalt = ByteSource.Util.bytes(phone);

        if (type.equals(ShiroToken.Type.Student)) {
            Student student = studentService.getByPhone(phone);
            status = student.getCheckStatus();
            if (status == Constants.CHECK_STATUS_PASS) {
                return new SimpleAuthenticationInfo(phone, student.getPswd(), credentialsSalt, realmName);
            } else if (status == Constants.CHECK_STATUS_CHECKED) {

                throw new LockedAccountException();
            } else {
                throw new DisabledAccountException();
            }
        } else if (type.equals(ShiroToken.Type.Coach)) {
            Coach coach = coachService.getByPhone(phone);
            status = coach.getCheckStatus();
            if (status == Constants.CHECK_STATUS_PASS) {
                return new SimpleAuthenticationInfo(phone, coach.getPswd(), credentialsSalt, realmName);
            } else if (status == Constants.CHECK_STATUS_CHECKED) {
                throw new LockedAccountException();
            } else {
                throw new DisabledAccountException();
            }
        } else {
            Admin admin = adminService.getByPhone(phone);
            return new SimpleAuthenticationInfo(phone, admin.getPswd(), credentialsSalt, realmName);      //admin用明文存储
        }
        //此处是正确的账号与密码，与studentService中的账号密码相匹配，若不同则抛出IncorrectCredentialsException异常
    }
}
