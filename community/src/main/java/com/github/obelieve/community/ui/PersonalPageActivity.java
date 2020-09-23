package com.github.obelieve.community.ui;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.PersonalPageTabAdapter;
import com.github.obelieve.community.bean.BBSUserInfoEntity;
import com.github.obelieve.community.ui.view.PersonalPageHeaderView;
import com.github.obelieve.community.viewmodel.PersonalPageViewModel;
import com.github.obelieve.event.message.MessageUnReadNumRefreshEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.utils.tab.StringTabLayoutHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalPageActivity extends ApiBaseActivity {

    @BindView(R.id.left_icon)
    ImageView leftIcon;
    @BindView(R.id.left_layout)
    RelativeLayout leftLayout;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.rl_tab)
    RelativeLayout rlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.scroll_container)
    LinearLayout scrollContainer;
    @BindView(R.id.view_header)
    PersonalPageHeaderView viewHeader;


    PersonalPageViewModel mViewModel;
    BBSUserInfoEntity mBBSUserInfoEntity;
    StringTabLayoutHelper mStringTabLayoutHelper;

    private int mUserId;
    private boolean mCurUser;
    private boolean mRefreshUnRead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        mLightStatusBar = false;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_personal_page;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mUserId = getIntent().getIntExtra("userId", 0);
        mViewModel = ViewModelProviders.of(this).get(PersonalPageViewModel.class);
        mViewModel.getUserInfo(mActivity, mUserId);
        mViewModel.getBbsUserInfoEntityMutableLiveData().observe(this, new Observer<BBSUserInfoEntity>() {
            @Override
            public void onChanged(BBSUserInfoEntity bbsUserInfoEntity) {
                mBBSUserInfoEntity = bbsUserInfoEntity;
                loadData(mBBSUserInfoEntity);
            }
        });
        UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
        int userId = userEntity != null ? userEntity.user_id : 0;
        mCurUser = mUserId == userId;
        if (mStringTabLayoutHelper == null) {
            mStringTabLayoutHelper = new StringTabLayoutHelper();
        }
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appbar.getTotalScrollRange() / 2.2) {
                    toolbar.setBackgroundResource(R.color.common_white);
                    title.setTextColor(getResources().getColor(R.color.common_title));
                    leftIcon.setBackgroundResource(R.drawable.return_black);
                    // 设置状态栏字体黑色
                    StatusBarUtil.setWindowLightStatusBar(mActivity, true);
                    title.setText(mBBSUserInfoEntity != null ? mBBSUserInfoEntity.getNickname() : "");
                } else {
                    title.setTextColor(getResources().getColor(R.color.common_white));
                    toolbar.setBackgroundResource(R.color.transparent);
                    leftIcon.setBackgroundResource(R.drawable.return_white);
                    StatusBarUtil.setWindowLightStatusBar(mActivity, false);
                    title.setText("");
                }
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    rlTab.setBackgroundResource(R.color.common_white);
                } else {
                    rlTab.setBackgroundResource(R.color.transparent);
                }
            }
        });
        tlTab.setTabRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
        vpContent.setAdapter(new PersonalPageTabAdapter(getSupportFragmentManager(), mUserId, mCurUser));
        mStringTabLayoutHelper.init(vpContent, tlTab, Arrays.asList(mCurUser ? "我的动态" : "TA的动态", mCurUser ? "我的点赞" : "TA的点赞"), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mRefreshUnRead) {
            mViewModel.getUserInfo(mActivity, mUserId);
            mRefreshUnRead = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUnReadNumRefreshEvent(MessageUnReadNumRefreshEvent event) {
        if (event.isResumeRefresh()) {
            mRefreshUnRead = true;
        } else {
            mViewModel.getUserInfo(mActivity, mUserId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadData(BBSUserInfoEntity entity) {
        if (entity == null)
            return;
        viewHeader.loadData(entity);
    }


    @OnClick({R.id.left_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }
}
