package com.zxy.mall.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.frame.base.BaseFragment;
import com.zxy.mall.R;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.mall.entity.SupportEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.utils.MallUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreInfoFragment extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rb_score)
    RatingBar mRbScore;
    @BindView(R.id.tv_rating_count)
    TextView mTvRatingCount;
    @BindView(R.id.tv_sell_count)
    TextView mTvSellCount;
    @BindView(R.id.iv_collect)
    ImageView mIvCollect;
    @BindView(R.id.tv_collect)
    TextView mTvCollect;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_min_price)
    TextView mTvMinPrice;
    @BindView(R.id.tv_delivery_price)
    TextView mTvDeliveryPrice;
    @BindView(R.id.tv_delivery_time)
    TextView mTvDeliveryTime;
    @BindView(R.id.tv_bulletin)
    TextView mTvBulletin;
    @BindView(R.id.rv_support)
    RecyclerView mRvSupport;
    @BindView(R.id.rv_picture)
    RecyclerView mRvPicture;
    @BindView(R.id.rv_info)
    RecyclerView mRvInfo;

    SupportAdapter mSupportAdapter;
    PictureAdapter mPictureAdapter;
    InfoAdapter mInfoAdapter;


    @Override
    public int layoutId() {
        return R.layout.fragment_store_info;
    }

    @Override
    protected void initView() {
        SellerEntity entity = MockRepository.getSeller();
        mRvSupport.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);
        mRvPicture.setLayoutManager(ll);
        mSupportAdapter = new SupportAdapter(getActivity());
        mPictureAdapter = new PictureAdapter(getActivity());
        mInfoAdapter = new InfoAdapter(getActivity());
        mRvSupport.setAdapter(mSupportAdapter);
        mRvPicture.addItemDecoration(new HorizontalItemDivider(true,6, Color.TRANSPARENT));
        mRvPicture.setAdapter(mPictureAdapter);
        mRvInfo.setAdapter(mInfoAdapter);
        loadData(entity);
    }

    private void loadData(SellerEntity entity) {
        mTvName.setText(entity.getName());
        mRbScore.setRating((float) entity.getScore());
        mTvRatingCount.setText("(" + entity.getRatingCount() + ")");
        mTvSellCount.setText("月售" + entity.getSellCount() + "单");
        mTvMinPrice.setText(entity.getMinPrice()+"");
        mTvDeliveryPrice.setText(entity.getDeliveryPrice()+"");
        mTvDeliveryTime.setText(entity.getDeliveryTime()+"");
        mTvBulletin.setText(entity.getBulletin());
        mSupportAdapter.getDataHolder().setList(entity.getSupports());
        mPictureAdapter.getDataHolder().setList(entity.getPics());
        mInfoAdapter.getDataHolder().setList(entity.getInfos());
    }

    public static class SupportAdapter extends BaseRecyclerViewAdapter<SupportEntity> {


        public SupportAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new SupportViewHolder(parent, R.layout.viewholder_support);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            SupportAdapter.SupportViewHolder supportViewHolder = (SupportAdapter.SupportViewHolder) holder;
            supportViewHolder.bind(getDataHolder().getList().get(position));
        }

        public class SupportViewHolder extends BaseViewHolder {

            @BindView(R.id.iv_type)
            ImageView mIvType;
            @BindView(R.id.tv_desc)
            TextView mTvDesc;

            public SupportViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this,itemView);
            }

            public void bind(SupportEntity entity) {
                mIvType.setImageResource(MallUtil.getSupportTypeIcon(entity.getType()));
                mTvDesc.setText(entity.getDescription());
            }
        }
    }

    public static class PictureAdapter extends BaseRecyclerViewAdapter<String> {


        public PictureAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new PictureViewHolder(parent, R.layout.viewholder_picture);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            PictureAdapter.PictureViewHolder pictureViewHolder = (PictureAdapter.PictureViewHolder) holder;
            pictureViewHolder.bind(getDataHolder().getList().get(position));
        }


        public class PictureViewHolder extends BaseViewHolder {

            @BindView(R.id.iv_pic)
            ImageView mIvPic;

            public PictureViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this,itemView);
            }

            public void bind(String entity) {
                Glide.with(getContext()).load(entity).error(R.drawable.ic_error).into(mIvPic);
            }

            @OnClick(R.id.iv_pic)
            public void onViewClicked() {
            }
        }
    }

    public static class InfoAdapter extends BaseRecyclerViewAdapter<String> {


        public InfoAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new InfoViewHolder(parent, R.layout.viewholder_info);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            InfoAdapter.InfoViewHolder infoViewHolder = (InfoAdapter.InfoViewHolder) holder;
            infoViewHolder.bind(getDataHolder().getList().get(position));
        }

        public class InfoViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_desc)
            TextView mTvDesc;

            public InfoViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this,itemView);
            }

            public void bind(String entity) {
                mTvDesc.setText(entity);
            }
        }
    }
}
