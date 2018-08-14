package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zxy.utility.LogUtil;

/**
 * 查看Activity 和 Fragment 菜单状态叠加
 * 其中Fragment需要设置 setHasOptionsMenu(true);
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content,new MainFragment(),"MainFragment")
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.act,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.ok:
                LogUtil.e("act:ok");
                break;
            case R.id.one:
                LogUtil.e("act:one");
                break;
            case R.id.two:
                LogUtil.e("act:two");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
