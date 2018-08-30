package com.zxy.demo.bind;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by zxy on 2018/8/30 14:11.
 */

public class Bind
{
    public static void inject(Activity activity)
    {
        Class<Activity> tClass = (Class<Activity>) activity.getClass();
        Field[] fields = tClass.getDeclaredFields();
        if (fields != null)
        {
            for (Field field : fields)
            {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if(viewInject!=null){
                    if (!field.isAccessible())
                    {
                        field.setAccessible(true);
                    }
                    try
                    {
                        field.set(activity,activity.findViewById(viewInject.value()));
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
