package com.github.obelieve.community.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.obelieve.community.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin
 * on 2020/9/1
 */
public class EmptyView extends FrameLayout {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    public EmptyView(@NonNull Context context, String tips) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        init(context,tips);
    }

    private void init(Context context,String tips) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, this, true);
        ButterKnife.bind(this,view);
        tvEmpty.setText(tips);
    }

    public void setTips(String tips) {
        tvEmpty.setText(tips);
    }
}
