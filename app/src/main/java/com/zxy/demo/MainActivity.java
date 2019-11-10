package com.zxy.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxy.demo.model.SquareListModel;
import com.zxy.demo.viewmodel.MainViewModel;
import com.zxy.frame.adapter.BaseQuickRecyclerViewAdapter;
import com.zxy.frame.adapter.func.Func;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;

    MainAdapter mMainAdapter;

    MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainAdapter = new MainAdapter(this);
        mMainAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_empty, null));
        mMainAdapter.setOnLoadMoreListener(new Func.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mMainViewModel.square_post(true);
            }
        }, mRvContent);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new VerticalItemDivider());
        mRvContent.setAdapter(mMainAdapter);
        mSrlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMainViewModel.square_post(false);
            }
        });
        mMainViewModel.getListMutableLiveData().observe(this, new Observer<List<SquareListModel.DataBean.PostListBean>>() {
            @Override
            public void onChanged(List<SquareListModel.DataBean.PostListBean> postListBeans) {
                mMainAdapter.setNewData(postListBeans);
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
                if(loadMoreModel.isCompleted()){
                    mMainAdapter.loadMoreComplete();
                }else if(loadMoreModel.isEnd()){
                    mMainAdapter.loadMoreEnd();
                }else{
                    mMainAdapter.loadMoreFail();
                }
            }
        });
    }

    public class MainAdapter extends BaseQuickRecyclerViewAdapter<SquareListModel.DataBean.PostListBean, BaseViewHolder> {

        public MainAdapter(Context context) {
            super(context);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, SquareListModel.DataBean.PostListBean item) {
            holder.setText(R.id.tv_name, item.getNickname())
                    .setText(R.id.tv_content, item.getContent());
            Glide.with(getContext()).load(item.getAvatar()).into((ImageView) holder.getView(R.id.iv_image));
        }
    }
}



