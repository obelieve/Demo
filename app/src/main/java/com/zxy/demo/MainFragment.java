package com.zxy.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/8/14 16:11.
 */

public class MainFragment extends Fragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.ok:
                LogUtil.e("frag:ok");
                break;
            case R.id.three:
                LogUtil.e("frag:three");
                break;
            case R.id.four:
                LogUtil.e("frag:four");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.frag,menu);
    }
}
