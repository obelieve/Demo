package com.zxy.demo.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.zxy.demo.R;
import com.zxy.demo.view.ThreeLayerSelectView;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.PopupMenuUtil;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zxy
 * on 2020/5/8
 */
public class FilterListFragment extends BaseFragment {


    @BindView(R.id.ll_type1)
    LinearLayout llType1;
    @BindView(R.id.ll_type2)
    LinearLayout llType2;
    @BindView(R.id.ll_type3)
    LinearLayout llType3;

    PopupMenuUtil mPopupMenuType1;
    ThreeLayerSelectView mThreeLayerSelectView;
    @Override
    public int layoutId() {
        return R.layout.fragment_filter_list;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.ll_type1, R.id.ll_type2, R.id.ll_type3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_type1:
                if(mPopupMenuType1==null){
                    mThreeLayerSelectView = new ThreeLayerSelectView(view.getContext());
                    List<ThreeLayerSelectView.Select1Entity> select1EntityList = MockData.getSelect1Entity();
                    mThreeLayerSelectView.loadData(select1EntityList);
                    mThreeLayerSelectView.setCallback(new ThreeLayerSelectView.Callback() {
                        @Override
                        public void onSelectedItem(int pos1, int pos2, int pos3) {
                            String selected1 = select1EntityList.get(pos1).getName();
                            String selected2 = select1EntityList.get(pos1).getList().get(pos2).getName();
                            String selected3 = select1EntityList.get(pos1).getList().get(pos2).getList().get(pos3).getName();
                            LogUtil.e(String.format("%s->%s->%s",selected1,selected2,selected3));
                            mPopupMenuType1.dismiss();
                        }
                    });
                    mPopupMenuType1 = new PopupMenuUtil(getActivity());
                }
                mPopupMenuType1.showDownPopup(llType1, mThreeLayerSelectView);
                break;
            case R.id.ll_type2:
                break;
            case R.id.ll_type3:
                break;
        }
    }



    public static class MockData{
        public static List<ThreeLayerSelectView.Select1Entity> getSelect1Entity() {
            List<ThreeLayerSelectView.Select1Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select1Entity select1Entity = new ThreeLayerSelectView.Select1Entity();
                select1Entity.setSelected(i == 0);
                select1Entity.setName(ThreeLayerSelectView.Select1Entity.class.getSimpleName() + " " + i);
                select1Entity.setList(getSelect2Entity(ThreeLayerSelectView.Select1Entity.class.getSimpleName() + "->" + i));
                list.add(select1Entity);
            }
            return list;
        }

        public static List<ThreeLayerSelectView.Select2Entity> getSelect2Entity(String tag) {
            List<ThreeLayerSelectView.Select2Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select2Entity select2Entity = new ThreeLayerSelectView.Select2Entity();
                select2Entity.setSelected(i == 0);
                select2Entity.setName(tag + " " + ThreeLayerSelectView.Select2Entity.class.getSimpleName() + " " + i);
                select2Entity.setList(getSelect3Entity(ThreeLayerSelectView.Select2Entity.class.getSimpleName() + "->" + i));
                list.add(select2Entity);
            }
            return list;
        }

        public static List<ThreeLayerSelectView.Select3Entity> getSelect3Entity(String tag) {
            List<ThreeLayerSelectView.Select3Entity> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ThreeLayerSelectView.Select3Entity select3Entity = new ThreeLayerSelectView.Select3Entity();
                select3Entity.setSelected(i == 0);
                select3Entity.setName(tag + " " + ThreeLayerSelectView.Select3Entity.class.getSimpleName() + " " + i);
                list.add(select3Entity);
            }
            return list;
        }
    }
}
