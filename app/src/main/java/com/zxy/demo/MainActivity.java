package com.zxy.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zxy.demo.model.SquareListModel;
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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainAdapter = new MainAdapter();
        mMainAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_empty, null));
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
            super(R.layout.viewholder_main, mPostListBeans);//R.layout.viewholder_main
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, SquareListModel.DataBean.PostListBean item) {
            helper.setText(R.id.tv_name, item.getNickname())
                    .setText(R.id.tv_content, item.getContent());
            Glide.with(mContext).load(item.getAvatar()).into((ImageView) helper.getView(R.id.iv_image));
        }


    }
}
