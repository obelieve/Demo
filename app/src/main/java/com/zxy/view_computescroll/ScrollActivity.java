package com.zxy.view_computescroll;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 17:35.
 */

public class ScrollActivity extends Activity
{

    ListView lv;
    SView sv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        lv = (ListView) findViewById(R.id.lv);
        sv = (SView) findViewById(R.id.sv);
        String[] s = new String[30];
        int i = 0;
        while (i < 30)
        {
            s[i] = "i:" + i;
            i++;
        }
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s));
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                LogUtil.e("handler->执行");
                sv.mScroller.startScroll(0, 0, 0, -200,600);
                sv.invalidate();

//                ViewCompat.offsetTopAndBottom(findViewById(R.id.lv),200);
            }
        },2000);
    }
}
