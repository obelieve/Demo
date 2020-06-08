package com.zxy.demo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.adapter.layout_manager.AutoFixWidthLayoutManager;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.view.LeftRightRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ListContainerFragment extends BaseFragment {

    @BindView(R.id.view_lr_recycler_view)
    LeftRightRecyclerView<LeftData, RightData> mViewLrRecyclerView;
    @BindView(R.id.rv_auto_fix_width)
    RecyclerView rvAutoFixWidth;

    AutoFixWidthAdapter mAutoFixWidthAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_list_container;
    }

    @Override
    protected void initView() {
        mViewLrRecyclerView.setCallback(new LeftRightRecyclerView.Callback<RightData>() {
            @Override
            public void onRightItemClick(View view, RightData data, int position) {

            }
        });
        mViewLrRecyclerView.init(mLeftViewHolderFactory,
                mRightViewHolderFactory);
        mViewLrRecyclerView.setLeftRightData(getDataList(), getDataItemList());
        mAutoFixWidthAdapter = new AutoFixWidthAdapter(getActivity());
        rvAutoFixWidth.setLayoutManager(new AutoFixWidthLayoutManager());
        rvAutoFixWidth.setAdapter(mAutoFixWidthAdapter);
        rvAutoFixWidth.addItemDecoration(new HorizontalItemDivider(true,5, Color.parseColor("#00000000")));
        rvAutoFixWidth.addItemDecoration(new VerticalItemDivider(true,5,Color.parseColor("#00000000")));
        mAutoFixWidthAdapter.getDataHolder().setList(Arrays.asList("123","名称","数据结构","算法","计算机","繁花似锦"));
    }

    LeftRightRecyclerView.LeftViewHolderFactory<LeftData> mLeftViewHolderFactory = new LeftRightRecyclerView.LeftViewHolderFactory<LeftData>() {
        @Override
        public LeftRightRecyclerView.LeftViewHolder<LeftData> genLeftViewHolder(ViewGroup parent) {
            return new LeftRightRecyclerView.LeftViewHolder<LeftData>(parent, R.layout.item_lrrv) {
                @Override
                public void bind(LeftData data) {
                    TextView tv = itemView.findViewById(R.id.tv_name);
                    tv.setText(data.toString());
                    boolean select = data.isSelect();
                    tv.setSelected(select);
                }
            };
        }
    };

    LeftRightRecyclerView.RightViewHolderFactory<RightData> mRightViewHolderFactory = new LeftRightRecyclerView.RightViewHolderFactory<RightData>() {

        @Override
        public LeftRightRecyclerView.RightViewHolder<RightData> genRightViewHolder(ViewGroup parent) {
            return new LeftRightRecyclerView.RightViewHolder<RightData>(parent, R.layout.item_lrrv) {

                @Override
                public void bind(RightData data) {
                    TextView tv = itemView.findViewById(R.id.tv_name);
                    tv.setText(data.toString());
                }
            };
        }
    };

    public static class AutoFixWidthAdapter extends BaseRecyclerViewAdapter<String> {

        public AutoFixWidthAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(parent,R.layout.viewholder_auto_fix_width);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            TextView tv = holder.itemView.findViewById(R.id.tv_name);
            tv.setText(getDataHolder().getList().get(position));
        }
    }

    public static class LeftData implements LeftRightRecyclerView.ILeftData {

        private boolean select;
        private int type;
        private List<RightData> dataItemList;

        public LeftData(int type) {
            this.type = type;
        }

        @Override
        public void setSelected(boolean selected) {
            this.select = selected;
        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public String LRRVTag() {
            return type + "";
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean isSelect() {
            return select;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<RightData> getDataItemList() {
            return dataItemList;
        }

        public void setDataItemList(List<RightData> dataItemList) {
            this.dataItemList = dataItemList;
        }

        @Override
        public String toString() {
            return "type=" + type;
        }
    }

    public static class RightData implements LeftRightRecyclerView.IRightData {

        private boolean top;
        private int type;

        public RightData(boolean top, int type) {
            this.top = top;
            this.type = type;
        }

        public boolean getTop() {
            return top;
        }

        public void setTop(boolean top) {
            this.top = top;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return top ? "OK" : "" + " type=" + type;
        }

        @Override
        public boolean isTop() {
            return top;
        }

        @Override
        public String LRRVTag() {
            return type + "";
        }
    }

    public static List<LeftData> getDataList() {
        List<LeftData> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LeftData data = new LeftData(i);
            if (i == 0) {
                data.setSelect(true);
            } else {
                data.setSelect(false);
            }
            List<RightData> dataItemList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                if (j == 0) {
                    dataItemList.add(new RightData(true, i));
                } else {
                    dataItemList.add(new RightData(false, i));
                }
            }
            data.setDataItemList(dataItemList);
            list.add(data);
        }
        return list;
    }

    public static List<RightData> getDataItemList() {
        List<LeftData> dataList = getDataList();
        List<RightData> list = new ArrayList<>();
        if (dataList != null) {
            for (LeftData data : dataList) {
                list.addAll(data.getDataItemList());
            }
        }
        return list;
    }

}
