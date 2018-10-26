package com.zxy.view_pull_to_refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/10/26 12:01.
 */

public class HeaderLoadingView extends PullToRefreshView.LoadingView
{
    public HeaderLoadingView(@NonNull Context context)
    {
        this(context,null);
    }

    public HeaderLoadingView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(R.layout.view_image_header, this,true);
    }

    @Override
    public void showViewState(PullToRefreshView.State state)
    {
        ImageView iv = (ImageView)findViewById(R.id.iv);
        String s = "";
        switch (state)
        {
            case PULL_TO_REFRESH:
                s = "加载更多";
                iv.setImageResource(R.drawable.ic_pull_refresh_normal);
                break;
            case RELEASE_TO_REFRESH:
                s = "松开加载";
                iv.setImageResource(R.drawable.ic_pull_refresh_ready);
                break;
            case REFRESHING:
                s = "正在加载...";
                iv.setImageResource(R.drawable.ic_pull_refresh_refreshing);
                PullToRefreshView.startAnimationDrawable(iv.getDrawable());
                break;
            case FINISH:
                s = "加载完成";
                iv.setImageResource(R.drawable.ic_pull_refresh_normal);
                break;
        }
    }
}
