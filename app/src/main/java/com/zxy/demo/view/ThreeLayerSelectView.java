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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zxy
 * on 2020/5/8
 */
public class ThreeLayerSelectView extends FrameLayout {

    @BindView(R.id.rv_type1)
    RecyclerView rvType1;
    @BindView(R.id.rv_type2)
    RecyclerView rvType2;
    @BindView(R.id.rv_type3)
    RecyclerView rvType3;

    Type1Adapter mType1Adapter;
    Type2Adapter mType2Adapter;
    Type3Adapter mType3Adapter;

    List<Select1Entity> mSelect1EntityList;
    int[] mType1SelectedPosition = new int[1];
    int[] mType2SelectedPosition = new int[2];
    int[] mType3SelectedPosition = new int[3];
    Callback mCallback;

    public ThreeLayerSelectView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ThreeLayerSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeLayerSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_three_layer_select, this, true);
        ButterKnife.bind(this, view);
        mType1Adapter = new Type1Adapter(context);
        mType2Adapter = new Type2Adapter(context);
        mType3Adapter = new Type3Adapter(context);
        rvType1.setLayoutManager(new LinearLayoutManager(context));
        rvType2.setLayoutManager(new LinearLayoutManager(context));
        rvType3.setLayoutManager(new LinearLayoutManager(context));
        rvType1.setAdapter(mType1Adapter);
        rvType2.setAdapter(mType2Adapter);
        rvType3.setAdapter(mType3Adapter);
        mType1Adapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<Select1Entity>() {
            @Override
            public void onItemClick(View view, Select1Entity entity, int position) {
                if (mSelect1EntityList.get(mType1SelectedPosition[0]) != null &&
                        entity != mSelect1EntityList.get(mType1SelectedPosition[0])) {
                    resetSelectedPosition(1, position, 0, 0);
                    mType1Adapter.notifyDataSetChanged();
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mType2Adapter.getDataHolder().setList(entity.getList());
                        if (entity.getList().get(0).getList() != null && entity.getList().get(0).getList().size() > 0) {
                            mType3Adapter.getDataHolder().setList(entity.getList().get(0).getList());
                        }
                    }
                }
            }
        });
        mType2Adapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<Select2Entity>() {
            @Override
            public void onItemClick(View view, Select2Entity entity, int position) {
                if (mSelect1EntityList.get(mType2SelectedPosition[0]) != null &&
                        mSelect1EntityList.get(mType2SelectedPosition[0]).getList() != null &&
                        entity != mSelect1EntityList.get(mType2SelectedPosition[0]).getList().get(mType2SelectedPosition[1])) {
                    resetSelectedPosition(2, mType1SelectedPosition[0], position, 0);
                    mType2Adapter.notifyDataSetChanged();
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mType3Adapter.getDataHolder().setList(entity.getList());
                    }
                }
            }
        });
        mType3Adapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<Select3Entity>() {
            @Override
            public void onItemClick(View view, Select3Entity entity, int position) {
                if (mSelect1EntityList.get(mType3SelectedPosition[0]) != null &&
                        mSelect1EntityList.get(mType3SelectedPosition[0]).getList() != null &&
                        mSelect1EntityList.get(mType3SelectedPosition[0]).getList().get(mType3SelectedPosition[1]) != null &&
                        mSelect1EntityList.get(mType3SelectedPosition[0]).getList().get(mType3SelectedPosition[1]).getList() != null &&
                        entity != mSelect1EntityList.get(mType3SelectedPosition[0]).getList().get(mType3SelectedPosition[1]).getList().get(mType3SelectedPosition[2])) {
                    resetSelectedPosition(3, mType1SelectedPosition[0], mType2SelectedPosition[1], position);
                    mType3Adapter.notifyDataSetChanged();
                }
                if (mCallback != null) {
                    mCallback.onSelectedItem(mType1SelectedPosition[0], mType2SelectedPosition[1], mType3SelectedPosition[2]);
                }
            }
        });
    }

    public void loadData(List<Select1Entity> list) {
        mSelect1EntityList = list;
        int[] posArr = getDefSelectedPosition(list);
        int pos1 = posArr[0];
        int pos2 = posArr[1];
        int pos3 = posArr[2];
        mType1SelectedPosition[0] = pos1;

        mType2SelectedPosition[0] = pos1;
        mType2SelectedPosition[1] = pos2;

        mType3SelectedPosition[0] = pos1;
        mType3SelectedPosition[1] = pos2;
        mType3SelectedPosition[2] = pos3;
        mType1Adapter.getDataHolder().setList(list);
        if (list != null && mType2SelectedPosition[0] < list.size()) {
            mType2Adapter.getDataHolder().setList(list.get(mType2SelectedPosition[0]).getList());
            if (list.get(mType2SelectedPosition[0]).getList() != null && mType2SelectedPosition[1] < list.get(mType2SelectedPosition[0]).getList().size()) {
                mType3Adapter.getDataHolder().setList(list.get(mType2SelectedPosition[0]).getList().get(mType2SelectedPosition[1]).getList());
            }
        }
    }

    private void resetSelectedPosition(int layer, int pos1, int pos2, int pos3) {
        if (mSelect1EntityList != null) {
            try {
                mSelect1EntityList.get(mType1SelectedPosition[0]).setSelected(false);
                mSelect1EntityList.get(pos1).setSelected(true);
                mType1SelectedPosition[0] = pos1;
                if (layer >= 2) {
                    mSelect1EntityList.get(mType2SelectedPosition[0]).getList().get(mType2SelectedPosition[1]).setSelected(false);
                    mSelect1EntityList.get(pos1).getList().get(pos2).setSelected(true);
                    mType2SelectedPosition[0] = pos1;
                    mType2SelectedPosition[1] = pos2;
                }
                if (layer >= 3) {
                    mSelect1EntityList.get(mType3SelectedPosition[0]).getList().get(mType3SelectedPosition[1]).getList().get(mType3SelectedPosition[2]).setSelected(false);
                    mSelect1EntityList.get(pos1).getList().get(pos2).getList().get(pos3).setSelected(true);
                    mType3SelectedPosition[0] = pos1;
                    mType3SelectedPosition[1] = pos2;
                    mType3SelectedPosition[2] = pos3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (layer >= 1) {
                    mType1SelectedPosition[0] = 0;
                }
                if (layer >= 2) {
                    mType2SelectedPosition[0] = 0;
                    mType2SelectedPosition[1] = 0;
                }
                if (layer >= 3) {
                    mType3SelectedPosition[0] = 0;
                    mType3SelectedPosition[1] = 0;
                    mType3SelectedPosition[2] = 0;
                }
            }
        }
    }


    private int[] getDefSelectedPosition(List<Select1Entity> list) {
        int pos1 = 0;
        int pos2 = 0;
        int pos3 = 0;
        if (list != null) {
            pos1 = getSelectedPosition((List) list);
            if (pos1 < list.size()) {
                List<Select2Entity> list2 = list.get(pos1).getList();
                if (list2 != null) {
                    pos2 = getSelectedPosition((List) list2);
                    if (pos2 < list2.size()) {
                        List<Select3Entity> list3 = list2.get(pos2).getList();
                        if (list3 != null) {
                            pos3 = getSelectedPosition((List) list3);
                        }
                    }
                }
            }
        }
        return new int[]{pos1, pos2, pos3};
    }

    private int getSelectedPosition(List<Selectable> list) {
        int position = 0;
        boolean selected = false;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && list.get(i).isSelected()) {
                    selected = true;
                    position = i;
                    continue;
                }
                if (selected) {
                    list.get(i).setSelected(false);
                }
            }
        }
        return position;
    }

    public static class Type1Adapter extends BaseRecyclerViewAdapter<Select1Entity> {

        public Type1Adapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new TypeSelect1ViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            TypeSelect1ViewHolder viewHolder = (TypeSelect1ViewHolder) holder;
            viewHolder.tvName.setText(getDataHolder().getList().get(position).getName());
            viewHolder.tvName.setSelected(getDataHolder().getList().get(position).isSelected());
        }


        public class TypeSelect1ViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView tvName;

            public TypeSelect1ViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_type_select1);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class Type2Adapter extends BaseRecyclerViewAdapter<Select2Entity> {

        public Type2Adapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new TypeSelect2ViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            TypeSelect2ViewHolder viewHolder = (TypeSelect2ViewHolder) holder;
            viewHolder.tvName.setText(getDataHolder().getList().get(position).getName());
            viewHolder.tvName.setSelected(getDataHolder().getList().get(position).isSelected());
        }


        public class TypeSelect2ViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView tvName;

            public TypeSelect2ViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_type_select2);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class Type3Adapter extends BaseRecyclerViewAdapter<Select3Entity> {

        public Type3Adapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new TypeSelect3ViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            TypeSelect3ViewHolder viewHolder = (TypeSelect3ViewHolder) holder;
            viewHolder.tvName.setText(getDataHolder().getList().get(position).getName());
            if (getDataHolder().getList().get(position).isSelected()) {
                viewHolder.ivSelect.setVisibility(VISIBLE);
            } else {
                viewHolder.ivSelect.setVisibility(GONE);
            }
        }


        public class TypeSelect3ViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.iv_select)
            ImageView ivSelect;

            public TypeSelect3ViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_type_select3);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class Select1Entity extends BaseSelectEntity {

        private List<Select2Entity> list;

        public List<Select2Entity> getList() {
            return list;
        }

        public void setList(List<Select2Entity> list) {
            this.list = list;
        }
    }

    public static class Select2Entity extends BaseSelectEntity {

        private List<Select3Entity> list;

        public List<Select3Entity> getList() {
            return list;
        }

        public void setList(List<Select3Entity> list) {
            this.list = list;
        }
    }

    public static class Select3Entity extends BaseSelectEntity {

    }

    public static class BaseSelectEntity implements Selectable {

        private String name;
        private boolean selected;

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isSelected() {
            return selected;
        }

        @Override
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public interface Selectable {
        String getName();

        boolean isSelected();

        void setSelected(boolean selected);
    }

    public interface Callback {
        void onSelectedItem(int pos1, int pos2, int pos3);
    }
}
