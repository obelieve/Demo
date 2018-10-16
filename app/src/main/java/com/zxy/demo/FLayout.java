package com.zxy.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zxy on 2018/10/16 15:52.
 */

public class FLayout extends ViewGroup
{
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    private int orientation = VERTICAL;
    private int hSpacing = 0;
    private int vSpacing = 0;

    public FLayout(Context context)
    {
        super(context);
    }

    public FLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //最终的宽、高
        int finalWidth = 0;
        int finalHeight = 0;
        //每行的宽、高
        int lineMaxWidth = 0;
        int lineMaxHeight = 0;
        //当前的位置
        int curPositionX = 0;
        int curPositionY = 0;


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            view.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width),
                    getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height));

            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            if (orientation == HORIZONTAL)
            {
                if (parentWidthMode != MeasureSpec.UNSPECIFIED && (curPositionX + childWidth + hSpacing) > parentWidthSize)
                {//水平新行
                    curPositionX = 0;
                    curPositionY += lineMaxHeight;
                    lineMaxHeight = 0;
                } else
                {
                    params.positionX = curPositionX;
                    params.positionY = curPositionY;
                    lineMaxHeight = Math.max(lineMaxHeight, childHeight + vSpacing);
                    curPositionX += childWidth + hSpacing;
                }
            } else
            {
                if (parentHeightMode != MeasureSpec.UNSPECIFIED && (curPositionY + childHeight + vSpacing) > parentHeightSize)
                {//垂直新行
                    curPositionX += lineMaxWidth;
                    curPositionY = 0;
                    lineMaxWidth = 0;
                } else
                {
                    params.positionX = curPositionX;
                    params.positionY = curPositionY;
                    lineMaxWidth = Math.max(lineMaxWidth, childWidth + hSpacing);
                    curPositionY += childHeight + vSpacing;
                }
            }
        }
        if (orientation == HORIZONTAL)
        {
            finalWidth = parentWidthSize;
            finalHeight = curPositionY - hSpacing;
        } else
        {
            finalWidth = curPositionX - vSpacing;
            finalHeight = parentHeightSize;
        }
        setMeasuredDimension(resolveSize(finalWidth, widthMeasureSpec), resolveSize(finalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            view.layout(params.positionX, params.positionY, params.positionX + view.getMeasuredWidth(), params.positionY + view.getMeasuredHeight());
        }
    }

    /**
     * 需要判断这个为false 才能调用generateLayoutParams重新赋值，才会生成FLayout.LayoutParams
     *
     * @param p
     * @return
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    /**
     * 用于子View的布局参数更改为FLayout.LayoutParams
     *
     * @return
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends ViewGroup.LayoutParams
    {

        private int positionX, positionY;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
        }

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }


        public void setPositionX(int positionX)
        {
            this.positionX = positionX;
        }

        public void setPositionY(int positionY)
        {
            this.positionY = positionY;
        }

        public int getPositionX()
        {
            return positionX;
        }

        public int getPositionY()
        {
            return positionY;
        }
    }

}
