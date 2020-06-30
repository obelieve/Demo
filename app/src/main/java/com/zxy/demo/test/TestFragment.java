package com.zxy.demo.test;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.utility.LogUtil;
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
        //mTestAdapter.setFooterView(tv3);
        rvContent.setAdapter(mTestAdapter);
        List<String> l =new ArrayList<>();
        int i=100;
        while (i>0){
            l.add(i+"");
            i--;
        }
        mTestAdapter.getDataHolder().setList(l);
//        mTestAdapter.setLoadMoreListener(rvContent,new BaseRecyclerViewAdapter.OnLoadMoreListener(){
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
    }

    public static class TestAdapter extends BaseRecyclerViewAdapter<String> {

        public TestAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(parent,R.layout.item_test);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            TextView tv = holder.itemView.findViewById(R.id.textView);
            tv.setText(getDataHolder().getList().get(position));
        }

        @Override
        public int getItemCount() {
            int a = super.getItemCount();
            LogUtil.e("count=" + a);
            return a;
        }
    }
}
