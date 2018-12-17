package io.rong.imkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.plugin.IPluginModule;

/**
 * 会话插件-转账
 * Created by zxy on 2018/12/13 11:18.
 */

public class DefTransferAccountsPlugin implements IPluginModule
{
    @Override
    public Drawable obtainDrawable(Context context)
    {
        return ContextCompat.getDrawable(context, R.drawable.ic_transfer_accounts);
    }

    @Override
    public String obtainTitle(Context context)
    {
        return context.getResources().getString(R.string.transfer_accounts_plugin);
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
