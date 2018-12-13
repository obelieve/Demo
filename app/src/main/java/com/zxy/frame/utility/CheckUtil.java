package com.zxy.frame.utility;

/**
 * Created by zxy on 2018/8/13 11:33.
 */

public class CheckUtil
{
    public static void checkNotNull(Object obj)
    {
        if (obj == null)
        {
            throw new NullPointerException("class object:" + obj.getClass().getName() + "is null!");
        }
    }

    public static boolean isEmpty(String text)
    {
        if (text == null || text.equals(""))
        {
            return true;
        } else
        {
            return false;
        }
    }
}
