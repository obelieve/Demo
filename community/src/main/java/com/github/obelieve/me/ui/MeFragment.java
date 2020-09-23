package com.github.obelieve.me.ui;

import android.text.TextUtils;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.obelieve.community.R;
import com.github.obelieve.event.login.LoginNotifyEvent;
import com.github.obelieve.event.login.LogoutNotifyEvent;
import com.github.obelieve.event.login.UserInfoRefreshNotifyEvent;
import com.github.obelieve.login.LoginTypeActivity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.main.UserNavInfoEntity;
import com.github.obelieve.me.adapter.MeFAdapter;
import com.github.obelieve.me.bean.CenterMenuEntity;
import com.github.obelieve.me.bean.MenuDataEntity;
import com.github.obelieve.me.ui.view.MeHeaderView;
import com.github.obelieve.me.viewmodel.MeViewModel;
import com.github.obelieve.repository.cache.PreferenceUtil;
import com.github.obelieve.repository.cache.UserHelper;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.base.ApiBaseStatusBarFragment;
import com.zxy.frame.utils.LogUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/9/3
 */
public class MeFragment extends ApiBaseStatusBarFragment {

    @BindView(R.id.view_status_bar)
    View statusBarView;

    @BindView(R.id.view_me_header)
    MeHeaderView viewMeHeader;
    @BindView(R.id.cv_nav_up)
    CardView cvNavUp;
    @BindView(R.id.rv_nav_up)
    RecyclerView rvNavUp;
    @BindView(R.id.rv_nav_down)
    RecyclerView rvNavDown;

    MeViewModel mMeViewModel;

    UserNavInfoEntity mCjUserNavInfoEntity;
    CenterMenuEntity mCjCenterMenuEntity;
    MeFAdapter mMeFAdapter;
    MeFAdapter mMeNavUpAdapter;
    int loginType = -1;//0:手机,1:微信,2:QQ

