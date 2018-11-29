package com.zxy.view_one;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bigkoo.pickerview.lib.WheelView;
import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zxy on 2018/11/29 15:23.
 */

public class WheelView2 extends WheelView
{
    public static final String TEXT_COLOR_OUT = "#8F7213";
    public static final String TEXT_COLOR_CENTER = "#FEE204";
    public static final String DIVIDER_COLOR = "#00000000";
    public static final int TEXT_SIZE = 24;

    public WheelView2(Context context)
    {
        super(context);
    }

    public WheelView2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initField(attrs);
    }

    private void initField(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WheelView2, 0, 0);
        int textColorOut = a.getColor(R.styleable.WheelView2_textColorOut, Color.parseColor(TEXT_COLOR_OUT));
        int textColorCenter = a.getColor(R.styleable.WheelView2_textColorCenter, Color.parseColor(TEXT_COLOR_CENTER));
        int dividerColor = a.getColor(R.styleable.WheelView2_dividerColor, Color.parseColor(DIVIDER_COLOR));
        int textSize = a.getDimensionPixelSize(R.styleable.pickerview_pickerview_textSize, dp2px(TEXT_SIZE));
        a.recycle();
        try
        {
            Field textColorOutField = getClass().getSuperclass().getDeclaredField("textColorOut");
            textColorOutField.setAccessible(true);
            textColorOutField.set(this, textColorOut);
            int co = (int)textColorOutField.get(this);
            LogUtil.e("co:"+co);
            Field textColorCenterField = getClass().getSuperclass().getDeclaredField("textColorCenter");
            textColorCenterField.setAccessible(true);
            textColorCenterField.set(this, textColorCenter);

            Field dividerColorField = getClass().getSuperclass().getDeclaredField("dividerColor");
            dividerColorField.setAccessible(true);
            dividerColorField.set(this, dividerColor);

            Field textSizeField = getClass().getSuperclass().getDeclaredField("textSize");
            textSizeField.setAccessible(true);
            textSizeField.set(this, textSize);

            Method method = getClass().getSuperclass().getDeclaredMethod("initLoopView",new Class[]{Context.class});
            method.setAccessible(true);
            method.invoke(this,getContext());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int dp2px(int size)
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * size);
    }

}
