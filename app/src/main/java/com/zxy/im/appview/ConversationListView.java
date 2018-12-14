package com.zxy.im.appview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.zxy.im.R;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

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
        final Uri uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();
        final ConversationListFragment fragment = new ConversationListFragment();
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                fragment.setUri(uri);
                ((FragmentActivity) getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fl_content,fragment, ConversationListFragment.class.getSimpleName()).commit();
            }
        },1000);
    }


}