    @Override
    public int layoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        super.initView();
        initViewModel();
        mMeFAdapter = new MeFAdapter(mActivity);
        mMeNavUpAdapter = new MeFAdapter(mActivity);
        rvNavUp.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNavDown.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNavUp.setAdapter(mMeNavUpAdapter);
        rvNavDown.setAdapter(mMeFAdapter);
        mMeFAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<MenuDataEntity>() {
            @Override
            public void onItemClick(View view, MenuDataEntity menuDataEntity, int position) {
                itemClick(menuDataEntity);
            }
        });
        mMeNavUpAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<MenuDataEntity>() {
            @Override
            public void onItemClick(View view, MenuDataEntity menuItemBean, int position) {
                itemClick(menuItemBean);
            }
        });
        viewMeHeader.loadDefUserData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMeViewModel.requestData(mActivity);
    }

    private void initViewModel() {
        mMeViewModel = ViewModelProviders.of(this).get(MeViewModel.class);
        mMeViewModel.getShowProgressMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (mActivity instanceof ApiBaseActivity) {
                        ApiBaseActivity activity = (ApiBaseActivity) mActivity;
                        activity.showLoading();
                    }
                } else {
                    if (mActivity instanceof ApiBaseActivity) {
                        ApiBaseActivity activity = (ApiBaseActivity) mActivity;
                        activity.dismissLoading();
                    }
                }
            }
        });
        mMeViewModel.getUserNavInfoEntityMutableLiveData().observe(this, cjUserNavInfoEntity -> {
            mCjUserNavInfoEntity = cjUserNavInfoEntity;
            loadData(mCjUserNavInfoEntity);
        });
        mMeViewModel.getCenterMenuEntityMutableLiveData().observe(this, cjCenterMenuEntity -> {
            mCjCenterMenuEntity = cjCenterMenuEntity;
            loadData(mCjCenterMenuEntity);
        });
        mMeViewModel.getErrorMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
        mMeViewModel.getUserEntityMutableLiveData().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity entity) {
                if (entity != null) {
                    if (entity.do_type.equals("login")) {
                        UserHelper.getInstance().getUserInfo(mActivity);
                        PreferenceUtil.putInteger(mActivity, PreferenceConst.SP_LOGIN_TYPE, loginType);
                        EventBus.getDefault().post(new LoginNotifyEvent());
                    } else if (entity.do_type.equals("wechat") || entity.do_type.equals("qq")) {
                        LoginTypeActivity.startBindPhone(mActivity, entity.open_id, entity.do_type);
                    }
                }
            }
        });
    }

    private void loadData(UserNavInfoEntity cjUserNavInfoEntity) {
        viewMeHeader.loadData(cjUserNavInfoEntity);
    }

    private void loadData(CenterMenuEntity centerMenuEntity) {
        if (centerMenuEntity == null)
            return;
        if (centerMenuEntity.getMenus() != null) {
            mMeNavUpAdapter.getDataHolder().setList(centerMenuEntity.getMenus().getMenu_up());
            ArrayList<MenuDataEntity> list = new ArrayList<>();
            list.addAll(centerMenuEntity.getMenus().getMenu_down());
            mMeFAdapter.getDataHolder().setList(list);
        }
        if (centerMenuEntity.getMenus() != null &&
                centerMenuEntity.getMenus().getMenu_up() != null &&
                centerMenuEntity.getMenus().getMenu_up().size() > 0) {
            cvNavUp.setVisibility(centerMenuEntity.getMenus().getRed_status() == 1 ? View.VISIBLE : View.GONE);
        }
    }

    public void itemClick(MenuDataEntity menuDataEntity) {
        //判断是否需要登入
        if (1 == menuDataEntity.getMenu_need_login() && TextUtils.isEmpty(SystemValue.token)) {
            //登入
            ActivityUtil.gotoLoginActivity(mActivity);
            return;
        }
        //是否可点击
        if (0 == menuDataEntity.getMenu_is_hit()) {
            ToastUtil.show("功能开发中");
            return;
        }

        // 网页
        if (!"".equals(menuDataEntity.getMenu_url()) && menuDataEntity.getMenu_url().contains("http")) {
            //WebViewActivityI.startActivity(getActivity(), menuDataEntity.getMenu_url(), "");
            return;
        }

        switch (menuDataEntity.getMenu_ident()) {
            case "plan"://已购方案
                // PurchasedActivity.start(getActivity());
            case "money"://钱包
                //CoinDetailActivity.start(getActivity());
            case "notice":
                //系统公告
//                startActivity(new Intent(getActivity(), NoticeListActivity.class));
            case "feedback":
                //问题反馈
//                startActivity(new Intent(getActivity(), FeedbackActivityI.class));
            case "planuser":
                //关注的大咖
//                RecommendFollowActivity.start(getActivity());
//                Statistics.buttonClick(mContext, Statistics.PROFILE_EXPERT_FOLLOW);
                ToastUtil.show("暂无功能");
                break;
            case "person":
                //个人主页
                if (mCjUserNavInfoEntity != null) {
                    LogUtil.e("user_id", mCjUserNavInfoEntity.getUser_id() + "");
                    ActivityUtil.gotoPersonalPageActivity(getActivity(), mCjUserNavInfoEntity.getUser_id());
                }
                break;
            case "setup":
                //系统设置
                if (TextUtils.isEmpty(SystemValue.token)) {
                    ActivityUtil.gotoLoginActivity(mActivity);
                    return;
                }
                ActivityUtil.gotoSettingsActivity(mActivity);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoRefreshNotifyEvent(UserInfoRefreshNotifyEvent event) {
        mMeViewModel.userNavInfo(mActivity,true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginNotifyEvent(LoginNotifyEvent event) {
        mMeViewModel.userNavInfo(mActivity,true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutNotifyEvent(LogoutNotifyEvent event) {
        viewMeHeader.refreshVisibilityLogin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public View statusBarView() {
        return statusBarView;
    }

    @Override
    public boolean statusBarLight() {
        return true;
    }


}