package com.github.obelieve.community.adapter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.obelieve.App;
import com.zxy.frame.utils.SystemUtil;


/**
 * Created by Admin
 * on 2020/8/24
 */
public class CommunityStaggeredGridItemDecoration extends RecyclerView.ItemDecoration {

    int padding;

    {
        padding = SystemUtil.dp2px(App.getContext(), 12);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) parent.getLayoutManager();
        int spanCount = lm.getSpanCount();
        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();
        if (position < spanCount && spanIndex < spanCount) {
            outRect.top = padding;
        }
        outRect.right = padding;
        outRect.bottom = padding;
    }
}
