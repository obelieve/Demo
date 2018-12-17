package io.rong.imkit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.plugin.DefaultLocationPlugin;

/**会话插件-位置
 * Created by zxy on 2018/12/13 14:23.
 */

public class DefLocationPlugin extends DefaultLocationPlugin
{
    @Override
    public Drawable obtainDrawable(Context context)
    {
        return ContextCompat.getDrawable(context,R.drawable.ic_location_plugin);
    }

    @Override
    public String obtainTitle(Context context)
    {
        return context.getString(R.string.location_plugin);
    }

}
