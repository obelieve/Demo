package com.zxy.mall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxy.mall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingView extends FrameLayout {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;

    private int mSizeDp = 15;

    public RatingView(Context context) {
        this(context, null, 0);

    }

    public RatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rating, this, true);
        ButterKnife.bind(this, view);
        int i=5;
        float density = context.getResources().getDisplayMetrics().density;
        while (i>0){
            ImageView iv =new ImageView(getContext());
            iv.setLayoutParams(new LinearLayout.LayoutParams((int)(density*mSizeDp),(int)(density*mSizeDp)));
            iv.setImageResource(R.drawable.star_off);
            mLlContent.addView(iv);
            i--;
        }
    }

    /**
     *
     * @param rating  0~100
     */
    public void setRating(int rating){
        float percent = (rating/100.0f)*5;
        int p = Math.round(percent);
        //if(p==0.5)
    }
}
