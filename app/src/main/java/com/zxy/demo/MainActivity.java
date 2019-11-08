package com.zxy.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zxy.demo.adapter.viewholder.MainViewHolder;
import com.zxy.demo.model.SquareListModel;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.frame.adapter.item_decoration.VerticalItemDivider;
import com.zxy.frame.json.MGson;
import com.zxy.json.JsonUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.trl_refresh)
    TwinklingRefreshLayout mTrlRefresh;

    MainAdapter mMainAdapter;

    int mCurPage = 1;
    boolean mHasNextPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainAdapter = new MainAdapter(this);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new VerticalItemDivider());
        mRvContent.setAdapter(mMainAdapter);

        mTrlRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String json = response.body().string();
                            SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                            if (model.getData().getHas_next_page() == 1) {
                                mHasNextPage = true;
                                mCurPage++;
                                refreshLayout.setEnableLoadmore(true);
                            } else {
                                mHasNextPage = false;
                                refreshLayout.setEnableLoadmore(false);
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
                App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String json = response.body().string();
                            SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                            if (model.getData().getHas_next_page() == 1) {
                                mHasNextPage = true;
                                mCurPage++;
                                refreshLayout.setEnableLoadmore(true);
                            } else {
                                mHasNextPage = false;
                                refreshLayout.setEnableLoadmore(false);
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
}
