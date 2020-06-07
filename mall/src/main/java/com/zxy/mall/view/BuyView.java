package com.zxy.mall.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zxy.mall.R;
import com.zxy.utility.SystemUtil;


public class BuyView extends FrameLayout implements View.OnClickListener {

    private final int TURN_LEFT_MODE = 0;
    private final int TURN_RIGHT_MODE = 1;
    private State mLastState = State.INITIAL;

    private ImageView mIvAdd;
    private ImageView mIvSub;
    private TextView mTvNum;
    private TextView mTvTitle;
    private ConstraintLayout mClContent;
    private FrameLayout mFlAdd;

    private int mNum;
    private int mMaxNum;
    private boolean mAnimating;
    private AnimatorSet mAnimatorSet;
    private Callback mCallback;

    public BuyView(Context context) {
        this(context, null, 0);

    }

    public BuyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_buy, this, true);
        mFlAdd = findViewById(R.id.fl_add);
        mIvAdd = findViewById(R.id.iv_add);
        mIvSub = findViewById(R.id.iv_sub);
        mTvNum = findViewById(R.id.tv_num);
        mTvTitle = findViewById(R.id.tv_title);
        mClContent = findViewById(R.id.cl_content);
        mClContent.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mIvSub.setOnClickListener(this);
        mClContent.setClickable(true);
        mIvSub.setClickable(false);
        mIvAdd.setSelected(false);
        mIvAdd.setClickable(false);
        mTvNum.setVisibility(GONE);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setMaxNum(int maxNum) {
        mMaxNum = maxNum;
    }

    public int getMaxNum() {
        return mMaxNum;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
        mLastState = State.INITIAL;
        if(mNum==getMaxNum()){
            setState(State.FULL_GOODS,false);
        }else if(mNum == 0){
            setState(State.INITIAL,false);
        }else{
            setState(State.ADD_GOODS,false);
        }
        mTvNum.setText(mNum+"");
    }

    @Override
    public void onClick(View v) {
        if (mAnimating)
            return;
        switch (v.getId()) {
            case R.id.cl_content:
                if (getMaxNum() == 1) {
                    setState(State.FULL_GOODS);
                } else {
                    setState(State.ADD_GOODS);
                }
                mNum = 1;
                mTvNum.setText(String.valueOf(1));
                if (mCallback != null) {
                    mCallback.onAdd(mNum);
                }
                break;
            case R.id.iv_add:
                if (!TextUtils.isEmpty(mTvNum.getText().toString())) {
                    if (Integer.valueOf(mTvNum.getText().toString()) + 1 == getMaxNum()) {
                        setState(State.FULL_GOODS);
                    }
                    mTvNum.setText(String.valueOf(Integer.valueOf(mTvNum.getText().toString()) + 1));
                    mNum = mNum + 1;
                    if (mCallback != null) {
                        mCallback.onAdd(mNum);
                    }
                }
                break;
            case R.id.iv_sub:
                if (!TextUtils.isEmpty(mTvNum.getText().toString())) {
                    if (Integer.valueOf(mTvNum.getText().toString()) == getMaxNum()) {
                        setState(State.ADD_GOODS);
                    }
                    if (Integer.valueOf(mTvNum.getText().toString()) == 1) {
                        setState(State.INITIAL);
                    } else {
                        mTvNum.setText(String.valueOf(Integer.valueOf(mTvNum.getText().toString()) - 1));
                    }
                    mNum = mNum - 1;
                    if (mCallback != null) {
                        mCallback.onRemove(mNum);
                    }
                }
                break;
        }
    }

    public enum State {

        INITIAL, ADD_GOODS, FULL_GOODS
    }
    public void setState(State state) {
        setState(state,true);
    }

    public void setState(State state,boolean anim) {
        switch (state) {
            case INITIAL:
                moveAnimation(TURN_LEFT_MODE,anim);
                mClContent.setClickable(true);
                mIvSub.setClickable(false);
                mIvAdd.setSelected(false);
                mIvAdd.setClickable(false);
                break;
            case ADD_GOODS:
                if (mLastState == State.INITIAL) {
                    moveAnimation(TURN_RIGHT_MODE,anim);
                }
                mClContent.setClickable(false);
                mIvSub.setClickable(true);
                mIvAdd.setSelected(false);
                mIvAdd.setClickable(true);
                break;
            case FULL_GOODS:
                if (mLastState == State.INITIAL) {
                    moveAnimation(TURN_RIGHT_MODE,anim);
                }
                mClContent.setClickable(false);
                mIvSub.setClickable(true);
                mIvAdd.setSelected(true);
                mIvAdd.setClickable(false);
                break;
        }
        mLastState = state;
    }

    /**
     * @param mode TURN_LEFT_MODE, TURN_RIGHT_MODE
     */
    private void moveAnimation(final int mode,boolean anim) {
        float subLeft = mIvSub.getLeft();
        float addX = mIvAdd.getX();
        float subX = mIvSub.getX();
        switch (mode) {
            case TURN_LEFT_MODE:
                mTvTitle.setVisibility(VISIBLE);
                mTvNum.setVisibility(GONE);
                break;
            case TURN_RIGHT_MODE:
                mTvTitle.setVisibility(GONE);
                break;
        }
        if(!anim){
            if(mode == TURN_LEFT_MODE){
                mFlAdd.setTranslationX(0);
                mIvAdd.setRotation(0);
                mIvAdd.setTranslationX(0);
                mIvSub.setRotation(0);
                mIvSub.setTranslationX(0);
            }else if (mode == TURN_RIGHT_MODE) {
                mFlAdd.setTranslationX(SystemUtil.dp2px(50));
                mTvNum.setVisibility(VISIBLE);
                mIvAdd.setRotation(360.0f);
                mIvAdd.setTranslationX(SystemUtil.dp2px(50));
                mIvSub.setRotation(360.0f);
                mIvSub.setTranslationX(-SystemUtil.dp2px(50));
            }
        }else{
            PropertyValuesHolder rotateHolder = PropertyValuesHolder.ofFloat("rotation", 0.0f, 360.0f);
            PropertyValuesHolder addHolder = PropertyValuesHolder.ofFloat("translationX", addX, subX);
            PropertyValuesHolder subHolder = PropertyValuesHolder.ofFloat("translationX", subX - subLeft, addX - subLeft);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mFlAdd, addHolder);
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(mIvAdd, rotateHolder, addHolder);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(mIvSub, rotateHolder, subHolder);
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.setDuration(300);
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnimating = false;
                    if (mode == TURN_RIGHT_MODE) {
                        mTvNum.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mAnimating = false;
                }
            });
            mAnimatorSet.playTogether(animator, animator1, animator2);
            mAnimatorSet.start();
        }

    }

    public void stopAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.removeAllListeners();
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    public interface Callback {
        void onAdd(int num);
        void onRemove(int num);
    }

}
