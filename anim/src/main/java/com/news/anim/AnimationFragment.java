package com.news.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.news.anim.databinding.FragmentAnimationBinding;
import com.zxy.frame.base.ApiBaseFragment;


/**
 * Created by Admin
 * on 2020/6/9
 */
public class AnimationFragment extends ApiBaseFragment<FragmentAnimationBinding> implements View.OnClickListener {


    boolean mStartFrameAnim = true;

    @Override
    protected void initView() {
        mViewBinding.ivFrame.setOnClickListener(this);

        mViewBinding.btnAlpha.setOnClickListener(this);
        mViewBinding.btnTranslate.setOnClickListener(this);
        mViewBinding.btnScale.setOnClickListener(this);
        mViewBinding.btnRotate.setOnClickListener(this);
        mViewBinding.btnSet.setOnClickListener(this);

        mViewBinding.btnObjectAlpha.setOnClickListener(this);
        mViewBinding.btnObjectTranslate.setOnClickListener(this);
        mViewBinding.btnObjectScale.setOnClickListener(this);
        mViewBinding.btnObjectRotate.setOnClickListener(this);
        mViewBinding.btnObjectSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==mViewBinding.ivFrame.getId()){
            final AnimationDrawable animationDrawable = (AnimationDrawable) mViewBinding.ivFrame.getBackground();
            animationDrawable.setOneShot(false);//执行一次 或 循环
            if (mStartFrameAnim) {
                mStartFrameAnim = false;
                animationDrawable.start();
            } else {
                mStartFrameAnim = true;
                animationDrawable.stop();
            }
        }else{
            switch (view.getId()) {
                case R.id.btn_object_alpha:
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(mViewBinding.ivObject,"Alpha",0,1);
                    alpha.setInterpolator(new DecelerateInterpolator());
                    alpha.setDuration(1000);
                    alpha.start();
                    break;
                case R.id.btn_object_translate:
                    ObjectAnimator translate = ObjectAnimator.ofFloat(mViewBinding.ivObject,"TranslationX",0,100);
                    translate.setInterpolator(new DecelerateInterpolator());
                    translate.setDuration(1000);
                    translate.start();
                    break;
                case R.id.btn_object_scale:
                    ObjectAnimator scale = ObjectAnimator.ofFloat(mViewBinding.ivObject,"ScaleX",0,1f);
                    scale.setInterpolator(new DecelerateInterpolator());
                    scale.setDuration(1000);
                    scale.start();
                    break;
                case R.id.btn_object_rotate:
                    ObjectAnimator rotate = ObjectAnimator.ofFloat(mViewBinding.ivObject,"RotationX",0,300f);//Rotation 以屏幕方向为轴的旋转度数
                    rotate.setInterpolator(new DecelerateInterpolator());
                    rotate.setDuration(1000);
                    rotate.start();
                    break;
                case R.id.btn_object_set:
                    ObjectAnimator animatorA = ObjectAnimator.ofFloat(mViewBinding.ivObject, "TranslationY", -300, 300, 0);
                    ObjectAnimator animatorB = ObjectAnimator.ofFloat(mViewBinding.ivObject, "scaleX", 0.5f, 1.5f, 1f);
                    ObjectAnimator animatorC = ObjectAnimator.ofFloat(mViewBinding.ivObject, "rotation", 0, 270, 90, 180, 0);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animatorA, animatorB, animatorC);
                    animatorSet.setInterpolator(new DecelerateInterpolator());
                    animatorSet.setDuration(1000);
                    animatorSet.start();
                    break;
                default:
                    int anim = R.anim.rotate;
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
                    mViewBinding.ivTweened.startAnimation(animation);
            }

        }

    }

}
