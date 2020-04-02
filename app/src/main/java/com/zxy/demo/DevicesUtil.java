package com.zxy.demo;

/**
 * @author: hgb
 * @createTime: 2019/9/27
 * @description:
 * @changed by:
 */
public class DevicesUtil {


    /**
     * 获取唯一标识idfa
     *
     * @return
     */
    /**
     * 获取Oaid
     *
     * @return oaid或错误码
     */
    public static String getOaid() {
        String idfa;
        if (MyApplication.isSupportOaid()) {
            idfa = MyApplication.getOaid();
        } else {
            idfa = "获取失败，ErrorCode: " + MyApplication.getErrorCode();
        }
        return idfa;
    }


}
