package com.zxy.ui.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.news.anim.databinding.FragmentRvAnimationBinding;
import com.news.anim.databinding.ItemRvAnimBinding;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin
 * on 2020/6/10
 */
public class RvAnimationFragment extends ApiBaseFragment<FragmentRvAnimationBinding> {


    RvAdapter mRvAdapter;
    SpringAnimation mSpringAnimation;

    int mClickNum = 5;

    @Override
    protected void initView() {
        mRvAdapter = new RvAdapter(getActivity());
        mRvAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<String>() {
            @Override
            public void onItemClick(View view, String t, int position) {
                ToastUtil.show(t);
            }
        });
        mViewBinding.rvContent.setAdapter(mRvAdapter);
        mRvAdapter.getDataHolder().setList(Arrays.asList(
                "111111111111111111111111", "222222222222222222222222", "333333333333333333333333", "444444444444444444444444", "555555555555555555555555",
                "666666666666666666666666", "777777777777777777777777777777", "888888888888888888888888888888", "999999999999999999999999999999",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbb", "cccccccccccccccccccccccc", "dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeeeeeeee",
                "ffffffffffffffffffffffff", "gggggggggggggggggggggggg", "hhhhhhhhhhhhhhhhhhhhhhhh", "iiiiiiiiiiiiiiiiiiiiiiii",
                "jjjjjjjjjjjjjjjjjjjjjjjj", "kkkkkkkkkkkkkkkkkkkkkkkk", "llllllllllllllllllllllll", "mmmmmmmmmmmmmmmmmmmmmmmm", "nnnnnnnnnnnnnnnnnnnnnnnn",
                "oooooooooooooooooooooooooooooo", "pppppppppppppppppppppppp", "qqqqqqqqqqqqqqqqqqqqqqqq", "rrrrrrrrrrrrrrrrrrrrrrrr"));
        mSpringAnimation = new SpringAnimation(mViewBinding.viewBottom, SpringAnimation.TRANSLATION_Y, 0);
        mViewBinding.viewBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpringAnimation.cancel();
                mSpringAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
                mSpringAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
                mClickNum++;
                mSpringAnimation.setStartVelocity(1 << mClickNum);
                mSpringAnimation.start();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRvAdapter.release();
    }

    public static class RvAdapter extends BaseRecyclerViewAdapter<String> {

        boolean mIsOnlyFirstAnimation = false;
        int mLastPosition = -1;

        public RvAdapter(Context context) {
            super(context);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RvViewHolder(ItemRvAnimBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof RvViewHolder) {
                holder.bind(getDataHolder().getList().get(position),position,getDataHolder().getList());
            }
        }


        @Override
        public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (holder.getItemViewType() == 0) {
                int position = holder.getLayoutPosition();
                if (!mIsOnlyFirstAnimation || position > mLastPosition) {
                    setAnimation(holder.itemView, position);
                }
            }
        }

        boolean isOnlyFirst = false;
        private int lastPosition = -1;
        private Map<Object, ObjectAnimator> mObjectAnimatorMap = new HashMap<>();
        private Map<Object, ObjectAnimator> mObjectAnimator2Map = new HashMap<>();

        protected void setAnimation(View viewToAnimate, int position) {
            if ((!isOnlyFirst || position > lastPosition)) {
                ObjectAnimator animator;
                ObjectAnimator animator2;
                if (mObjectAnimatorMap.get(viewToAnimate) == null) {
                    animator = ObjectAnimator.ofFloat(viewToAnimate, "scaleX", 0.90f, 1);
                    animator.setDuration(300);
                    animator.setInterpolator(new DecelerateInterpolator());
                    mObjectAnimatorMap.put(viewToAnimate, animator);
                } else {
                    animator = mObjectAnimatorMap.get(viewToAnimate);
                    animator.start();
                }
                if (mObjectAnimator2Map.get(viewToAnimate) == null) {
                    animator2 = ObjectAnimator.ofFloat(viewToAnimate, "scaleY", 0.90f, 1);
                    animator2.setDuration(300);
                    animator2.setInterpolator(new DecelerateInterpolator());
                    mObjectAnimator2Map.put(viewToAnimate, animator2);
                } else {
                    animator2 = mObjectAnimator2Map.get(viewToAnimate);
                    animator2.start();
                }
                lastPosition = position;
            }
        }


        public void release() {
            for (Map.Entry<Object, ObjectAnimator> map : mObjectAnimatorMap.entrySet()) {
                ObjectAnimator animator = map.getValue();
                if (animator != null) {
                    animator.cancel();
                }
            }
            for (Map.Entry<Object, ObjectAnimator> map : mObjectAnimator2Map.entrySet()) {
                ObjectAnimator animator = map.getValue();
                if (animator != null) {
                    animator.cancel();
                }
            }
            mObjectAnimatorMap.clear();
            mObjectAnimator2Map.clear();
        }

        @Override
        public void onSetListAfter() {
            super.onSetListAfter();
            release();
            mLastPosition = -1;
        }

        public static class RvViewHolder extends BaseViewHolder<String, ItemRvAnimBinding> {


            public RvViewHolder(ItemRvAnimBinding viewBinding) {
                super(viewBinding);
            }

            @Override
            public void bind(String s, int position, List<String> list) {
                super.bind(s, position, list);
                mViewBinding.tvContent.setText(s);
            }
        }
    }
}
