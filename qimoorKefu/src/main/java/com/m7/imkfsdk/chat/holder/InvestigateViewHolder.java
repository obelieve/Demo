package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2016/3/9.
 */
public class InvestigateViewHolder extends BaseHolder {

    private LinearLayout chat_investigate_ll;

    public InvestigateViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        chat_investigate_ll = (LinearLayout) baseView.findViewById(R.id.chat_investigate_ll);
        type = 7;
        return this;
    }

    public LinearLayout getChat_investigate_ll () {
        if(chat_investigate_ll == null) {
            chat_investigate_ll = (LinearLayout) baseView.findViewById(R.id.chat_investigate_ll);
        }
        return chat_investigate_ll;
    }

}
