package com.zxy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自适应宽度 RecyclerView.LayoutManager
 * Created by zxy on 2019/2/12 11:57.
 */
public class AutoFixWidthLayoutManager extends RecyclerView.LayoutManager
{
    public AutoFixWidthLayoutManager()
    {

    }

    @Override
    public boolean isAutoMeasureEnabled()
    {
        //必须，防止recyclerview高度为wrap时测量item高度0
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams()
    {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        detachAndScrapAttachedViews(recycler);
        int parentWidth = getWidth();
        int curWidth = 0;//当前行，已经填充的宽度
        int curHeight = 0;//当前容易，已经填充的高度
        int lastHeight = 0;//最后的高度
        for (int i = 0; i < getItemCount(); i++)
        {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int viewWidth = getDecoratedMeasuredWidth(view);
            int viewHeight = getDecoratedMeasuredHeight(view);
            curWidth += viewWidth;
            if (curWidth <= parentWidth)
            {//当前行
                int l = view.getPaddingLeft() + curWidth - viewWidth;
                int t = view.getPaddingTop() + curHeight;
                int r = view.getPaddingRight() + curWidth;
                int b = view.getPaddingBottom() + curHeight + viewHeight;
                layoutDecorated(view, l, t, r, b);
                lastHeight = curHeight + viewHeight;
            } else
            {//新行
                int l = view.getPaddingLeft();
                int t = view.getPaddingTop() + lastHeight;
                int r = view.getPaddingRight() + viewWidth;
                int b = view.getPaddingBottom() + lastHeight + viewHeight;
                layoutDecorated(view, l, t, r, b);
                curWidth = viewWidth;
                curHeight = lastHeight;
            }
        }
    }
}