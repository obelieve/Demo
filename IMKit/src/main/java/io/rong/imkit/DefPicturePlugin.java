package io.rong.imkit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;

/**
 * 会话插件-照片
 * Created by zxy on 2018/12/13 14:22.
 */

public class DefPicturePlugin extends ImagePlugin implements IPluginModule
{
    @Override
    public Drawable obtainDrawable(Context context)
    {
        return ContextCompat.getDrawable(context,R.drawable.ic_picture_plugin);
    }

    @Override
    public String obtainTitle(Context context)
    {
        return context.getString(R.string.picture_plugin);
    }
}
