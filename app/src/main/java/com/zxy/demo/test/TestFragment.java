package com.zxy.demo.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.zxy.demo.databinding.FragmentTestBinding;
import com.zxy.demo.databinding.ItemTestBinding;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.info.SystemInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 * on 2020/6/30
 */
public class TestFragment extends ApiBaseFragment<FragmentTestBinding> {


    TestAdapter mTestAdapter;

    @Override
    protected void initView() {
        mTestAdapter = new TestAdapter(getActivity());
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemInfoUtil.dp2px(mActivity, 100)));
        tv.setBackgroundColor(Color.GREEN);
        tv.setText("空数据");
        mTestAdapter.setEmptyView(tv);
        mViewBinding.rvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mViewBinding.rvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = SystemInfoUtil.dp2px(mActivity, 10);
                outRect.bottom = SystemInfoUtil.dp2px(mActivity, 10);
            }
        });
        mViewBinding.rvContent.setAdapter(mTestAdapter);
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
        String url = "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1626745461,2641918547&fm=26&gp=0.jpg";
        int size = SystemInfoUtil.dp2px(getContext(),50);
        Glide.with(TestFragment.this).load(url).circleCrop().override(size,size).into(mViewBinding.iv);

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
                return new T1(ItemTestBinding.inflate(LayoutInflater.from(parent.getContext())));
            } else {
                return new T2(ItemTestBinding.inflate(LayoutInflater.from(parent.getContext())));
            }
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position),position,getDataHolder().getList());
        }

        public static class T1 extends BaseViewHolder<String, ItemTestBinding> {


            public T1(ItemTestBinding viewBinding) {
                super(viewBinding);
            }

            @Override
            public void bind(String s, int position, List<String> list) {
                mViewBinding.textView.setBackgroundColor(Color.GREEN);
                mViewBinding.textView.setText(s);
            }
        }

        public static class T2 extends BaseViewHolder<String,ItemTestBinding> {


            public T2(ItemTestBinding viewBinding) {
                super(viewBinding);
            }

            @Override
            public void bind(String s, int position, List<String> list) {
                mViewBinding.textView.setText(s);
            }
        }

        @Override
        public int loadItemViewType(int position) {
            return getDataHolder().getList().get(position).contains("1") ? TYPE1 : TYPE2;
        }
    }
}
