package com.zxy.demo.fragment;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.zxy.demo.activity.VersionUpdateActivity;
import com.zxy.demo.databinding.FragmentVersionUpdateEntranceBinding;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.demo.viewmodel.VersionUpdateViewModel;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

public class VersionUpdateEntranceFragment extends ApiBaseFragment<FragmentVersionUpdateEntranceBinding> {

    VersionUpdateViewModel mVersionUpdateViewModel;
    boolean mBool;

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
        mViewBinding.btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBool) {
                    mVersionUpdateViewModel.version_check();
                }
            }
        });
    }

}
