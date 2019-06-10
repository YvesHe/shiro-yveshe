/**
 * Copyright:   Copyright (c)2016
 * Company:     YvesHe
 * @version:    1.0
 * Create at:   2019年5月29日
 * Description:
 *
 * Author       YvesHe
 */
package com.yveshe.shiro.authorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 用户信息 + 权限认证都是模拟从数据库获取
 *
 * @author YvesHe
 *
 */
public class AuthorizationTest2 {

    public static void main(String[] args) {

        // 1.构建SecurityManager工厂，IniSecurityManagerFactory可以从ini文件中初始化SecurityManager环境
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:permission/shiro-permission-from-cache.ini");

        // 2.通过工厂创建SecurityManager
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        // 3.将securityManager设置到运行环境中
        SecurityUtils.setSecurityManager(securityManager);

        // 4.创建一个Subject实例，该实例认证要使用上边创建的securityManager进行
        Subject subject = SecurityUtils.getSubject();

        // 5.创建token令牌，记录用户认证的身份和凭证即账号和密码
        UsernamePasswordToken token = new UsernamePasswordToken("yveshe", "yveshepasswd");

        try {
            // Subject 携带token登录
            subject.login(token);
        } catch (AuthenticationException e) {
            // 登录失败可能出现的两个异常:
            // 1.IncorrectCredentialsException: 不正确的凭证异常,也就是账号存在,但是密码错误
            // 2.UnknownAccountException: 不知道的账号异常,也就是账号不纯在

            e.printStackTrace();
        }

        /* 授权测试 */
        Boolean isAuthenticated = subject.isAuthenticated();
        System.out.println("用户认证状态：" + isAuthenticated);// true
        if (isAuthenticated) {

            // 基于资源授权
            System.out.println("是否拥有某一个权限：" + subject.isPermitted("user:delete"));
            System.out.println("是否拥有多个权限：" + subject.isPermittedAll("user:create:1", "user:delete"));
            try { // 检查权限
                subject.checkPermission("sys:user:delete");
                subject.checkPermissions("user:create:1", "user:delete");
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }

    }
}
