package com.zxy.view_computescroll;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/10/20 17:35.
 */

public class ScrollActivity extends Activity
{

    ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        lv = (ListView) findViewById(R.id.lv);
        String[] s = new String[30];
        int i = 0;
        while (i < 30)
        {
            s[i] = "i:" + i;
            i++;
        }
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s));
    }
}
