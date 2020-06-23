package com.zxy.demo.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.zxy.demo.R;
import com.zxy.demo.activity.VersionUpdateActivity;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.demo.viewmodel.VersionUpdateViewModel;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;

import butterknife.OnClick;

public class VersionUpdateEntranceFragment extends BaseFragment {

    VersionUpdateViewModel mVersionUpdateViewModel;
    boolean mBool;

    @Override
    public int layoutId() {
        return R.layout.fragment_version_update_entrance;
    }

    @Override
    protected void initView() {
        mVersionUpdateViewModel = ViewModelProviders.of(this).get(VersionUpdateViewModel.class);
        mVersionUpdateViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mBool = aBoolean;
            }
        });
        mVersionUpdateViewModel.getVersionUpdateEntityLiveData().observe(this, new Observer<VersionUpdateEntity>() {
            @Override
            public void onChanged(VersionUpdateEntity entity) {
                if (entity.getIs_need() == 1) {
                    VersionUpdateActivity.start(getActivity(), entity);
                } else {
                    ToastUtil.show("不需要更新");
                }
            }
        });
    }


    @OnClick(R.id.btn_version)
    public void onViewClicked() {
        if (!mBool) {
            mVersionUpdateViewModel.version_check();
        }
    }
}
