package com.zxy.demo.fragment;

import android.view.View;

import com.zxy.demo.databinding.FragmentLottieAnimationBinding;
import com.zxy.frame.base.ApiBaseFragment;

import java.util.Random;

/**
 * ((DefaultItemAnimator) #RecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//RecyclerView item中使用，要调用 （动画有冲突）
 */
public class LottieAnimationFragment extends ApiBaseFragment<FragmentLottieAnimationBinding> {


    String[] mImageJson = new String[]{"community", "data", "find", "match", "me", "refresh", "zan"};


    @Override
    protected void initView() {
        mViewBinding.animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = new Random().nextInt(mImageJson.length);
                mViewBinding.animationView.cancelAnimation();
                mViewBinding.animationView.setImageAssetsFolder(mImageJson[index]+"/images");
                mViewBinding.animationView.setAnimation(mImageJson[index]+"/"+mImageJson[index]+".json");
                mViewBinding.animationView.playAnimation();
            }
        });
    }
}
