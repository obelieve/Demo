package com.zxy.demo.fragment;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SystemUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class Fragment2 extends BaseFragment {

    @BindView(R.id.rv_content)
    CRecyclerView mRvContent;

    ConAdapter mConAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_2;
    }

    @Override
    protected void initView() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        mRvContent.setLayoutManager(lm);
        mConAdapter = new ConAdapter(getActivity());
        mConAdapter.getDataHolder().setList(Arrays.asList(
                Arrays.asList("你", "wo", "说"),
                Arrays.asList("你", "wo", "撒"),
                Arrays.asList("你", "wo", "是"),
                Arrays.asList("你", "wo", "是"),
                Arrays.asList("你", "", "是")
        ));
        mRvContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.dp2px(42)*3));
        mRvContent.setAdapter(mConAdapter);
        LogUtil.e("mRvContent h=" + mRvContent.getMeasuredHeight() + " w=" + mRvContent.getMeasuredWidth());
    }

    public static class ConAdapter extends BaseRecyclerViewAdapter<List<String>> {


        public ConAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new CViewHolder(parent, R.layout.viewholder_con);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }

        public static class CViewHolder extends BaseViewHolder<List<String>> {

            @BindView(R.id.rv_item)
            CRecyclerView mRvItem;

            ItemAdapter mItemAdapter;

            public CViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                mRvItem.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                mItemAdapter = new ItemAdapter(parent.getContext());
                mRvItem.setAdapter(mItemAdapter);
            }

            @Override
            public void bind(List<String> list) {
                super.bind(list);
                mItemAdapter.getDataHolder().setList(list);
            }

            public static class ItemAdapter extends BaseRecyclerViewAdapter<String> {


                public ItemAdapter(Context context) {
                    super(context);
                }

                @Override
                public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                    return new ItemViewHolder(parent, R.layout.viewholder_con_item);
                }

                @Override
                public void loadViewHolder(BaseViewHolder holder, int position) {
                    holder.bind(getDataHolder().getList().get(position));
                }

                public static class ItemViewHolder extends BaseViewHolder<String> {

                    @BindView(R.id.tv_content)
                    CTextView mTvContent;

                    public ItemViewHolder(ViewGroup parent, int layoutId) {
                        super(parent, layoutId);
                    }

                    @Override
                    public void bind(String s) {
                        super.bind(s);
                        mTvContent.setText(s);
                    }
                }
            }
        }
    }
}
