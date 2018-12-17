package io.rong.imkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.plugin.IPluginModule;

/**
 * 会话插件-个人名片
 * Created by zxy on 2018/12/13 14:27.
 */

public class DefPersonalCardPlugin implements IPluginModule
{
    @Override
    public Drawable obtainDrawable(Context context)
    {
        return ContextCompat.getDrawable(context,R.drawable.ic_personal_card_plugin);
    }

    @Override
    public String obtainTitle(Context context)
    {
        return context.getString(R.string.personal_card_plugin);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension)
    {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent)
    {

    }
}
