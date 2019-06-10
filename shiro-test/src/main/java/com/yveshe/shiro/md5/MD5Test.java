/**
 * Copyright:   Copyright (c)2016
 * Company:     YvesHe
 * @version:    1.0
 * Create at:   2019年5月29日
 * Description:
 *
 * Author       YvesHe
 */
package com.yveshe.shiro.md5;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 测试Shiro中散列算法MD5的使用
 *
 * @author YvesHe
 *
 */
public class MD5Test {

    public static void main(String[] args) {

        // 1. md5加密，不加盐
        String password_md5_nosalt = new Md5Hash("yveshepasswd").toString();
        System.out.println("password_md5_nosalt: " + password_md5_nosalt);// 112bbea27e6fc806bfcb1caedc5356c4

        // 2. md5加密，加盐，一次散列
        String password_md5_sale_1 = new Md5Hash("yveshepasswd", "salt", 1).toString();
        System.out.println("password_md5_sale_1: " + password_md5_sale_1);// 89c84faf38549a16d4bbeea258208815

        // 两次散列相当于md5(md5())
        String password_md5_sale_2 = new Md5Hash("yveshepasswd", "salt", 2).toString();
        System.out.println("password_md5_sale_2: " + password_md5_sale_2);// 637b1e87aa03c2704350bdc5e54da60d

        // 3.使用SimpleHash
        String algorithmName = "MD5"; // hash算法名
        Object source = "yveshepasswd"; // 待加密的源
        Object salt = "salt"; // 加密盐
        int hashIterations = 1;// 散列次数
        String simpleHash = new SimpleHash(algorithmName, source, salt, hashIterations).toString();
        System.out.println(simpleHash);// 89c84faf38549a16d4bbeea258208815
                                       // 我们可以发现与使用Md5Hash的1次散列结果保持一致

    }

}
