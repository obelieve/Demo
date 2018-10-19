package com.zxy.view_touch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

/**
 * Created by zxy on 2018/10/17 10:33.
 * 事件组成：
 * 按下(DOWN)->移动(MOVE)->抬起(UP)
 *                      ->撤销(CANCEL)
 * 事件分发传递过程：
 * 主页面(Activity) -> 父控件(DecorView->FrameLayout->ViewGroup) - > 子控件(View)
 * 事件分发的3个方法：
 * 分发事件方法(dispatchTouchEvent();)
 * 处理点击事件方法(onTouchEvent();)
 * 拦截事件方法(onInterceptTouchEvent();)
 *
 * 传递细节：
 *
 * 注意情况：【分情况去看源码执行过程：1.View中不接收事件；2.ViewGroup1中接收DOWN返回true，查看执行过程；3.View中接收DOWN返回true;】
 * DOWN没View接收，mFirstTouchTarget ==null 说明没有View接收，后续事件不会分发，就直接调用Acitivity的onTouchEvent方法；
 * 1.DOWN到哪个子View接收，下次MOVE/UP事件就传到哪里不会继续往下个View的子View下发；
 * 2.如果DOWN之后，后续事件View不接收，直接传到Activity.onTouchEvent；
 * 3.如果DOWN被子View接收后，其他MOVE、UP事件被它的父控件拦截，子View收到一次CANCEL事件并且后续MOVE、UP事件就不会分发到子View；
 *
 * 主页面(Activity)->分发(dispatchTouchEvent())-->窗口的分发方法(getWindow().superDispatchTouchEvent(ev))
 *                                               ->return 调用顶级View方法(mDecorView.superDispatchTouchEvent(ev)) (DecorView ->FrameLayout ->ViewGroup)
 *                                                        ->顶级View超类方法(super.dispatchTouchEvent(ev))【父控件分发方法ViewGroup.dispatchTouchEvent(ev)】
 *
 *                                               ->return false->主页面处理事件方法onTouchEvent(ev);【主页面处理点击方法Activity.onTouchEvent(ev)】
 *                                                               ->如果在窗口边界外部就finish()，返回true;
 *                                                               ->返回 false;
 * 父控件(ViewGroup)->条件(可以撤销拦截(disallowIntercept ) 或 !onInterceptTouchEvent(ev)【父控件拦截方法ViewGroup.onInterceptTouchEvent(ev)】)
 *                   ->返回 true ->遍历子View(如果是正在点击的View)
 *                          ->调用子控件的分发方法(child.dispatchTouchEvent(ev))【子控件分发方法View.dispatchTouchEvent(ev)】
 *                   ->false->super.dispatchTouchEvent(ev)【父控件父类分发方法 ViewGroup->View.dispatchTouchEvent(ev)】
 *
 * 子控件(View)->条件(mTouchListener!=null&&mTouchListener.onTouch()&&ENABLE)【子控件onTouch()方法 View.TouchListener.onTouch()】
 *            ->直接结束返回 return true;
 *            ->返回 return onTouchEvent(ev);【子控件处理点击方法onTouchEvent(ev)】
 *
 *
 * 处理点击事件方法onTouchEvent(ev):
 * 流程->条件(DISABLE)
 *      ->true 直接返回 CLICKABLE || LONG_CLICKABLE
 *      ->false
 *
 *      (ENABLE)
 *      条件(mTouchDelegate !=null)
 *      ->mTouchDelegate.onTouchEvent(event)?return true; : null;
 *      ->false
 *      条件(CLICKABLE||LONG_CLICKABLE)
 *      ->true UP事件执行 performClick();
 *                      ->条件(mOnClickListener!=null)
 *                      ->mOnClickListener.onClick();【调用onClick()方法】
 *                      ->直接返回(mOnClickListener==null);
 *      (ENABLE,但不可点击返回 false)
 *      ->false
 *
 */

public class TouchViewActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_view);
        LogUtil.e();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        LogUtil.e(MotionEventUtil.name(ev));
        boolean  b = super.dispatchTouchEvent(ev);
        LogUtil.e(b+" ==="+MotionEventUtil.name(ev));
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
//        LogUtil.e(MotionEventUtil.name(event));
//        if (event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            LogUtil.e("点击");
//            return true;
//        }
//        return false;
        LogUtil.e(MotionEventUtil.name(event));
        boolean b= super.onTouchEvent(event);
        LogUtil.e(b+" ==="+MotionEventUtil.name(event));
        return b;
    }
}
