package com.zxy.demo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.databinding.FragmentFilterListBinding;
import com.zxy.demo.databinding.ViewListSelectItemBinding;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.PopupMenuUtil;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.info.SystemInfoUtil;
import com.zxy.frame.utils.log.LogUtil;
import com.zxy.frame.view.select.ListSelectPopupView;
import com.zxy.frame.view.select.ListSelectView;
import com.zxy.frame.view.select.ThreeLayerSelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy
 * on 2020/5/8
 */
public class FilterListFragment extends ApiBaseFragment<FragmentFilterListBinding> implements View.OnClickListener {


    PopupMenuUtil mPopupMenuType1;
    PopupMenuUtil mPopupMenuType2;

    ThreeLayerSelectView mThreeLayerSelectView;
    ListSelectPopupView mListSelectPopupView;

    @Override
    protected void initView() {
        mViewBinding.llType1.setOnClickListener(this);
        mViewBinding.llType2.setOnClickListener(this);
        mViewBinding.llType3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_type1:
                if (mPopupMenuType1 == null) {
                    mThreeLayerSelectView = new ThreeLayerSelectView(view.getContext());
                    List<ThreeLayerSelectView.Select1Entity> select1EntityList = MockData.getSelect1Entity();
                    mThreeLayerSelectView.loadData(select1EntityList);
                    mThreeLayerSelectView.setCallback(new ThreeLayerSelectView.Callback() {

                        @Override
                        public void onClickEmpty() {
                            mPopupMenuType1.dismiss();
                        }

                        @Override
                        public void onSelectedItem(int id1, String name1, int id2, String name2, int id3, String name3) {
                            String str = String.format("%s->%s->%s", name1, name2, name3);
                            LogUtil.e(str);
                            ToastUtil.show(str);
                            mPopupMenuType1.dismiss();
                        }
                    });
                    mPopupMenuType1 = new PopupMenuUtil(getActivity());
                }
                mPopupMenuType1.showDownPopup(mViewBinding.llType1, mThreeLayerSelectView, SystemInfoUtil.screenWidth(mActivity));
                break;
            case R.id.ll_type2:
                if (mPopupMenuType2 == null) {
                    mListSelectPopupView = new ListSelectPopupView(view.getContext());
                    mListSelectPopupView.loadData(new MockData.ListSelectViewImpl(), MockData.getIListSelectViewDataList(), ListSelectView.SINGLE_TYPE);
                    mListSelectPopupView.setCallback(new ListSelectPopupView.Callback() {
                        @Override
                        public void onClickEmpty() {
                            mPopupMenuType2.dismiss();
                        }

                        @Override
                        public void onSingleSelected(ListSelectView.IListSelectViewData data) {
                            ToastUtil.show(data.getName());
                            mPopupMenuType2.dismiss();
                        }
                    });
                    mPopupMenuType2 = new PopupMenuUtil(getActivity());
                }
                mPopupMenuType2.showDownPopup(mViewBinding.llType2, mListSelectPopupView, SystemInfoUtil.screenWidth(mActivity));
                break;
            case R.id.ll_type3:
                break;
        }
    }


    public static class MockData {
        public static List<ThreeLayerSelectView.Select1Entity> getSelect1Entity() {
            List<ThreeLayerSelectView.Select1Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select1Entity select1Entity = new ThreeLayerSelectView.Select1Entity();
                select1Entity.setSelected(false);
                select1Entity.setName("" + i);
                select1Entity.setList(getSelect2Entity(""));
                list.add(select1Entity);
            }
            list.get(0).setSelected(true);
            list.get(0).getList().get(0).setSelected(true);
            list.get(0).getList().get(0).getList().get(0).setSelected(true);
            return list;
        }

        public static List<ThreeLayerSelectView.Select2Entity> getSelect2Entity(String tag) {
            List<ThreeLayerSelectView.Select2Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select2Entity select2Entity = new ThreeLayerSelectView.Select2Entity();
                select2Entity.setSelected(false);
                select2Entity.setName(tag + " " + i);
                select2Entity.setList(getSelect3Entity(""));
                list.add(select2Entity);
            }
            return list;
        }

        public static List<ThreeLayerSelectView.Select3Entity> getSelect3Entity(String tag) {
            List<ThreeLayerSelectView.Select3Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select3Entity select3Entity = new ThreeLayerSelectView.Select3Entity();
                select3Entity.setSelected(false);
                select3Entity.setName(tag + " " + i);
                list.add(select3Entity);
            }
            return list;
        }

        public static List<ListSelectView.IListSelectViewData> getIListSelectViewDataList() {
            List<ListSelectView.IListSelectViewData> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                list.add(new ListSelectData(i, "i:" + i, false));
            }
            return list;
        }

        public static class ListSelectViewImpl implements ListSelectView.IListSelectView {

            @Override
            public BaseRecyclerViewAdapter.BaseViewHolder genViewHolder(ViewGroup parent) {
                return new ListSelectViewHolder(ViewListSelectItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,false));
            }

            @Override
            public void select(View view, boolean selected) {
                TextView tvName = view.findViewById(R.id.tv_name);
                ImageView ivSelect = view.findViewById(R.id.iv_select);
                if (tvName != null) {
                    tvName.setSelected(selected);
                }
                if (ivSelect != null) {
                    if (selected) {
                        ivSelect.setVisibility(View.VISIBLE);
                    } else {
                        ivSelect.setVisibility(View.GONE);
                    }
                }
            }
        }

        public static class ListSelectViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<ListSelectData, ViewListSelectItemBinding> {


            public ListSelectViewHolder(ViewListSelectItemBinding viewBinding) {
                super(viewBinding);
            }


            @Override
            public void bind(ListSelectData data) {
                super.bind(data);
                if (data != null) {
                    mViewBinding.tvName.setText(data.getName());
                    setSelectView(data.isSelected());
                }
            }

            private void setSelectView(boolean selected) {
                mViewBinding.tvName.setSelected(selected);
                if (selected) {
                    mViewBinding.ivSelect.setVisibility(View.VISIBLE);
                } else {
                    mViewBinding.ivSelect.setVisibility(View.GONE);
                }
            }
        }

        public static class ListSelectData implements ListSelectView.IListSelectViewData {

            private int id;
            private String name;
            private boolean selected;

            public ListSelectData(int id, String name, boolean selected) {
                this.id = id;
                this.name = name;
                this.selected = selected;
            }

            public int getId() {
                return id;
            }

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
    }
}
