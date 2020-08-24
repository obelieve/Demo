package com.zxy.demo.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.utility.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/6/30
 */
public class TestFragment extends BaseFragment {


    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    TestAdapter mTestAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        mTestAdapter = new TestAdapter(getActivity());
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.dp2px(100)));
        tv.setBackgroundColor(Color.GREEN);
        tv.setText("空数据");
        mTestAdapter.setEmptyView(tv);
        rvContent.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        rvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = SystemUtil.dp2px(10);
                outRect.bottom = SystemUtil.dp2px(10);
            }
        });
        rvContent.setAdapter(mTestAdapter);
        List<String> l = new ArrayList<>();
        int i = 6;
        while (i > 0) {
            l.add(i + "ASDAD我完全A");
            i--;
        }
        mTestAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<String>() {
            @Override
            public void onItemClick(View view, String t, int position) {
                ToastUtil.show(t);
            }
        });
        mTestAdapter.getDataHolder().setList(l);

    }

    public static class TestAdapter extends BaseRecyclerViewAdapter<String> {

        private static final int TYPE1 = 0;
        private static final int TYPE2 = 1;

        public TestAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE1) {
                return new T1(parent);
            } else {
                return new T2(parent);
            }
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }

        public static class T1 extends BaseViewHolder<String> {

            @BindView(R.id.textView)
            TextView mTextView;

            public T1(ViewGroup parent) {
                super(parent, R.layout.item_test);
            }

            @Override
            public void bind(String s) {
                super.bind(s);
                mTextView.setBackgroundColor(Color.GREEN);
                mTextView.setText(s);
            }
        }

        public static class T2 extends BaseViewHolder<String> {

            @BindView(R.id.textView)
            TextView mTextView;

            public T2(ViewGroup parent) {
                super(parent, R.layout.item_test);
            }

            @Override
            public void bind(String s) {
                super.bind(s);
                mTextView.setText(s);
            }
        }

        @Override
        public int loadItemViewType(int position) {
            return getDataHolder().getList().get(position).contains("1") ? TYPE1 : TYPE2;
        }
    }
}
