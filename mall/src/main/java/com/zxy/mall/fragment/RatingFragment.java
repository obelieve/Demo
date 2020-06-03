package com.zxy.mall.fragment;

import com.zxy.frame.base.BaseFragment;
import com.zxy.mall.R;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.view.header.RatingHeaderView;

import butterknife.BindView;

public class RatingFragment extends BaseFragment {

    @BindView(R.id.view_rating_header)
    RatingHeaderView mViewRatingHeader;

    @Override
    public int layoutId() {
        return R.layout.fragment_rating;
    }

    @Override
    protected void initView() {
        mViewRatingHeader.loadData(MockRepository.getSeller(),MockRepository.getRatingList());
    }
}
