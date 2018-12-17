package io.rong.imkit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.widget.provider.FilePlugin;

/**
 * 会话插件-文件
 * Created by zxy on 2018/12/13 14:24.
 */

public class DefFilePlugin extends FilePlugin
{
    @Override
    public Drawable obtainDrawable(Context context)
    {
        return ContextCompat.getDrawable(context, R.drawable.ic_file_plugin);
    }

    @Override
    public String obtainTitle(Context context)
    {
        return context.getString(R.string.file_plugin);
    }

}
