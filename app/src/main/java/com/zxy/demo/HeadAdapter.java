package com.zxy.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxy on 2018/10/12 18:08.
 */

public class HeadAdapter extends BaseAdapter
{
    public List<String> mList = new ArrayList<>(Arrays.asList("1111111111111111111", "222222222222222", "3333333333"));

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
        View view;
        view = convertView;
        if (convertView == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(mList.get(position)+(position!=0?"":"卡萨\n帝景看见企鹅及\n其阿胶起哦阿\n胶企鹅减轻饥饿前饿哦j\no前精品景区"));
        return view;
    }
}
