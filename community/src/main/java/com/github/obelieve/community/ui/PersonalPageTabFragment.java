package com.github.obelieve.community.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.UpdatesAdapter2;
import com.github.obelieve.community.adapter.decoration.CommunityStaggeredGridItemDecoration;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.viewmodel.PersonalPageViewModel;
import com.github.obelieve.event.community.CommunityItemChangedEvent;
import com.github.obelieve.event.community.PostFilterCacheEvent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalPageTabFragment extends ApiBaseFragment {
    public static final String INDEX_EXTRA = "index_extra";
    public static final String USER_ID_EXTRA = "user_id_extra";
    public static final int TRENDS_INDEX = 0;
    public static final int ZAN_INDEX = 1;

    @BindView(R.id.recycler_updates)
    RecyclerView recyclerUpdates;
    @BindView(R.id.srl_refresh_layout)
    SmartRefreshLayout srlRefreshLayout;
    @BindView(R.id.fl_empty)
    FrameLayout flEmpty;

    PersonalPageViewModel mViewModel;
    UpdatesAdapter2 mAdapter;

    int mType;
    int mUserId;
    private int mCurrentPosition;
    private SquareListsEntity.PostListBean mCurrentPostListBean;


    public static PersonalPageTabFragment getInstance(int position, int userId) {
        PersonalPageTabFragment fragment = new PersonalPageTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX_EXTRA, position);
        bundle.putInt(USER_ID_EXTRA, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_personal_page_tab;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mType = getArguments().getInt(INDEX_EXTRA);
            mUserId = getArguments().getInt(USER_ID_EXTRA);
        }
        mViewModel = ViewModelProviders.of(this).get(PersonalPageViewModel.class);
        ((DefaultItemAnimator) recyclerUpdates.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerUpdates.getItemAnimator().setChangeDuration(0);
        recyclerUpdates.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerUpdates.addItemDecoration(new CommunityStaggeredGridItemDecoration());
        mAdapter = new UpdatesAdapter2((Activity) mContext, PersonalPageTabFragment.class.getSimpleName());
        mAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<SquareListsEntity.PostListBean>() {
            @Override
            public void onItemClick(View view, SquareListsEntity.PostListBean bean, int position) {
                mCurrentPosition = position;
                mCurrentPostListBean = bean;
            }
        });
        recyclerUpdates.setAdapter(mAdapter);
        EventBus.getDefault().register(this);

        //下拉刷新
        srlRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                switch (mType) {
                    case TRENDS_INDEX:
                        mViewModel.getTrends(mActivity, mUserId, false);
                        break;
                    case ZAN_INDEX:
                        mViewModel.getZan(mActivity, mUserId, false);
                        break;
                }
            }
        });
        srlRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                switch (mType) {
                    case TRENDS_INDEX:
                        mViewModel.getTrends(mActivity, mUserId, true);
                        break;
                    case ZAN_INDEX:
                        mViewModel.getZan(mActivity, mUserId, true);
                        break;
                }
            }
        });
        //获取数据成功
        mViewModel.getLoadMoreMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    srlRefreshLayout.finishLoadMore();
                } else {
                    srlRefreshLayout.finishRefresh();
                }
            }
        });
        recyclerUpdates.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //todo 待优化
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    GlideApp.with(mContext).resumeRequests();
//                } else {
//                    GlideApp.with(mContext).pauseRequests();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        switch (mType) {
            case TRENDS_INDEX:
                mViewModel.getBbsUserTrendsEntityMutableLiveData().observe(this, seasonMenuEntity -> {
                    List<SquareListsEntity.PostListBean> post_list = seasonMenuEntity.getPost_list();
                    if (!mViewModel.isLoadMore()) {
                        mAdapter.getDataHolder().setList(post_list);
                    } else {
                        mAdapter.getDataHolder().addAll(post_list);
                    }
                    if (mAdapter.getItemCount() == 0) {
                        flEmpty.setVisibility(View.VISIBLE);
                    } else {
                        flEmpty.setVisibility(View.GONE);
                    }
                    //是否有一下页数据
                    srlRefreshLayout.setEnableLoadMore(seasonMenuEntity.getHas_next_page() == 1);
                });
                break;
            case ZAN_INDEX:
                mViewModel.getBbsUserZanEntityMutableLiveData().observe(this, seasonMenuEntity -> {
                    List<SquareListsEntity.PostListBean> post_list = seasonMenuEntity.getPost_list();
                    if (!mViewModel.isLoadMore()) {
                        mAdapter.getDataHolder().setList(post_list);
                    } else {
                        mAdapter.getDataHolder().addAll(post_list);
                    }
                    if (mAdapter.getItemCount() == 0) {
                        flEmpty.setVisibility(View.VISIBLE);
                    } else {
                        flEmpty.setVisibility(View.GONE);
                    }
                    //是否有一下页数据
                    srlRefreshLayout.setEnableLoadMore(seasonMenuEntity.getHas_next_page() == 1);
                });
                break;
        }
        srlRefreshLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunityItemChangedEvent(CommunityItemChangedEvent event) {
        if (mCurrentPosition >= 0 && mCurrentPostListBean != null && getUserVisibleHint()) {
            mCurrentPostListBean.setPc_num(event.getCommentCount());
            mCurrentPostListBean.setZan_num(event.getZanCount());
            mCurrentPostListBean.setIs_zan(event.isZan() ? 1 : 0);
            mAdapter.notifyItemChanged(mCurrentPosition);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostFilterCacheEvent(PostFilterCacheEvent event) {
        if (!PersonalPageTabFragment.class.getSimpleName().equals(event.getTag()))
            return;
        Set<SquareListsEntity.PostListBean> beans = new HashSet<>();
        if (mAdapter != null && mAdapter.getDataHolder().getList() != null) {
            for (SquareListsEntity.PostListBean bean : mAdapter.getDataHolder().getList()) {
                if (event.getPost_id() == 0) {
                    if (bean.getUser_id() == event.getUser_id()) {
                        beans.add(bean);
                    }
                } else {
                    if (bean.getPost_id() == event.getPost_id()) {
                        beans.add(bean);
                    }
                }
            }
            for (SquareListsEntity.PostListBean bean : beans) {
                mAdapter.getDataHolder().getList().remove(bean);
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
