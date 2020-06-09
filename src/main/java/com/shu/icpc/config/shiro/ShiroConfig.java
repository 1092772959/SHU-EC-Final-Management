package com.shu.icpc.config.shiro;

import com.shu.icpc.utils.Constants;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Component
public class ShiroConfig {
    /**
     * 密码校验规则HashedCredentialsMatcher
     * 这个类是为了对密码进行编码的 ,
     * 防止密码在数据库里明码保存 , 当然在登陆认证的时候 ,
     * 这个类也负责对form里输入的密码进行编码
     * 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(Constants.hashTime);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 自定义realm
     *
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm(HashedCredentialsMatcher matcher) {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setAuthorizationCachingEnabled(false);
        shiroRealm.setCredentialsMatcher(matcher);
        return shiroRealm;
    }

//    @Bean
//    public ShiroRealm shiroRealm(){
//        return new ShiroRealm();
//    }

    /**
     * 安全管理器
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    /**
     * @param securityManager
     * @return 1. 设置安全管理器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //添加自定义的过滤器
        LinkedHashMap<String, String> uriFilter = new LinkedHashMap<>();
        //注意，这个白名单要加 且要加在后面那些拦截器之前，否则会把login也拦截
        uriFilter.put("/api/user/**", "anon");

        uriFilter.put("/api/**", "authc");                  //需要登陆

        shiroFilterFactoryBean.setFilterChainDefinitionMap(uriFilter);

        //在jsp开发下，此处转跳至login页面，但此处直接返回fail
        shiroFilterFactoryBean.setLoginUrl("/api/user/unAuthenticate");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/api/user/unAuthenticate");
        /**
         *       loginUrl：没有登录的用户请求需要登录的页面时自动跳转到登录页面。 
         *     unauthorizedUrl：没有权限默认跳转的页面，登录的用户访问了没有被授权的资源自动跳转到的页面。
         * 其他的一些配置，如下：   
         *     successUrl：登录成功默认跳转页面，不配置则跳转至”/”，可以不配置，直接通过代码进行处理。在前后端一体时使用
         *     securityManager：这个属性是必须的，配置为securityManager就好了。
         */


        Map<String, Filter> filterMap = new HashMap<>();

//        filterMap.put("check", new AuthFilter());          //不能把过滤器交给Spring管理，否则会把静态资源也拦截
        shiroFilterFactoryBean.setFilters(filterMap);

        shiroFilterFactoryBean.setSecurityManager(securityManager);     //设置安全管理器
        return shiroFilterFactoryBean;
    }

    /**
     * 开启Shiro代理
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * lifecycleBeanPostProcessor是负责生命周期的 , 初始化和销毁的类
     * (可选)
     */
    @Bean()
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
