package com.zxy.demo.test;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
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
        mTestAdapter.setLoadMoreListener(rvContent, new BaseRecyclerViewAdapter.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                rvContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTestAdapter.loadMoreEnd();
                    }
                }, 2000);
            }
        });
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.dp2px(100)));
        tv.setBackgroundColor(Color.GREEN);
        tv.setText("空数据");
        mTestAdapter.setEmptyView(tv);
        //
        TextView tv2 = new TextView(getActivity());
        tv2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.dp2px(100)));
        tv2.setBackgroundColor(Color.BLUE);
        tv2.setText("Header");
        mTestAdapter.setHeaderView(tv2);
        //
        TextView tv3 = new TextView(getActivity());
        tv3.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.dp2px(100)));
        tv3.setBackgroundColor(Color.RED);
        tv3.setText("Footer");
        mTestAdapter.setFooterView(tv3);
        rvContent.addItemDecoration(new VerticalItemDivider());
        rvContent.setAdapter(mTestAdapter);
        List<String> l = new ArrayList<>();
        int i = 100;
        while (i > 0) {
            l.add(i + "");
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
            if(viewType==TYPE1){
                return new T1(parent);
            }else{
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
