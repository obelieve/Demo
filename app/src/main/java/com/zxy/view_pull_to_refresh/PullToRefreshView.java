package com.zxy.view_pull_to_refresh;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.zxy.utility.LogUtil;
import com.zxy.utility.MotionEventUtil;

/**
 * Created by zxy on 2018/10/23 15:52.
 */

public class PullToRefreshView extends ViewGroup
{
    private final float DELTA_SCALE_VALUE = 0.5f;

    private Mode mMode = Mode.BOTH;
    private State mState = State.RESET;
    private Direction mDirection = Direction.NONE;

    private View mHeaderView;
    private View mContentView;
    private View mFooterView;


    //Scroller
    private Scroller mScroller;

    //初始值
    private int mHeaderDefaultTop;
    private int mFooterDefaultTop;

    //拦截消费
    private boolean mIsInterrupt;
    private boolean mIsConsume;

    //判断
    private float mDownX, mDownY;
    private float mLastX, mLastY;
    private float mCurrentX, mCurrentY;

    private int mTouchSlop;

    private RefreshListener mRefreshListener;

    {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    public PullToRefreshView(Context context)
    {
        super(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setMode(Mode mode)
    {
        mMode = mode;
    }

    public void setRefreshListener(RefreshListener refreshListener)
    {
        mRefreshListener = refreshListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = 0;
        int maxHeight = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < getChildCount(); i++)
        {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            maxWidth = Math.max(maxWidth, getChildAt(i).getMeasuredWidth());
        }
        maxHeight += getChildAt(1).getMeasuredHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode)
        {
            case MeasureSpec.EXACTLY:
                maxWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (heightMode)
        {
            case MeasureSpec.EXACTLY:
                maxHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        l = getPaddingLeft();
        r = getPaddingRight();
        t = getPaddingTop();
        b = getPaddingBottom();

        View headerView = getChildAt(0);
        View contentView = getChildAt(1);
        View footerView = getChildAt(2);

        mHeaderDefaultTop = t - headerView.getMeasuredHeight();
        LogUtil.e("headerView.layout:" + "l：" + l + " t:" + (t - headerView.getMeasuredHeight()) + " r:" + (r + headerView.getMeasuredWidth()) + " b:" + t);
        headerView.layout(l, t - headerView.getMeasuredHeight(), r + headerView.getMeasuredWidth(), t);
        LogUtil.e("contentView.layout:" + "l：" + l + " t:" + t + " r:" + (r + contentView.getMeasuredWidth()) + " b:" + (b + contentView.getMeasuredHeight()));
        contentView.layout(l, t, r + contentView.getMeasuredWidth(), b + contentView.getMeasuredHeight());
        t = getHeight() - getPaddingBottom();
        mFooterDefaultTop = t;
        LogUtil.e("footerView.layout:" + "l：" + l + " t:" + t + " r:" + (r + footerView.getMeasuredWidth()) + " b:" + (b + footerView.getMeasuredHeight()));
        footerView.layout(l, t, r + footerView.getMeasuredWidth(), t + footerView.getMeasuredHeight());


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        mLastX = mCurrentX;
        mLastY = mCurrentY;

        mCurrentX = ev.getRawX();
        mCurrentY = ev.getRawY();
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            mDownX = ev.getRawX();
            mDownY = ev.getRawY();
        }
        mIsInterrupt = canPull();
        return mIsInterrupt;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e("MotionEvent:" + MotionEventUtil.name(event));
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                mCurrentX = mDownX;
                mCurrentY = mDownY;
                return true;
            case MotionEvent.ACTION_MOVE:
                mLastX = mCurrentX;
                mLastY = mCurrentY;
                mCurrentX = event.getRawX();
                mCurrentY = event.getRawY();

                if (mIsConsume)
                {
                    execConsume();
                } else
                {
                    mIsConsume = canPull();
                }
                return mIsConsume;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastX = mCurrentX;
                mLastY = mCurrentY;
                mCurrentX = event.getRawX();
                mCurrentY = event.getRawY();
                //reset touch state
                if (mIsConsume)
                {
                    pullFinish();
                }
                mIsConsume = false;
                mIsInterrupt = false;
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (getChildCount() != 1)
        {
            throw new RuntimeException("child view count of XML is only one !");
        }
        addView(headerView(), 0);
        addView(footerView(), 2);

        mHeaderView = getChildAt(0);
        mContentView = getChildAt(1);
        mFooterView = getChildAt(2);
    }


    private boolean canPull()
    {
        boolean angle = Math.toDegrees(Math.atan(Math.abs(mCurrentX - mDownX) / Math.abs(mCurrentY - mDownY))) < 30;
        boolean isScroll = Math.abs((mCurrentY - mDownY)) > mTouchSlop;

        boolean headerDirection = ((mMode == Mode.BOTH || mMode == Mode.FROM_HEADER) && (mCurrentY - mDownY) > 0 && !mContentView.canScrollVertically(-1));
        boolean footerDirection = ((mMode == Mode.BOTH || mMode == Mode.FROM_FOOTER) && (mCurrentY - mDownY) < 0 && !mContentView.canScrollVertically(1));
        if (headerDirection)
        {
            mDirection = Direction.HEADER;
        } else if (footerDirection)
        {
            mDirection = Direction.FOOTER;
        }
        boolean direction = headerDirection || footerDirection;
        boolean state = mState == State.RESET ? true : false;

        return angle && isScroll && direction && state;
    }

    private void execConsume()
    {
        int eventDelta = (int) ((mCurrentY - mLastY) * DELTA_SCALE_VALUE);
        if (eventDelta == 0)
            return;
        if (mDirection == Direction.NONE)
            throw new RuntimeException("Direction error, must isn't  Direction.NONE !");
        if (mDirection == Direction.HEADER)
        {
            ViewCompat.offsetTopAndBottom(mHeaderView, eventDelta);
            LogUtil.e("eventDelta:" + eventDelta + " headerHeight:" + mHeaderView.getMeasuredHeight() + " top:" + mHeaderView.getTop());
            if (mHeaderView.getTop() >= mHeaderDefaultTop + mHeaderView.getMeasuredHeight())
            {
                setState(State.RELEASE_TO_REFRESH);
            } else
            {
                setState(State.PULL_TO_REFRESH);
            }
        } else
        {
            ViewCompat.offsetTopAndBottom(mFooterView, eventDelta);

            if (mFooterView.getTop() < mFooterDefaultTop - mFooterView.getMeasuredHeight())
            {
                setState(State.RELEASE_TO_REFRESH);
            } else
            {
                setState(State.PULL_TO_REFRESH);
            }
        }
        ViewCompat.offsetTopAndBottom(mContentView, eventDelta);
    }

    private void pullFinish()
    {
        if (mState == State.RELEASE_TO_REFRESH)
        {
            mState = State.REFRESHING;
        }
        if (mDirection == Direction.HEADER)
        {
            int curTop = mHeaderView.getTop();
            int refreshingTop = mHeaderDefaultTop + mHeaderView.getMeasuredHeight();

            if (curTop < refreshingTop)
            {
                LogUtil.e("HEADER->RESET" + "curTop:" + curTop + "dy:" + (curTop - mHeaderDefaultTop));
                setState(State.FINISH);
                mScroller.startScroll(0, 0, 0, curTop - mHeaderDefaultTop, 500);
                invalidateRefreshView();
            } else if (curTop > refreshingTop)
            {
                setState(State.REFRESHING);
                LogUtil.e("HEADER->REFRESHING" + "curTop:" + curTop + "dy:" + (curTop - refreshingTop));
                mScroller.startScroll(0, 0, 0, curTop - refreshingTop, 500);
                invalidateRefreshView();
            } else
            {
                setState(State.REFRESHING);
            }
        } else
        {
            int curTop = mFooterView.getTop();
            int refreshingTop = mFooterDefaultTop - mFooterView.getMeasuredHeight();

            if (curTop > refreshingTop)
            {
                LogUtil.e("FOOTER->RESET" + "curTop:" + curTop + "dy:" + (curTop - mFooterDefaultTop));
                setState(State.FINISH);
                mScroller.startScroll(0, 0, 0, curTop - mFooterDefaultTop, 500);
                invalidateRefreshView();
            } else if (curTop < refreshingTop)
            {
                LogUtil.e("FOOTER->REFRESHING" + "curTop:" + curTop + "dy:" + (curTop - refreshingTop));
                setState(State.REFRESHING);
                mScroller.startScroll(0, 0, 0, curTop - refreshingTop, 500);
                invalidateRefreshView();
            } else
            {
                setState(State.REFRESHING);
            }
        }
    }

    public void setState(State state)
    {

        mState = state;
        if (mDirection == Direction.HEADER)
        {
            TextView tv = (TextView) mHeaderView;
            String s = "";
            switch (state)
            {
                case PULL_TO_REFRESH:
                    s = "下拉刷新";
                    break;
                case RELEASE_TO_REFRESH:
                    s = "松开刷新";
                    break;
                case REFRESHING:
                    s = "刷新中...";
                    break;
                case FINISH:
                    s = "刷新完成";
                    break;
            }
            tv.setText(s);
            LogUtil.e("State:" + state + " " + s);
        } else if (mDirection == Direction.FOOTER)
        {
            TextView tv = (TextView) mFooterView;
            String s = "";
            switch (state)
            {
                case PULL_TO_REFRESH:
                    s = "加载更多";
                    break;
                case RELEASE_TO_REFRESH:
                    s = "松开加载";
                    break;
                case REFRESHING:
                    s = "正在加载...";
                    break;
                case FINISH:
                    s = "加载完成";
                    break;
            }
            tv.setText(s);
            LogUtil.e("State:" + state + " " + s);
        }
    }

    private int mScrollLastY;

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {

            if (mDirection == Direction.HEADER)
            {
                LogUtil.e("mHeaderView.getTop():" + mHeaderView.getTop() + " mScroller.getCurrY():" + mScroller.getCurrY() + " dy:" + (mHeaderView.getTop() - mScroller.getCurrY()));
                ViewCompat.offsetTopAndBottom(mHeaderView, mScrollLastY - mScroller.getCurrY());
                ViewCompat.offsetTopAndBottom(mContentView, mScrollLastY - mScroller.getCurrY());
            } else if (mDirection == Direction.FOOTER)
            {
                LogUtil.e("mFooterView.getTop():" + mFooterView.getTop() + " mScroller.getCurrY():" + mScroller.getCurrY() + " dy:" + (mFooterView.getTop() - mScroller.getCurrY()));
                ViewCompat.offsetTopAndBottom(mFooterView, mScrollLastY - mScroller.getCurrY());
                ViewCompat.offsetTopAndBottom(mContentView, mScrollLastY - mScroller.getCurrY());
            }
            mScrollLastY = mScroller.getCurrY();
            if (mScroller.isFinished() && mRefreshListener != null)
            {
                mScrollLastY = 0;
                if (mState == State.REFRESHING)
                {
                    if (mDirection == Direction.HEADER)
                    {
                        mRefreshListener.headerRefreshCompleted();
                    } else
                    {
                        mRefreshListener.footerRefreshCompleted();
                    }
                } else
                {
                    stopRefreshing();
                }
            }
            invalidateRefreshView();
        }

    }

    private void invalidateRefreshView()
    {
        postInvalidate();
    }

    public void stopRefreshing()
    {
        int hTop = mHeaderDefaultTop - mHeaderView.getTop();
        int cTop = mHeaderDefaultTop + mHeaderView.getMeasuredHeight() - mContentView.getTop();
        int fTop = mFooterDefaultTop - mFooterView.getTop();
        ViewCompat.offsetTopAndBottom(mHeaderView, hTop);
        ViewCompat.offsetTopAndBottom(mContentView, cTop);
        ViewCompat.offsetTopAndBottom(mFooterView, fTop);
        // LogUtil.e("hTop:" + hTop + " cTop:" + cTop + " fTop：" + fTop);
        LogUtil.e("top==hTop:" + mHeaderView.getTop() + " cTop:" + mContentView.getTop() + " fTop：" + mFooterView.getTop());
        mState = State.RESET;
        mDirection = Direction.NONE;

    }

    interface RefreshListener
    {
        void headerRefreshCompleted();

        void footerRefreshCompleted();
    }

    public enum Mode
    {
        BOTH,
        FROM_HEADER,
        FROM_FOOTER
    }

    public enum State
    {
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        FINISH
    }

    public enum Direction
    {
        NONE,
        HEADER,
        FOOTER
    }

    public View headerView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 48 * 3);
        TextView view = new TextView(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.CYAN);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public View footerView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 48 * 3);
        TextView view = new TextView(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.GREEN);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public View contentView()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = new View(getContext());
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
