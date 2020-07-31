package com.zxy.demo.test.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * ssh
 * 2020/3/11
 **/
public class NumUseTTFTextView extends androidx.appcompat.widget.AppCompatTextView {
    public NumUseTTFTextView(Context context) {
        super(context);
        init();
    }

    public NumUseTTFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumUseTTFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            try{
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "num.ttf");
                setTypeface(tf);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
