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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
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
public class CustomRealmFromCache extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("CustomRealm111");
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
        if (!DataBaseUtil.containUser(userName)) {// 用户不存在
            return null;
        }

        // 3.构建认证信息
        String passwd = DataBaseUtil.getPasswd(userName);
        Object principal = userName; // 用户名称
        Object hashedCredentials = passwd; // 加密后的密码
        ByteSource credentialsSalt = ByteSource.Util.bytes(MD5Util.SALT); // 加密盐
        String realmName = this.getName();// 领域名称?
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
        return info;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalcollection) {
        String userName = (String) principalcollection.getPrimaryPrincipal();
        List<String> permissionList = DataBaseUtil.getPermissions(userName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        // info.setRoles(roles);//此处没有模拟角色信息,不测试
        return info;
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

    public static class DataBaseUtil {

        /**
         * 模拟数据库中的用户信息
         */
        private static Map<String, String> user_passwd_map = new HashMap<String, String>();
        static {
            user_passwd_map.put("yveshe", MD5Util.md5String("yveshepasswd"));
            user_passwd_map.put("yves", MD5Util.md5String("yvespasswd"));
            user_passwd_map.put("jack", MD5Util.md5String("jackpasswd"));
        }

        public static String getPasswd(String userName) {
            return user_passwd_map.get(userName);
        }

        public static boolean containUser(String userName) {
            return user_passwd_map.containsKey(userName);
        }

        // ///////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * 模拟数据库中的角色信息
         */
        private static Map<String, List<String>> user_permission_map = new HashMap<String, List<String>>();
        private static final List<String> PERMISSION_LIST = new ArrayList<String>();
        static {
            PERMISSION_LIST.add("user:create:*");
            PERMISSION_LIST.add("user:update:*");
            PERMISSION_LIST.add("user:delete:*");
            user_permission_map.put("yveshe", PERMISSION_LIST);
        }

        public static List<String> getPermissions(String userName) {
            return user_permission_map.get(userName);
        }

    }

}
