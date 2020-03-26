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
import com.zxy.demo.adapter.viewholder.MainViewHolder;
import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.demo.viewmodel.MainViewModel;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.image.GlideUtil;

import java.util.List;

import butterknife.BindView;

public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;

    MainAdapter mMainAdapter;

    MainViewModel mMainViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainAdapter = new MainAdapter(getContext());
        mMainAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.view_empty, mSrlContent, false));
        mMainAdapter.setLoadMoreListener(mRvContent, () -> mMainViewModel.square_post(true));
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new VerticalItemDivider());
        mRvContent.setAdapter(mMainAdapter);
        mSrlContent.setOnRefreshListener(() -> mMainViewModel.square_post(false));
        observerData();
    }

    private void observerData() {
        mMainViewModel.getListMutableLiveData().observe(this, new Observer<List<SquarePostEntity.PostListBean>>() {
            @Override
            public void onChanged(List<SquarePostEntity.PostListBean> postListBeans) {
                mMainAdapter.getDataHolder().setList(postListBeans).notifyDataSetChanged();
            }
        });
        mMainViewModel.getRefreshLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mSrlContent.setRefreshing(aBoolean);
            }
        });
        mMainViewModel.getLoadMoreLiveData().observe(this, new Observer<MainViewModel.LoadMoreModel>() {
            @Override
            public void onChanged(MainViewModel.LoadMoreModel loadMoreModel) {
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

    public class MainAdapter extends BaseRecyclerViewAdapter<SquarePostEntity.PostListBean> {

        public MainAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(parent, R.layout.viewholder_main);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            MainViewHolder holder1 = ((MainViewHolder) holder);
            holder1.mTvName.setText(getDataHolder().getList().get(position).getNickname());
            holder1.mTvName.setText(getDataHolder().getList().get(position).getNickname());
            GlideUtil.loadImage(getContext(), getDataHolder().getList().get(position).getAvatar(), holder1.mIvImage);
        }

    }
}
