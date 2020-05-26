package com.zxy.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/5/25
 */
public class ListSelectView extends FrameLayout {
    /**
     * 单选类型
     */
    public static final int SINGLE_TYPE = 0;
    /**
     * 复选类型
     */
    public static final int MULTI_TYPE = 1;

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.view_empty)
    View viewEmpty;

    List<IListSelectViewData> mList = new ArrayList<>();
    ListSelectAdapter mAdapter;
    Callback mCallback;
    int mSelectType;
    int mCurSelectedPosition;

    public ListSelectView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ListSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_list_select, this, true);
        ButterKnife.bind(this, view);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    /**
     * @param list
     * @param selectType ListSelectView#SINGLE_TYPE,ListSelectView#MULTI_TYPE
     */
    public void loadData(List<IListSelectViewData> list, int selectType) {
        mCurSelectedPosition = -1;
        mSelectType = selectType;
        mList = list != null ? list : new ArrayList<>();
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ListSelectAdapter(getContext());
        mAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<IListSelectViewData>() {
            @Override
            public void onItemClick(View view, IListSelectViewData data, int position) {
                if (mSelectType == SINGLE_TYPE) {
                    if (mCurSelectedPosition != position) {
                        if (mCurSelectedPosition >= 0 && mCurSelectedPosition < mAdapter.getDataHolder().getList().size()) {
                            mAdapter.getDataHolder().getList().get(mCurSelectedPosition).setSelected(false);
                        }
                        mAdapter.getDataHolder().getList().get(position).setSelected(true);
                        mAdapter.notifyDataSetChanged();
                        mCurSelectedPosition = position;
                        if (mCallback != null) {
                            mCallback.onSingleSelected(data);
                        }
                    }
                } else if (mSelectType == MULTI_TYPE) {
                    data.setSelected(!data.isSelected());
                    displaySelectView(view, data);
                }
            }
        });
        mAdapter.getDataHolder().setList(mList);
        rvContent.setAdapter(mAdapter);
    }

    public List<IListSelectViewData> getSelectedData() {
        List<IListSelectViewData> list = new ArrayList<>();
        if (mAdapter != null) {
            for (IListSelectViewData data : mAdapter.getDataHolder().getList()) {
                if (data != null && data.isSelected()) {
                    list.add(data);
                }
            }
        }
        return list;
    }

    private void displaySelectView(View view, IListSelectViewData data) {
        TextView tvName = view.findViewById(R.id.tv_name);
        ImageView ivSelect = view.findViewById(R.id.iv_select);
        if (tvName != null) {
            tvName.setSelected(data.isSelected());
        }
        if (ivSelect != null) {
            if (data.isSelected()) {
                ivSelect.setVisibility(VISIBLE);
            } else {
                ivSelect.setVisibility(GONE);
            }
        }
    }

    @OnClick(R.id.view_empty)
    public void onViewClicked() {
        if (mCallback != null) {
            mCallback.onClickEmpty();
        }
    }

    public static class ListSelectAdapter extends BaseRecyclerViewAdapter<IListSelectViewData> {


        public ListSelectAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new ListSelectViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            ListSelectViewHolder viewHolder = (ListSelectViewHolder) holder;
            viewHolder.bind(getDataHolder().getList().get(position));
        }

        public class ListSelectViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.iv_select)
            ImageView ivSelect;

            public ListSelectViewHolder(ViewGroup parent) {
                super(parent, R.layout.view_list_select_item);
                ButterKnife.bind(this, itemView);
            }

            public void bind(IListSelectViewData data) {
                if (data != null) {
                    tvName.setText(data.getName());
                    setSelectView(data);
                }
            }

            private void setSelectView(IListSelectViewData data) {
                tvName.setSelected(data.isSelected());
                if (data.isSelected()) {
                    ivSelect.setVisibility(VISIBLE);
                } else {
                    ivSelect.setVisibility(GONE);
                }
            }
        }
    }

    public interface IListSelectViewData {

        int getId();

        String getName();

        boolean isSelected();

        void setSelected(boolean selected);
    }

    public interface Callback {

        void onClickEmpty();

        void onSingleSelected(IListSelectViewData data);
    }

}
