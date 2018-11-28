package com.zxy.view_one;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2018/11/28 16:53.
 */

public class WheelSelectListView extends FrameLayout
{
    ListView mListView;

    public WheelSelectListView(@NonNull Context context)
    {
        super(context);
        init();
    }

    public WheelSelectListView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public WheelSelectListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_wheel_select_list, this, true);
        mListView = view.findViewById(R.id.lv);
        mListView.setAdapter(new LvAdapter());
    }


    public static class LvAdapter extends BaseAdapter
    {

        private List<String> mList = new ArrayList<>();
        public LvAdapter()
        {
            mList.add("1");
            mList.add("2");
            mList.add("3");
            mList.add("4");
            mList.add("5");
        }

        @Override
        public int getCount()
        {
            return mList.size();
        }


        @Override
        public String getItem(int position)
        {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            if (view == null)
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item, parent, false);
            }
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(mList.get(position));
            return view;
        }
    }
}
