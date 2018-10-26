package com.zxy.view_pull_to_refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

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

    private ViewState mHeaderViewState;
    private ViewState mFooterViewState;


    //Scroller
    private Scroller mScroller;
    private int mScrollLastY;

    //初始值
    private int mHeaderDefTop;
    private int mFooterDefTop;

    //拦截消费
    private boolean mIsInterrupt;
    private boolean mIsConsume;

    //X,Y坐标
    private float mDownX, mDownY;
    private float mLastX, mLastY;
    private float mCurrentX, mCurrentY;

    private int mTouchSlop;

    private OnRefreshListener mOnRefreshListener;

    public void setMode(Mode mode)
    {
        mMode = mode;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener)
    {
        mOnRefreshListener = onRefreshListener;
    }

    public PullToRefreshView(Context context)
    {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (getChildCount() != 1)
        {
            throw new RuntimeException("child view count of XML is only one !");
        }
        setDefHeaderView();
        setDefFooterView();
        addView(mHeaderView, 0);
        addView(mFooterView, 2);

        mHeaderView = getChildAt(0);
        mContentView = getChildAt(1);
        mFooterView = getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < getChildCount(); i++)
        {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            maxWidth = Math.max(maxWidth, getChildAt(i).getMeasuredWidth());
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight = getPaddingTop() + getPaddingBottom() + getChildAt(1).getMeasuredHeight();

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
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int top = getPaddingTop();

        View headerView = getChildAt(0);
        View contentView = getChildAt(1);
        View footerView = getChildAt(2);

        /**但ListView 测试相同
         * getHeight() 和getMeasureHeight() 区别：
         * getHeight()获取屏幕中显示的高度，如果View高度大于屏幕剩下的就没显示。getMeasureHeight()获取View原始高度
         */
        final int headerTop = top - headerView.getMeasuredHeight();
        final int contentTop = top;
        final int footerTop = getHeight() - getPaddingBottom();

        headerView.layout(left, headerTop, right + headerView.getMeasuredWidth(), headerTop + headerView.getMeasuredHeight());
        contentView.layout(left, contentTop, right + contentView.getMeasuredWidth(), contentTop + contentView.getMeasuredHeight());
        footerView.layout(left, footerTop, right + footerView.getMeasuredWidth(), footerTop + footerView.getMeasuredHeight());
        mFooterDefTop = footerTop;
        mHeaderDefTop = headerTop;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            mDownX = ev.getRawX();
            mDownY = ev.getRawY();
        }
        mLastX = mCurrentX;
        mLastY = mCurrentY;
        mCurrentX = ev.getRawX();
        mCurrentY = ev.getRawY();

        mIsInterrupt = canPull();
        return mIsInterrupt;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mLastX = mCurrentX;
        mLastY = mCurrentY;
        mCurrentX = event.getRawX();
        mCurrentY = event.getRawY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mIsConsume)
                {
                    canConsume();
                } else
                {
                    mIsConsume = canPull();
                }
                return mIsConsume;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //reset touch state
                if (mIsConsume)
                {
                    if (mState == State.RELEASE_TO_REFRESH)
                    {
                        mState = State.REFRESHING;
                    }
                    switch (mDirection)
                    {
                        case HEADER:
                            releasePull(Direction.HEADER, mHeaderDefTop, mHeaderView.getTop(), mHeaderDefTop + mHeaderView.getMeasuredHeight());
                            break;
                        case FOOTER:
                            releasePull(Direction.FOOTER, mFooterDefTop, mFooterView.getTop(), mFooterDefTop - mFooterView.getMeasuredHeight());
                            break;
                    }
                }
                mIsConsume = false;
                mIsInterrupt = false;
                return true;
            default:
                break;
        }
        return false;
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

    private void canConsume()
    {
        int eventDelta = (int) ((mCurrentY - mLastY) * DELTA_SCALE_VALUE);
        if (eventDelta == 0)
            return;
        if (mDirection == Direction.NONE)
            throw new RuntimeException("Direction error, must isn't  Direction.NONE !");
        if (mDirection == Direction.HEADER)
        {
            ViewCompat.offsetTopAndBottom(mHeaderView, eventDelta);
            if (mHeaderView.getTop() >= mHeaderDefTop + mHeaderView.getMeasuredHeight())
            {
                setState(State.RELEASE_TO_REFRESH);
            } else
            {
                setState(State.PULL_TO_REFRESH);
            }
        } else
        {
            ViewCompat.offsetTopAndBottom(mFooterView, eventDelta);
            if (mFooterView.getTop() < mFooterDefTop - mFooterView.getMeasuredHeight())
            {
                setState(State.RELEASE_TO_REFRESH);
            } else
            {
                setState(State.PULL_TO_REFRESH);
            }
        }
        ViewCompat.offsetTopAndBottom(mContentView, eventDelta);
    }

    private void releasePull(Direction direction, int defTop, int curTop, int refreshingTop)
    {
        switch (direction)
        {
            case HEADER:
                if (curTop < refreshingTop)
                {
                    setState(State.FINISH);
                    startYOffsetScroll(curTop - defTop);
                } else if (curTop > refreshingTop)
                {
                    setState(State.REFRESHING);
                    startYOffsetScroll(curTop - refreshingTop);
                } else
                {
                    setState(State.REFRESHING);
                }
                break;
            case FOOTER:
                if (curTop > refreshingTop)
                {
                    setState(State.FINISH);
                    startYOffsetScroll(curTop - defTop);//小于临界值
                } else if (curTop < refreshingTop)
                {
                    setState(State.REFRESHING);
                    startYOffsetScroll(curTop - refreshingTop);//大于临界值
                } else
                {
                    setState(State.REFRESHING);//到达临界值
                }
                break;
        }
    }

    private void startYOffsetScroll(int dy)
    {
        mScroller.startScroll(0, 0, 0, dy, 500);
        invalidateRefreshView();
    }

    public void setState(State state)
    {

        mState = state;
        if (mDirection == Direction.HEADER)
        {
            if (mHeaderViewState != null)
                mHeaderViewState.showViewState(state);
        } else if (mDirection == Direction.FOOTER)
        {
            if (mFooterViewState != null)
                mFooterViewState.showViewState(state);
        }
    }

    public static void startAnimationDrawable(Drawable drawable)
    {
        if (drawable instanceof AnimationDrawable)
        {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            if (!animationDrawable.isRunning())
            {
                animationDrawable.start();
            }
        }
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {

            if (mDirection == Direction.HEADER)
            {
                ViewCompat.offsetTopAndBottom(mHeaderView, mScrollLastY - mScroller.getCurrY());
                ViewCompat.offsetTopAndBottom(mContentView, mScrollLastY - mScroller.getCurrY());
            } else if (mDirection == Direction.FOOTER)
            {
                ViewCompat.offsetTopAndBottom(mFooterView, mScrollLastY - mScroller.getCurrY());
                ViewCompat.offsetTopAndBottom(mContentView, mScrollLastY - mScroller.getCurrY());
            }
            mScrollLastY = mScroller.getCurrY();
            if (mScroller.isFinished() && mOnRefreshListener != null)
            {
                mScrollLastY = 0;
                if (mState == State.REFRESHING)
                {
                    if (mDirection == Direction.HEADER)
                    {
                        mOnRefreshListener.onRefreshFinish();
                    } else
                    {
                        mOnRefreshListener.onLoadMoreFinish();
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
        int hTop = mHeaderDefTop - mHeaderView.getTop();
        int cTop = mHeaderDefTop + mHeaderView.getMeasuredHeight() - mContentView.getTop();
        int fTop = mFooterDefTop - mFooterView.getTop();
        ViewCompat.offsetTopAndBottom(mHeaderView, hTop);
        ViewCompat.offsetTopAndBottom(mContentView, cTop);
        ViewCompat.offsetTopAndBottom(mFooterView, fTop);
        mState = State.RESET;
        mDirection = Direction.NONE;
    }

    interface OnRefreshListener
    {
        void onRefreshFinish();

        void onLoadMoreFinish();
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

    /**
     * 显示视图状态
     */
    public interface ViewState
    {
        void showViewState(State state);
    }


    public void setHeaderView(View view, ViewState state)
    {
        if (view != null)
        {
            if (mHeaderView != null)
            {
                removeView(mHeaderView);
                addView(view, 0);
            }
            mHeaderView = view;
            mHeaderViewState = state;
        }
    }

    public void setFooterView(View view, ViewState state)
    {
        if (view != null)
        {
            if (mFooterView != null)
            {
                removeView(mFooterView);
                addView(view, 2);
            }
            mFooterView = view;
            mFooterViewState = state;
        }
    }

    private View headerView()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_text_loading, this, false);
        return view;
    }

    private View footerView()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_image_loading, this,false);
        return view;
    }

    private void setDefHeaderView()
    {
        setHeaderView(headerView(), new ViewState()
        {
            @Override
            public void showViewState(State state)
            {
                TextView tv = (TextView) mHeaderView.findViewById(R.id.tv);
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
                LogUtil.e("State:" + state + " " + s);
                tv.setText(s);
            }
        });
    }

    private void setDefFooterView()
    {
        setFooterView(footerView(), new ViewState()
        {
            @Override
            public void showViewState(State state)
            {
                ImageView iv = (ImageView) mFooterView.findViewById(R.id.iv);
                String s = "";
                switch (state)
                {
                    case PULL_TO_REFRESH:
                        s = "加载更多";
                        iv.setImageResource(R.drawable.ic_pull_refresh_normal);
                        break;
                    case RELEASE_TO_REFRESH:
                        s = "松开加载";
                        iv.setImageResource(R.drawable.ic_pull_refresh_ready);
                        break;
                    case REFRESHING:
                        s = "正在加载...";
                        iv.setImageResource(R.drawable.ic_pull_refresh_refreshing);
                        startAnimationDrawable(iv.getDrawable());
                        break;
                    case FINISH:
                        s = "加载完成";
                        iv.setImageResource(R.drawable.ic_pull_refresh_normal);
                        break;
                }
            }
        });
    }
}
