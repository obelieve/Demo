package com.zxy.mall.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.mall.R;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.SellerEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingHeaderView extends FrameLayout {

    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.tv_score_title)
    TextView mTvScoreTitle;
    @BindView(R.id.tv_rank_rate)
    TextView mTvRankRate;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.tv_title1)
    TextView mTvTitle1;
    @BindView(R.id.rb_service_score)
    RatingBar mRbServiceScore;
    @BindView(R.id.tv_service_score)
    TextView mTvServiceScore;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.rb_food_score)
    RatingBar mRbFoodScore;
    @BindView(R.id.tv_food_score)
    TextView mTvFoodScore;
    @BindView(R.id.tv_delivery_time)
    TextView mTvDeliveryTime;
    @BindView(R.id.rv_rating_category)
    RecyclerView mRvRatingCategory;
    @BindView(R.id.cb_select_content)
    CheckBox mCbSelectContent;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    RatingCategoryAdapter mRatingCategoryAdapter;
    RatingAdapter mRatingAdapter;

    public RatingHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rating_header, this, true);
        ButterKnife.bind(this, view);
        LinearLayoutManager lm =new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.HORIZONTAL);
        mRvRatingCategory.setLayoutManager(lm);
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mRatingCategoryAdapter = new RatingCategoryAdapter(context);
        mRatingAdapter = new RatingAdapter(context);
        mRvRatingCategory.setAdapter(mRatingCategoryAdapter);
        mRvContent.setAdapter(mRatingAdapter);
        mRatingCategoryAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<RatingCategoryEntity>() {
            @Override
            public void onItemClick(View view, RatingCategoryEntity entity, int position) {
                mRatingAdapter.getDataHolder().setList(entity.getEntityList());
            }
        });
        mRatingAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<RatingEntity>() {
            @Override
            public void onItemClick(View view, RatingEntity t, int position) {

            }
        });
    }

    public void loadData(SellerEntity entity, List<RatingEntity> entityList) {

        if (entity != null) {
            mTvScore.setText(entity.getScore()+"");
            mTvServiceScore.setText(getScaleDouble(entity.getServiceScore())+"");
            mRbServiceScore.setRating((float) getScaleDouble(entity.getServiceScore()));
            mTvFoodScore.setText(getScaleDouble(entity.getFoodScore())+"");
            mRbFoodScore.setRating((float) getScaleDouble(entity.getFoodScore()));
            mTvRankRate.setText("高于周边商家"+entity.getRankRate()+"%");
            mTvDeliveryTime.setText("送达时间"+entity.getDeliveryTime()+"分钟");
        }
        if(entityList!=null){
            RatingCategoryEntity all=new RatingCategoryEntity();
            all.setSelected(true);
            all.setType(0);
            all.setName("全部");
            all.setEntityList(new ArrayList<>());
            RatingCategoryEntity satisfaction=new RatingCategoryEntity();
            satisfaction.setType(0);
            satisfaction.setName("满意");
            satisfaction.setEntityList(new ArrayList<>());
            RatingCategoryEntity unsatisfaction=new RatingCategoryEntity();
            unsatisfaction.setType(1);
            unsatisfaction.setName("不满意");
            unsatisfaction.setEntityList(new ArrayList<>());
            for(RatingEntity ratingEntity:entityList){
                if(ratingEntity.getRateType()==0){
                    all.getEntityList().add(ratingEntity);
                    satisfaction.getEntityList().add(ratingEntity);
                }else{
                    all.getEntityList().add(ratingEntity);
                    unsatisfaction.getEntityList().add(ratingEntity);
                }
            }
            mRatingCategoryAdapter.getDataHolder().setList(Arrays.asList(all,satisfaction,unsatisfaction));
            mRatingAdapter.getDataHolder().setList(all.getEntityList());
        }
    }

    private double getScaleDouble(double d){
        BigDecimal b = new BigDecimal(d);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static class RatingCategoryEntity {

        private boolean selected;
        private int type;//0 全部/满意 1 不满意
        private String mName;
        private List<RatingEntity> mEntityList;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public List<RatingEntity> getEntityList() {
            return mEntityList;
        }

        public void setEntityList(List<RatingEntity> entityList) {
            mEntityList = entityList;
        }
    }

    public static class RatingCategoryAdapter extends BaseRecyclerViewAdapter<RatingCategoryEntity> {

        private int mCurPosition = -1;

        public RatingCategoryAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RCViewHolder(parent, R.layout.viewholder_rating_category);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof RCViewHolder) {
                RatingCategoryEntity entity = getDataHolder().getList().get(position);
                boolean selected = entity.isSelected();
                if(selected){
                    mCurPosition = position;
                }
                ((RCViewHolder) holder).mTvName.setSelected(selected);
                ((RCViewHolder) holder).mTvNum.setSelected(selected);
                ((RCViewHolder) holder).mLlContent.setSelected(selected);
                if (entity.getType() == 0) {
                    ((RCViewHolder) holder).mLlContent.setBackgroundResource(R.drawable.sel_3300a0dc_00a0dc);
                } else {
                    ((RCViewHolder) holder).mLlContent.setBackgroundResource(R.drawable.sel_3393999f_00a0dc);
                }
                ((RCViewHolder) holder).mTvName.setText(entity.getName());
                ((RCViewHolder) holder).mTvNum.setText("" + (entity.getEntityList() != null ? entity.getEntityList().size() : 0));
                ((RCViewHolder) holder).itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurPosition != position) {
                            if (mCurPosition != -1) {
                                getDataHolder().getList().get(mCurPosition).setSelected(false);
                            }
                            entity.setSelected(true);
                            if (mItemClickCallback != null) {
                                mItemClickCallback.onItemClick(v, entity, position);
                            }
                            mCurPosition = position;
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        public class RCViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_num)
            TextView mTvNum;
            @BindView(R.id.ll_content)
            LinearLayout mLlContent;

            public RCViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class RatingAdapter extends BaseRecyclerViewAdapter<RatingEntity> {

        public RatingAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RatingViewHolder(parent, R.layout.viewholder_rating);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof RatingViewHolder) {
                RatingEntity entity = getDataHolder().getList().get(position);
                ((RatingViewHolder) holder).mTvName.setText(entity.getText());
            }
        }

        public class RatingViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView mTvName;

            public RatingViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
