package com.news.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Admin
 * on 2020/6/10
 */
public class RvAnimationFragment extends BaseFragment {


    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.view_bottom)
    View viewBottom;

    RvAdapter mRvAdapter;
    SpringAnimation mSpringAnimation;

    @Override
    public int layoutId() {
        return R.layout.fragment_rv_animation;
    }

    @Override
    protected void initView() {
        mRvAdapter = new RvAdapter(getActivity());
        mRvAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<String>() {
            @Override
            public void onItemClick(View view, String t, int position) {
                ToastUtil.show(t);
            }
        });
        rvContent.setAdapter(mRvAdapter);
        mRvAdapter.getDataHolder().setList(Arrays.asList(
                "111111111111111111111111", "222222222222222222222222", "333333333333333333333333", "444444444444444444444444", "555555555555555555555555",
                "666666666666666666666666", "777777777777777777777777777777", "888888888888888888888888888888", "999999999999999999999999999999",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbb", "cccccccccccccccccccccccc", "dddddddddddddddddddddddd", "eeeeeeeeeeeeeeeeeeeeeeee",
                "ffffffffffffffffffffffff", "gggggggggggggggggggggggg", "hhhhhhhhhhhhhhhhhhhhhhhh", "iiiiiiiiiiiiiiiiiiiiiiii",
                "jjjjjjjjjjjjjjjjjjjjjjjj", "kkkkkkkkkkkkkkkkkkkkkkkk", "llllllllllllllllllllllll", "mmmmmmmmmmmmmmmmmmmmmmmm", "nnnnnnnnnnnnnnnnnnnnnnnn",
                "oooooooooooooooooooooooooooooo", "pppppppppppppppppppppppp", "qqqqqqqqqqqqqqqqqqqqqqqq", "rrrrrrrrrrrrrrrrrrrrrrrr"));
        mSpringAnimation = new SpringAnimation(viewBottom, SpringAnimation.TRANSLATION_Y, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRvAdapter.release();
    }

    @OnClick(R.id.view_bottom)
    public void onViewClicked() {
        mSpringAnimation.cancel();
        mSpringAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        mSpringAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        mSpringAnimation.setStartVelocity(10000);
        mSpringAnimation.start();
    }

    public static class RvAdapter extends BaseRecyclerViewAdapter<String> {

        boolean mIsOnlyFirstAnimation = false;
        int mLastPosition = -1;

        public RvAdapter(Context context) {
            super(context);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RvViewHolder(parent, R.layout.item_rv_anim);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof RvViewHolder) {
                holder.bind(getDataHolder().getList().get(position));
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

        public static class RvViewHolder extends BaseViewHolder<String> {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public RvViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
            }

            @Override
            public void bind(String s) {
                tvContent.setText(s);
            }
        }
    }
}
