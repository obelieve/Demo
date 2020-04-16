package com.zxy.demo._issue;

import com.airbnb.lottie.LottieAnimationView;
import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ((DefaultItemAnimator) #RecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//RecyclerView item中使用，要调用 （动画有冲突）
 */
public class LottieAnimationFragment extends BaseFragment {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    String[] mImageJson = new String[]{"community", "data", "find", "match", "me", "refresh", "zan"};

    @Override
    public int layoutId() {
        return R.layout.fragment_lottie_animation;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.animation_view)
    public void onViewClicked() {
        int index = new Random().nextInt(mImageJson.length);
        animationView.cancelAnimation();
        animationView.setImageAssetsFolder(mImageJson[index]+"/images");
        animationView.setAnimation(mImageJson[index]+"/"+mImageJson[index]+".json");
        animationView.playAnimation();
    }
}
