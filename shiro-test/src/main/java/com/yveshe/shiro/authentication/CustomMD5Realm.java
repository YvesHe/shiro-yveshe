/**
 * Copyright:   Copyright (c)2016
 * Company:     YvesHe
 * @version:    1.0
 * Create at:   2019年5月29日
 * Description:
 *
 * Author       YvesHe
 */
package com.yveshe.shiro.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 自定义realm
 *
 * @author YvesHe
 *
 */
public class CustomMD5Realm extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("CustomRealm111");
    }

    /**
     * 模拟数据库中的用户信息
     */
    private static Map<String, String> user_passwd_map = new HashMap<String, String>();
    static {
        user_passwd_map.put("yveshe", MD5Util.md5String("yveshepasswd"));
        user_passwd_map.put("yves", MD5Util.md5String("yvespasswd"));
        user_passwd_map.put("jack", MD5Util.md5String("jackpasswd"));
    }

    /**
     * 认证(token令牌认证)
     *
     * Token=null时,在认证个时将会抛出UnknownAccountException异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 1.从token中获取用户信息
        String userName = (String) token.getPrincipal();

        // 2.从数据库中查询用户的完整信息
        if (!user_passwd_map.containsKey(userName)) {// 用户不存在
            return null;
        }

        // 3.构建认证信息
        String passwd = user_passwd_map.get(userName);
        Object principal = userName; // 用户名称
        Object hashedCredentials = passwd; // 加密后的密码
        ByteSource credentialsSalt = ByteSource.Util.bytes(MD5Util.SALT); // 加密盐
        String realmName = this.getName();// 领域名称?
        AuthenticationInfo info = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
        return info;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalcollection) {
        return null;
    }

    /**
     * MD5加密
     *
     * @author YvesHe
     *
     */
    public static class MD5Util {

        public static final String SALT = "yveshe_salt";
        public static final int HASH_ITERATIONS = 1;

        public static String md5String(String sourceStr) {
            SimpleHash simpleHash = new SimpleHash("MD5", sourceStr, SALT, HASH_ITERATIONS);
            System.err.println(simpleHash.toString());
            return simpleHash.toString();
        }
    }

}
