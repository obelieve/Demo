# 解决：当状态栏透明且虚拟键盘设置android:windowSoftInputMode="adjustResize"时，EditText被键盘挡住问题
- 1.调用`AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content))`，进行重新计算高度;
```
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
}
```
### 解决一个场景问题：CoordinatorLayout + AppBarLayout + CollapsingToolbarLayout +ViewPager 布局时，要求 ViewPager切换到某个position时，这个position固定高度.
```
   vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			collapsing(position != 1);//position ==1 固定高度
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	});

	public void collapsing(boolean collapsing) {
		int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
		int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
		View appBarChildAt = appbar.getChildAt(0);
		AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
		if (collapsing) {
			appBarParams.setScrollFlags(i0 | i1);// 重置折叠效果
		} else {
			appBarParams.setScrollFlags(0);//不可滑动
		}
		appBarChildAt.setLayoutParams(appBarParams);
	}
```
