package com.zxy.im.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.zxy.im.R;

/**
 * Created by zxy on 2018/12/12 15:13.
 */

public class ConversationListView extends FrameLayout
{
    public ConversationListView(@NonNull Context context)
    {
        super(context);
        init();
    }

    public ConversationListView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ConversationListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_list,this,true);
    }
}
