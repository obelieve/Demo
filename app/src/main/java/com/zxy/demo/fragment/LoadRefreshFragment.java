package com.zxy.demo.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zxy.demo.R;
import com.zxy.demo.adapter.viewholder.LoadRefreshViewHolder;
import com.zxy.demo.databinding.FragmentLoadRefreshBinding;
import com.zxy.demo.databinding.ViewholderLoadRefreshBinding;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.demo.viewmodel.LoadRefreshViewModel;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.image.GlideApp;

import java.util.List;

public class LoadRefreshFragment extends ApiBaseFragment<FragmentLoadRefreshBinding> {


    LoadRefreshAdapter mMainAdapter;

    LoadRefreshViewModel mLoadRefreshViewModel;

    @Override
    protected void initView() {
        mLoadRefreshViewModel = ViewModelProviders.of(this).get(LoadRefreshViewModel.class);
        mMainAdapter = new LoadRefreshAdapter(getContext());
        mMainAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, mViewBinding.root.srlContent, false));
        mMainAdapter.setLoadMoreListener(mViewBinding.root.rvContent, () -> mLoadRefreshViewModel.square_post(true));
        mViewBinding.root.rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.root.rvContent.addItemDecoration(new VerticalItemDivider());
        mViewBinding.root.rvContent.setAdapter(mMainAdapter);
        mViewBinding.root.srlContent.setOnRefreshListener(() -> mLoadRefreshViewModel.square_post(false));
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
                mViewBinding.root.srlContent.setRefreshing(aBoolean);
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
            return new LoadRefreshViewHolder(ViewholderLoadRefreshBinding.inflate(LayoutInflater.from(parent.getContext()),
                    parent,false));
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            LoadRefreshViewHolder holder1 = ((LoadRefreshViewHolder) holder);
            holder1.getViewBinding().tvName.setText(getDataHolder().getList().get(position).getNickname());
            holder1.getViewBinding().tvContent.setText(getDataHolder().getList().get(position).getContent());
            GlideApp.with(LoadRefreshFragment.this).load( getDataHolder().getList().get(position).getAvatar())
                    .defImage().into(holder1.getViewBinding().ivImage);
        }

    }
}
