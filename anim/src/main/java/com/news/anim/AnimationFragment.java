package com.news.anim;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zxy.frame.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Admin
 * on 2020/6/9
 */
public class AnimationFragment extends BaseFragment {

    @BindView(R.id.iv_frame)
    ImageView ivFrame;
    @BindView(R.id.iv_tweened)
    ImageView ivTweened;

    boolean mStartFrameAnim = true;


    @Override
    public int layoutId() {
        return R.layout.fragment_animation;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.iv_frame)
    public void onViewClicked() {
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivFrame.getBackground();
        animationDrawable.setOneShot(false);//执行一次 或 循环
        if (mStartFrameAnim) {
            mStartFrameAnim = false;
            animationDrawable.start();
        } else {
            mStartFrameAnim = true;
            animationDrawable.stop();
        }
    }


    @OnClick({R.id.btn_alpha, R.id.btn_translate, R.id.btn_scale, R.id.btn_rotate, R.id.btn_set})
    public void onViewClicked(View view) {
        int anim = R.anim.rotate ;
        int id = view.getId();
        if (id == R.id.btn_alpha) {
            anim = R.anim.alpha;
        } else if (id == R.id.btn_translate) {
            anim = R.anim.translate;
        } else if (id == R.id.btn_scale) {
            anim = R.anim.scale;
        } else if (id == R.id.btn_rotate) {
            anim = R.anim.rotate;
        } else if (id == R.id.btn_set) {
            anim = R.anim.set;
        }
        Animation animation = AnimationUtils.loadAnimation(getContext(), anim);
        ivTweened.startAnimation(animation);
    }
}
