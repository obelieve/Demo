package com.zxy.frame.utility;

import android.content.Context;

/**
 * Created by zxy on 2018/12/14 11:49.
 */

public class UContext
{
    private static Context mContext;

    public static void init(Context con){
        mContext = con.getApplicationContext();
    }

    public static Context getContext()
    {
        return mContext;
    }
}
