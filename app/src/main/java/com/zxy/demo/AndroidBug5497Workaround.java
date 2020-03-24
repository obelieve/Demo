package com.zxy.demo;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.zxy.utility.LogUtil;

public class AndroidBug5497Workaround {
    public static void assistActivity(View content) {
        new AndroidBug5497Workaround(content);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(View content) {
        if (content != null) {
            mChildOfContent = content;
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
            LogUtil.e("fixedHeight ResizeHeight:"+frameLayoutParams.height);
        }
    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

    public void fixedHeight(View contentView,boolean fixed){
        LinearLayout ll_content = contentView.findViewById(R.id.ll_content);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ll_content.getLayoutParams();

        params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
        if (fixed) {
            params.height = contentView.getHeight() - Util.dp2px(contentView.getContext(),210.0f);
        } else {
            params.height = contentView.getHeight();
        }
        LogUtil.e("fixedHeight: content-height= "+contentView.getHeight()+" ll-height: "+params.height);
        ll_content.setLayoutParams(params);
    }
}