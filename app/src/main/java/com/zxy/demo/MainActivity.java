package com.zxy.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zxy.demo.adapter.viewholder.MainViewHolder;
import com.zxy.demo.model.SquareListModel;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.json.MGson;
import com.zxy.frame.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;

    MainAdapter mMainAdapter;

    int mCurPage = 1;
    boolean mHasNextPage = false;

    List<SquareListModel.DataBean.PostListBean> mPostListBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        mMainAdapter = new MainAdapter();
        mMainAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String json = response.body().string();
                            SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                            mCurPage = model.getData().getCurrent_page();
                            mMainAdapter.addData(model.getData().getPost_list());
                            if (model.getData().getHas_next_page() == 1) {
                                mCurPage++;
                                mMainAdapter.loadMoreComplete();
                            } else {
                                mMainAdapter.loadMoreEnd();
                            }
//                            mMainAdapter.getDataHolder().addAll(model.getData().getPost_list())
//                                    .notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }, mRvContent);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new VerticalItemDivider());
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.show("点击: view=" + view + " " + position);
            }
        });
        mRvContent.setAdapter(mMainAdapter);
        mSrlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurPage = 1;
                App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String json = response.body().string();
                            SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
//                            mMainAdapter.getDataHolder().addAll(model.getData().getPost_list())
//                                    .notifyDataSetChanged();
                            mMainAdapter.setNewData(model.getData().getPost_list());
                            mSrlContent.setRefreshing(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }


    public class MainAdapter extends BaseQuickAdapter<SquareListModel.DataBean.PostListBean, BaseViewHolder> {

        public MainAdapter() {
            super(R.layout.viewholder_main, mPostListBeans);
        }


        @Override
        protected void convert(@NonNull BaseViewHolder helper, SquareListModel.DataBean.PostListBean item) {
            helper.setText(R.id.tv_name, item.getNickname())
                    .setText(R.id.tv_content, item.getContent());
            Glide.with(mContext).load(item.getAvatar()).into((ImageView) helper.getView(R.id.iv_image));
        }


//        public static class MViewHolder extends BaseViewHolder{
//            @BindView(R.id.tv_name)
//            public TextView mTvName;
//            @BindView(R.id.tv_content)
//            public TextView mTvContent;
//            @BindView(R.id.iv_image)
//            public ImageView mIvImage;
//
//            public MViewHolder(View view) {
//                super(view);
//                ButterKnife.bind(this,itemView);
//            }
//        }
    }

    public static class MainAct {
        @BindView(R.id.rv_content)
        RecyclerView mRvContent;
        @BindView(R.id.trl_refresh)
        TwinklingRefreshLayout mTrlRefresh;

        MainAdapter mMainAdapter;

        int mCurPage = 1;
        boolean mHasNextPage = false;
        private List<SquareListModel.DataBean.PostListBean> mPostListBeans = new ArrayList<>();

        public static class MainAdapter extends BaseRecyclerViewAdapter<SquareListModel.DataBean.PostListBean, MainViewHolder> {

            public MainAdapter(Context context) {
                super(context);
            }

            @Override
            public MainViewHolder getViewHolder(ViewGroup parent, int viewDATAype) {
                return new MainViewHolder(parent, 0);
            }

            @Override
            public void loadViewHolder(MainViewHolder holder, int position) {
                SquareListModel.DataBean.PostListBean bean = getDataHolder().getList().get(position);
                holder.mTvName.setText(bean.getNickname());
                holder.mTvContent.setText(bean.getContent());
                Glide.with(getContext()).load(bean.getAvatar()).into(holder.mIvImage);
            }
        }

        private void init(Activity activity) {

            mMainAdapter = new MainAdapter(activity);
            mRvContent.setLayoutManager(new LinearLayoutManager(activity));
            mRvContent.addItemDecoration(new VerticalItemDivider());
            mRvContent.setAdapter(mMainAdapter);
            mTrlRefresh.setEnableRefresh(false);
            mTrlRefresh.setEnableLoadmore(false);
            mTrlRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
                public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                    mCurPage = 1;
                    App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String json = response.body().string();
                                SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                                boolean isNext = mCurPage == 3 ? false : true;//model.getData().getHas_next_page() == 1;
                                if (isNext) {
                                    mHasNextPage = true;
                                    mCurPage++;
                                } else {
                                    mHasNextPage = false;
                                }
                                if (model.getData() != null)
                                    mMainAdapter.getDataHolder()
                                            .setList(model.getData().getPost_list())
                                            .notifyDataSetChanged();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            refreshLayout.finishRefreshing();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            refreshLayout.finishRefreshing();
                        }
                    });

                }

                @Override
                public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                    if (mCurPage == 3) {
                        mCurPage = 16;
                    }
                    App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String json = response.body().string();
                                SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                                boolean isNext = mCurPage == 3 ? false : true;//model.getData().getHas_next_page() == 1;
                                if (isNext) {
                                    mHasNextPage = true;
                                    mCurPage++;
                                } else {
                                    mHasNextPage = false;
                                }
                                if (model.getData() != null)
                                    mMainAdapter.getDataHolder()
                                            .addAll(model.getData().getPost_list())
                                            .notifyDataSetChanged();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            refreshLayout.finishLoadmore();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            refreshLayout.finishLoadmore();
                        }
                    });
                }
            });
            mTrlRefresh.startRefresh();
        }
    }
}
