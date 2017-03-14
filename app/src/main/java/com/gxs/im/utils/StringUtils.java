package com.gxs.im.utils;

import android.text.TextUtils;

/**
 * Call:vipggxs@163.com
 * Created by YT
 * String工具类
 */

public class StringUtils {
    /**
     * 校验用户名
     * @param name
     * @return
     */
    public static boolean checkUserName(String name){
        if(!TextUtils.isEmpty(name)){
            return name.matches("^[a-zA-Z]\\w{2,19}$");
        }
        return false;
    }

    /**
     * 校验密码
     * @param Pwd
     * @return
     */
    public static boolean checkPwd(String Pwd){
        if(!TextUtils.isEmpty(Pwd)){
            return Pwd.matches("^[0-9]{3,20}$");
        }
        return false;
    }

    /**
     *
     * @param contact
     * @return
     */
    public static  String getInitial(String contact){
        if (TextUtils.isEmpty(contact)){
            return contact;
        }else {
            return contact.substring(0,1).toUpperCase();
        }
    }
}
