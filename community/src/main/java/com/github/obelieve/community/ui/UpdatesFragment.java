package com.github.obelieve.community.ui;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.UpdatesAdapter2;
import com.github.obelieve.community.adapter.decoration.CommunityStaggeredGridItemDecoration;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.viewmodel.UpdatesViewModel;
import com.github.obelieve.event.community.CommunityItemChangedEvent;
import com.github.obelieve.event.community.CommunityScrollToTopNotifyEvent;
import com.github.obelieve.event.community.PostFilterCacheEvent;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFragment extends ApiBaseFragment {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout srlContent;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;


    UpdatesViewModel mUpdatesViewModel;
    UpdatesAdapter2 mUpdatesAdapter;

    int mCurrentPosition;
    SquareListsEntity.PostListBean mCurrentPostListBean;
    boolean mFirstInit = true;
    /**
     * 0:广场  默认
     * 1:精选
     * 2:搜索
     */
    int type;

    public UpdatesFragment() {

    }

    public UpdatesFragment(int type) {
        this.type = type;
    }


    @Override
    public int layoutId() {
        return R.layout.fragment_updates;
    }

    @Override
    protected void initView() {
        if (type == 1) {
            tv_no_data.setText("暂无相关资料");
            mUpdatesViewModel = ViewModelProviders.of(this).get(UpdatesViewModel.class);
        } else if (type == 2) {
            tv_no_data.setText("未搜索到你想要的");
            mUpdatesViewModel = ViewModelProviders.of(this).get(UpdatesViewModel.class);
        } else {
            tv_no_data.setText("暂无相关资料");
            mUpdatesViewModel = ViewModelProviders.of(this).get(UpdatesViewModel.class);
        }
        mUpdatesAdapter = new UpdatesAdapter2(getActivity(), UpdatesFragment.class.getSimpleName());
        mUpdatesAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<SquareListsEntity.PostListBean>() {
            @Override
            public void onItemClick(View view, SquareListsEntity.PostListBean bean, int position) {
                mCurrentPosition = position;
                mCurrentPostListBean = bean;
            }
        });
        mUpdatesAdapter.setLoadMoreListener(rvContent, new BaseRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshData(true);
            }
        });
        rvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvContent.addItemDecoration(new CommunityStaggeredGridItemDecoration());
        ((DefaultItemAnimator) rvContent.getItemAnimator()).setSupportsChangeAnimations(false);
        rvContent.setAdapter(mUpdatesAdapter);
        //下拉刷新
        srlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(false);
            }
        });

        //获取数据成功
        mUpdatesViewModel.getRefreshLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                srlContent.setRefreshing(false);
            }
        });
        mUpdatesViewModel.getLoadMoreStatusMutableLiveData().observe(this, new Observer<BaseRecyclerViewAdapter.LoadMoreStatus>() {
            @Override
            public void onChanged(BaseRecyclerViewAdapter.LoadMoreStatus status) {
                switch (status){
                    case LOADING:
                        mUpdatesAdapter.loadMoreLoading();
                        break;
                    case END:
                        mUpdatesAdapter.loadMoreEnd();
                        break;
                    case ERROR:
                        mUpdatesAdapter.loadMoreError();
                        break;
                }
            }
        });

        mUpdatesViewModel.getSquareListsEntityMutableLiveData().observe(this, seasonMenuEntity -> {
            List<SquareListsEntity.PostListBean> post_list = seasonMenuEntity.getPost_list();
            if (seasonMenuEntity.getCurrent_page() == 1) {
                if (mFirstInit) {
                    rvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    mFirstInit = false;
                }
                mUpdatesAdapter.getDataHolder().setList(post_list);
            } else {
                mUpdatesAdapter.getDataHolder().addAll(post_list);
            }
            if (mUpdatesAdapter.getItemCount() == 0) {
                tv_no_data.setVisibility(View.VISIBLE);
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
        });
        mFirstInit = true;
        refreshData(false);
        EventBus.getDefault().register(this);
    }

    private void refreshData(boolean b) {
        if (type == 1) {
            mUpdatesViewModel.getChoiceness(mActivity, b);
        } else {
            mUpdatesViewModel.getSqareUpates(mActivity, b);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunityItemChangedEvent(CommunityItemChangedEvent event){
        if (mCurrentPosition >= 0 && mCurrentPostListBean != null && getUserVisibleHint()) {
            mCurrentPostListBean.setPc_num(event.getCommentCount());
            mCurrentPostListBean.setZan_num(event.getZanCount());
            mCurrentPostListBean.setIs_zan(event.isZan() ? 1 : 0);
            mUpdatesAdapter.notifyItemChanged(mCurrentPosition);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunityScrollToTopNotifyEvent(CommunityScrollToTopNotifyEvent event) {
        if (event.getType() == type) {
            rvContent.smoothScrollToPosition(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostFilterCacheEvent(PostFilterCacheEvent event) {
        if (!UpdatesFragment.class.getSimpleName().equals(event.getTag()))
            return;
        Set<SquareListsEntity.PostListBean> beans = new HashSet<>();
        if (mUpdatesAdapter != null) {
            for (SquareListsEntity.PostListBean bean : mUpdatesAdapter.getDataHolder().getList()) {
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
                mUpdatesAdapter.getDataHolder().getList().remove(bean);
            }
            mUpdatesAdapter.notifyDataSetChanged();
        }
    }


    public void startRefresh() {
        refreshData(false);
    }

    public void clearData() {
        if (mUpdatesAdapter != null) {
            mUpdatesAdapter.getDataHolder().setList(new ArrayList<>());
        }
    }
}
