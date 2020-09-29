package com.github.obelieve.me.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.obelieve.community.R;
import com.github.obelieve.login.LoginTypeActivity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.main.UserNavInfoEntity;
import com.github.obelieve.net.UrlConst;
import com.github.obelieve.repository.cache.UserHelper;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.zxy.frame.utils.SPUtil;
import com.zxy.frame.utils.image.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeHeaderView extends FrameLayout {

    //已登录
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.iv_level)
    ImageView ivLevel;

    //未登录
    @BindView(R.id.fl_no_login)
    FrameLayout flNoLogin;
    @BindView(R.id.tv_phone_login)
    TextView tvPhoneLogin;
    @BindView(R.id.tv_login_tip)
    TextView tvLoginTip;

    private Activity mActivity;
    private String mAvatar;

    public MeHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mActivity = (Activity) context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_me_header, this, true);
        ButterKnife.bind(this, view);
        SpannableStringBuilder style = new SpannableStringBuilder();
        tvLoginTip.setMovementMethod(LinkMovementMethod.getInstance());
        style.append("登录即同意《软件许可及隐私政策》");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String privacy_url = SPUtil.getInstance().getString(PreferenceConst.COPYRIGHT_URL, UrlConst.PRIVACY_URL);
                ActivityUtil.gotoWebActivity(mActivity, privacy_url, getResources().getString(R.string.settings_privacy_policy), false);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(clickableSpan, 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new BackgroundColorSpan(Color.parseColor("#00000000")), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00C763"));
        style.setSpan(foregroundColorSpan, 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoginTip.setText(style);
    }

    public void loadDefUserData() {
        refreshVisibilityLogin();
        UserEntity userEntity = UserHelper.getInstance().getUserEntity();
        if (userEntity != null) {
            loadFaceImage(userEntity.avatar);
            tvNickname.setText(!TextUtils.isEmpty(userEntity.nickname) ? userEntity.nickname : userEntity.mobile);
        }
    }

    public void loadData(UserNavInfoEntity entity) {
        if (entity == null)
            return;
        refreshVisibilityLogin();
        loadFaceImage(entity.getAvatar());
        tvNickname.setText(!TextUtils.isEmpty(entity.getNickname()) ? entity.getNickname() : entity.getMobile());
        ivLevel.setImageResource(R.drawable.icon_level_0);
    }


    public void refreshVisibilityLogin() {
        if (TextUtils.isEmpty(SystemValue.token)) {
            mAvatar = null;
            flNoLogin.setVisibility(VISIBLE);
            flLogin.setVisibility(GONE);

            rlLogin.setVisibility(View.GONE);
            ivFace.setImageResource(R.drawable.me_face);
        } else {
            flNoLogin.setVisibility(View.GONE);
            flLogin.setVisibility(VISIBLE);
            rlLogin.setVisibility(View.VISIBLE);
        }
    }

    private void loadFaceImage(String avatar) {
        if (mAvatar == null) {
            GlideUtil.loadImageCircle(mActivity, avatar, ivFace, R.drawable.me_face);
        } else {
            if (!mAvatar.equals(avatar)) {
                GlideUtil.loadImageCircle(mActivity, avatar, ivFace, R.drawable.me_face);
            }
        }
        mAvatar = avatar;
    }

    @OnClick({R.id.rl_login, R.id.iv_level, R.id.iv_login, R.id.tv_phone_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_login:
                ActivityUtil.gotoUserInfoActivity(mActivity, false);
                break;
            case R.id.iv_level:
                //我的等级页面
//                LevelIndexActivity.start(mActivity);
                break;
            case R.id.tv_phone_login:
            case R.id.iv_login:
                LoginTypeActivity.startLoginInputPhone(mActivity);
                break;
        }
    }
}
