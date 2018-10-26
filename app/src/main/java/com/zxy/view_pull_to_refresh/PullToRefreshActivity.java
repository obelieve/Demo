package com.zxy.view_pull_to_refresh;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2018/10/23 16:11.
 */

public class PullToRefreshActivity extends Activity
{

    ListView lv_content;
    List<String> mList = new ArrayList<>();
    int i = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtil.e();
        setContentView(R.layout.activity_pull_to_refresh);
        LogUtil.e();
        lv_content = (ListView) findViewById(R.id.lv_content);
        lv_content.setAdapter(new Adapter());
        final PullToRefreshView ptrv = (PullToRefreshView) findViewById(R.id.ptrv);
        ptrv.setMode(PullToRefreshView.Mode.BOTH);
        HeaderLoadingView headerView = new HeaderLoadingView(this);
        ptrv.setHeaderView(headerView);
        ptrv.setOnRefreshListener(new PullToRefreshView.OnRefreshListener()
        {
            @Override
            public void onRefreshFinish()
            {

                lv_content.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mList.clear();
                        i = 0;
                        final int ok = i + 10;
                        while (i < ok)
                        {
                            mList.add("i:" + i);
                            i++;
                        }
                        ((Adapter) lv_content.getAdapter()).notifyDataSetChanged();
                        ptrv.stopRefreshing();
                    }
                }, 1500);
            }

            @Override
            public void onLoadMoreFinish()
            {
                lv_content.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final int ok = i + 10;
                        while (i < ok)
                        {
                            mList.add("i:" + i);
                            i++;
                        }
                        ((Adapter) lv_content.getAdapter()).notifyDataSetChanged();
                        ptrv.stopRefreshing();
                    }
                }, 1500);
            }
        });
    }

    public class Adapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return mList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
                convertView = LayoutInflater.from(PullToRefreshActivity.this).inflate(R.layout.list_item, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(mList.get(position));
            return convertView;
        }
    }
}
