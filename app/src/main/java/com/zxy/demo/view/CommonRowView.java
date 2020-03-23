package com.zxy.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;

/**
 * Created by zxy on 2018/10/30 15:24.
 */

public class CommonRowView extends FrameLayout
{
    private ImageView iv;
    private TextView tvTitle, tvContent;

    private int mIvResId;
    private String mTvTitleText, mTvContentText;

    private int mIvVisibility,mTvTitleVisibility,mTvContentVisibility;
    private int mTvTitleTextColor,mTvContentTextColor;
    public CommonRowView(Context context)
    {
        this(context, null);
    }

    public CommonRowView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.view_common_row, this, true);
        iv = findViewById(R.id.iv_head);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CommonRowView);
        mIvResId = a.getResourceId(R.styleable.CommonRowView_ivResource,0);
        mTvTitleText = a.getString(R.styleable.CommonRowView_tvTitleText);
        mTvContentText = a.getString(R.styleable.CommonRowView_tvContentText);
        mIvVisibility = a.getInt(R.styleable.CommonRowView_ivVisibility,-1);
        mTvTitleVisibility = a.getInt(R.styleable.CommonRowView_tvTitleVisibility,-1);
        mTvContentVisibility = a.getInt(R.styleable.CommonRowView_tvContentVisibility,-1);
        mTvTitleTextColor = a.getInt(R.styleable.CommonRowView_tvTitleTextColor, -1);
        mTvContentTextColor = a.getInt(R.styleable.CommonRowView_tvContentTextColor,-1);
        a.recycle();

        if(mIvResId!=0){
            iv.setImageResource(mIvResId);
        }
        if(!TextUtils.isEmpty(mTvTitleText)){
            tvTitle.setText(mTvTitleText);
        }
        if(!TextUtils.isEmpty(mTvContentText)){
            tvContent.setText(mTvContentText);
        }
        if(mIvVisibility!=-1){
            iv.setVisibility(mIvVisibility);
        }
        if(mTvTitleVisibility!=-1){
            tvTitle.setVisibility(mTvTitleVisibility);
        }
        if(mTvContentVisibility!=-1){
            tvContent.setVisibility(mTvContentVisibility);
        }
        if(mTvTitleTextColor!=-1){
            tvTitle.setTextColor(mTvTitleTextColor);
        }
        if(mTvContentTextColor!=-1){
            tvContent.setTextColor(mTvContentTextColor);
        }
    }
}
