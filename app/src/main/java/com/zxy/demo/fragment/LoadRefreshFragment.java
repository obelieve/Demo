package com.zxy.demo.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zxy.demo.R;
import com.zxy.demo.adapter.viewholder.LoadRefreshViewHolder;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.demo.viewmodel.LoadRefreshViewModel;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.image.GlideApp;

import java.util.List;

import butterknife.BindView;

public class LoadRefreshFragment extends ApiBaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;

    LoadRefreshAdapter mMainAdapter;

    LoadRefreshViewModel mLoadRefreshViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_load_refresh;
    }

    @Override
    protected void initView() {
        mLoadRefreshViewModel = ViewModelProviders.of(this).get(LoadRefreshViewModel.class);
        mMainAdapter = new LoadRefreshAdapter(getContext());
        mMainAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, mSrlContent, false));
        mMainAdapter.setLoadMoreListener(mRvContent, () -> mLoadRefreshViewModel.square_post(true));
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new VerticalItemDivider());
        mRvContent.setAdapter(mMainAdapter);
        mSrlContent.setOnRefreshListener(() -> mLoadRefreshViewModel.square_post(false));
        observerData();
    }

    private void observerData() {
        mLoadRefreshViewModel.getListMutableLiveData().observe(this, new Observer<List<SquarePostEntity.PostListBean>>() {
            @Override
            public void onChanged(List<SquarePostEntity.PostListBean> postListBeans) {
                mMainAdapter.getDataHolder().setList(postListBeans);
            }
        });
        mLoadRefreshViewModel.getRefreshLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mSrlContent.setRefreshing(aBoolean);
            }
        });
        mLoadRefreshViewModel.getLoadMoreLiveData().observe(this, new Observer<LoadRefreshViewModel.LoadMoreModel>() {
            @Override
            public void onChanged(LoadRefreshViewModel.LoadMoreModel loadMoreModel) {
                if (loadMoreModel.isCompleted()) {
                    mMainAdapter.loadMoreLoading();
                } else if (loadMoreModel.isEnd()) {
                    mMainAdapter.loadMoreEnd();
                } else {
                    mMainAdapter.loadMoreError();
                }
            }
        });
    }

    public class LoadRefreshAdapter extends BaseRecyclerViewAdapter<SquarePostEntity.PostListBean> {

        public LoadRefreshAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new LoadRefreshViewHolder(parent, R.layout.viewholder_load_refresh);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            LoadRefreshViewHolder holder1 = ((LoadRefreshViewHolder) holder);
            holder1.mTvName.setText(getDataHolder().getList().get(position).getNickname());
            holder1.mTvName.setText(getDataHolder().getList().get(position).getNickname());
            GlideApp.with(LoadRefreshFragment.this).load( getDataHolder().getList().get(position).getAvatar())
                    .defImage().into(holder1.mIvImage);
        }

    }
}
