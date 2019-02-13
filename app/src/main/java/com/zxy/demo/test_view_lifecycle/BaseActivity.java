package com.zxy.demo.test_view_lifecycle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zxy on 2019/2/13 16:51.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener
{

    private ProgressDialog mProgressDialog;

    private ObjectsHolder<ActivityLifecycleCallback> mLifecycleCallbackHolder;
    private ObjectsHolder<ActivityResultCallback> mActivityResultCallbackHolder;
    private ObjectsHolder<ActivityTouchEventCallback> mTouchEventCallbackHolder;
    private ObjectsHolder<ActivityKeyEventCallback> mKeyEventCallbackHolder;

    public Activity getActivity()
    {
        return this;
    }

    public final ObjectsHolder<ActivityLifecycleCallback> getLifecycleCallbackHolder()
    {
        if (mLifecycleCallbackHolder == null)
            mLifecycleCallbackHolder = new FStrongObjectsHolder<>(null);
        return mLifecycleCallbackHolder;
    }

    public final ObjectsHolder<ActivityResultCallback> getActivityResultCallbackHolder()
    {
        if (mActivityResultCallbackHolder == null)
            mActivityResultCallbackHolder = new FStrongObjectsHolder<>(null);
        return mActivityResultCallbackHolder;
    }

    public final ObjectsHolder<ActivityTouchEventCallback> getTouchEventCallbackHolder()
    {
        if (mTouchEventCallbackHolder == null)
            mTouchEventCallbackHolder = new FStrongObjectsHolder<>(null);
        return mTouchEventCallbackHolder;
    }

    public final ObjectsHolder<ActivityKeyEventCallback> getKeyEventCallbackHolder()
    {
        if (mKeyEventCallbackHolder == null)
            mKeyEventCallbackHolder = new FStrongObjectsHolder<>(null);
        return mKeyEventCallbackHolder;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final int layoutId = onCreateContentView();
        if (layoutId != 0)
            setContentView(layoutId);

        init(savedInstanceState);

        notifyOnCreate(savedInstanceState);
    }

    /**
     * 返回布局activity布局id，基类调用的顺序：onCreateContentView()-setContentView()-init()
     *
     * @return
     */
    protected int onCreateContentView()
    {
        return 0;
    }

    /**
     * 重写此方法初始化
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    @Override
    public void setContentView(int layoutId)
    {
        final View contentView = getLayoutInflater().inflate(layoutId, (ViewGroup) findViewById(android.R.id.content), false);
        setContentView(contentView);
    }

    @Override
    public void setContentView(View view)
    {
        final View contentView = addTitleViewIfNeed(view);
        contentView.setFitsSystemWindows(true);
        super.setContentView(contentView);

        onInitContentView(contentView);
    }

    /**
     * setContentView方法之后会回调此方法，可以用来初始化View
     *
     * @param view
     */
    protected void onInitContentView(View view)
    {

    }

    /**
     * 返回标题栏布局id
     *
     * @return
     */
    protected int onCreateTitleViewResId()
    {
        return 0;
    }

    /**
     * 为contentView添加titleView
     *
     * @param contentView
     * @return
     */
    private View addTitleViewIfNeed(View contentView)
    {
        View viewFinal = contentView;

        final int resId = onCreateTitleViewResId();
        if (resId != 0)
        {
            View titleView = getLayoutInflater().inflate(resId, (ViewGroup) findViewById(android.R.id.content), false);

            LinearLayout linAll = new LinearLayout(this);
            linAll.setOrientation(LinearLayout.VERTICAL);
            linAll.addView(titleView);
            linAll.addView(contentView);
            viewFinal = linAll;

            onInitTitleView(titleView);
        }
        return viewFinal;
    }

    /**
     * 初始化标题栏view
     *
     * @param view
     */
    protected void onInitTitleView(View view)
    {

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        notifyOnStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        notifyOnResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        notifyOnPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        notifyOnStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dismissProgressDialog();
        notifyOnDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        notifyOnSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        notifyOnRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        notifyOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev)
    {
        final Object data = getTouchEventCallbackHolder().foreachReverse(new ObjectsHolder.ForeachCallback<ActivityTouchEventCallback>()
        {
            @Override
            protected boolean next(ActivityTouchEventCallback item)
            {
                if (item.dispatchTouchEvent(BaseActivity.this, ev))
                {
                    setData(true);
                    return true;
                } else
                {
                    return false;
                }
            }
        });

        if (data != null)
        {
            // 不为null的话直接返回true，不做data为true的判断，上面已经写死了
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event)
    {
        final Object data = getKeyEventCallbackHolder().foreachReverse(new ObjectsHolder.ForeachCallback<ActivityKeyEventCallback>()
        {
            @Override
            protected boolean next(ActivityKeyEventCallback item)
            {
                if (item.dispatchKeyEvent(BaseActivity.this, event))
                {
                    setData(true);
                    return true;
                } else
                {
                    return false;
                }
            }
        });

        if (data != null)
        {
            // 不为null的话直接返回true，不做data为true的判断，上面已经写死了
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    /**
     * activity是否处于竖屏方向
     *
     * @return
     */
    public boolean isOrientationPortrait()
    {
        return Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation;
    }

    /**
     * activity是否处于横屏方向
     *
     * @return
     */
    public boolean isOrientationLandscape()
    {
        return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
    }

    /**
     * 设置activity为竖屏
     */
    public void setOrientationPortrait()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 设置activity为横屏
     */
    public void setOrientationLandscape()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void showProgressDialog(String msg)
    {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void dismissProgressDialog()
    {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    /**
     * 设置activity是否全屏
     *
     * @param fullScreen true-全屏，false-不全屏
     */
    public void setFullScreen(boolean fullScreen)
    {
        if (fullScreen)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    //------------notify callback start------------------

    private void notifyOnCreate(final Bundle savedInstanceState)
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityCreated(BaseActivity.this, savedInstanceState);
                return false;
            }
        });
    }

    private void notifyOnStart()
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityStarted(BaseActivity.this);
                return false;
            }
        });
    }

    private void notifyOnResume()
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityResumed(BaseActivity.this);
                return false;
            }
        });
    }

    private void notifyOnPause()
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityPaused(BaseActivity.this);
                return false;
            }
        });
    }

    private void notifyOnStop()
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityStopped(BaseActivity.this);
                return false;
            }
        });
    }

    private void notifyOnDestroy()
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityDestroyed(BaseActivity.this);
                return false;
            }
        });
    }

    private void notifyOnSaveInstanceState(final Bundle outState)
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivitySaveInstanceState(BaseActivity.this, outState);
                return false;
            }
        });
    }

    private void notifyOnRestoreInstanceState(final Bundle savedInstanceState)
    {
        getLifecycleCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityLifecycleCallback>()
        {
            @Override
            protected boolean next(ActivityLifecycleCallback item)
            {
                item.onActivityRestoreInstanceState(BaseActivity.this, savedInstanceState);
                return false;
            }
        });
    }

    private void notifyOnActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        getActivityResultCallbackHolder().foreach(new ObjectsHolder.ForeachCallback<ActivityResultCallback>()
        {
            @Override
            protected boolean next(ActivityResultCallback item)
            {
                item.onActivityResult(BaseActivity.this, requestCode, resultCode, data);
                return false;
            }
        });
    }

    //------------notify callback end------------------

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params)
    {
        if (params == null)
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        super.addContentView(view, params);
    }

    @Override
    public void onClick(View v)
    {

    }

    public interface ActivityLifecycleCallback
    {
        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivityDestroyed(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle outState);

        void onActivityRestoreInstanceState(Activity activity, Bundle savedInstanceState);
    }

    public interface ActivityResultCallback
    {
        void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
    }

    public interface ActivityTouchEventCallback
    {
        boolean dispatchTouchEvent(Activity activity, MotionEvent ev);
    }

    public interface ActivityKeyEventCallback
    {
        boolean dispatchKeyEvent(Activity activity, KeyEvent event);
    }

    /**
     * 对象holder
     *
     * @param <T>
     */
    public interface ObjectsHolder<T>
    {
        /**
         * 添加对象
         *
         * @param object
         * @return true-调用此方法后对象被添加了
         */
        boolean add(T object);

        /**
         * 移除对象
         *
         * @param object
         * @return true-调用此方法后对象被移除了
         */
        boolean remove(Object object);

        /**
         * 是否包含某个对象
         *
         * @param object
         * @return true-包含
         */
        boolean contains(T object);

        /**
         * 当前保存对象的个数
         *
         * @return
         */
        int size();

        /**
         * 清空
         */
        void clear();

        /**
         * 正序遍历
         *
         * @param callback
         * @return 返回callback中的data {@link ForeachCallback#getData()}
         */
        Object foreach(ForeachCallback<T> callback);

        /**
         * 倒序遍历
         *
         * @param callback
         * @return 返回callback中的data {@link ForeachCallback#getData()}
         */
        Object foreachReverse(ForeachCallback<T> callback);

        abstract class ForeachCallback<T>
        {
            private Object mData;

            /**
             * 设置遍历的数据
             *
             * @param data
             */
            protected final void setData(Object data)
            {
                mData = data;
            }

            /**
             * 返回设置的数据
             *
             * @return
             */
            public final Object getData()
            {
                return mData;
            }

            /**
             * 遍历到每一个item的时候触发此方法
             *
             * @param item
             * @return true-停止遍历，false-继续遍历
             */
            protected abstract boolean next(T item);
        }
    }

    /**
     * 强引用对象holder
     *
     * @param <T>
     */
    public static class FStrongObjectsHolder<T> implements ObjectsHolder<T>
    {
        private final List<T> mListObject;

        public FStrongObjectsHolder(List<T> list)
        {
            if (list == null)
                list = new CopyOnWriteArrayList<>();

            mListObject = list;
        }

        @Override
        public boolean add(T object)
        {
            if (object == null || contains(object))
                return false;

            mListObject.add(object);
            return true;
        }

        @Override
        public boolean remove(Object object)
        {
            return mListObject.remove(object);
        }

        @Override
        public boolean contains(T object)
        {
            return mListObject.contains(object);
        }

        @Override
        public int size()
        {
            return mListObject.size();
        }

        @Override
        public void clear()
        {
            mListObject.clear();
        }

        @Override
        public Object foreach(ForeachCallback<T> callback)
        {
            if (callback == null)
                return null;

            for (T item : mListObject)
            {
                if (callback.next(item))
                    break;
            }
            return callback.getData();
        }

        @Override
        public Object foreachReverse(ForeachCallback<T> callback)
        {
            if (callback == null)
                return null;

            final ListIterator<T> it = mListObject.listIterator(mListObject.size());
            while (it.hasPrevious())
            {
                if (callback.next(it.previous()))
                    break;
            }
            return callback.getData();
        }

        @Override
        public String toString()
        {
            return super.toString() + " " + mListObject.toString();
        }
    }

}
