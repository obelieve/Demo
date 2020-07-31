package com.zxy.demo.test.ui;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.utility.SystemUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/31
 */
public class PlanAnalysisLotteryDataViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<PlanAnalysisLotteryDataViewHolder.Data> {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_fold)
    CheckBox cbFold;
    @BindView(R.id.rv_lottery_data_period)
    RecyclerView rvLotteryDataPeriod;
    @BindView(R.id.rv_lottery_data_number)
    RecyclerView rvLotteryDataNumber;

    private LotteryDataPeriodAdapter mLotteryDataPeriodAdapter;
    private LotteryDataNumberAdapter mLotteryDataNumberAdapter;

    public PlanAnalysisLotteryDataViewHolder(ViewGroup parent) {
        super(parent,R.layout.viewholder_plan_analysis_lottery_data);
        mLotteryDataPeriodAdapter = new LotteryDataPeriodAdapter((Activity) parent.getContext());
        mLotteryDataNumberAdapter = new LotteryDataNumberAdapter((Activity) parent.getContext());
        rvLotteryDataPeriod.setAdapter(mLotteryDataPeriodAdapter);
        rvLotteryDataNumber.addItemDecoration(new RecyclerView.ItemDecoration() {
            int padding;
            {
                padding = SystemUtil.dp2px(8);
            }
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = padding;
                outRect.bottom = padding;
            }
        });
        rvLotteryDataNumber.setAdapter(mLotteryDataNumberAdapter);
    }

    @Override
    public void bind(Data data) {
        tvTitle.setText(data.getTitle());
        mLotteryDataPeriodAdapter.getDataHolder().setList(data.getLottery_data());
        mLotteryDataNumberAdapter.getDataHolder().setList(data.getNumber_data());
        cbFold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLotteryDataPeriodAdapter.setFold(!isChecked);
            }
        });
    }

    public static class LotteryDataPeriodAdapter extends BaseRecyclerViewAdapter<Data.LotteryDataBean> {

        private boolean mFold;

        public LotteryDataPeriodAdapter(Activity mActivity) {
            super(mActivity);
        }

        public void setFold(boolean fold) {
            mFold = fold;
            notifyDataSetChanged();
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new LotteryDataPeriodViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


        @Override
        public int getItemCount() {
            return mFold ? 0 : super.getItemCount();
        }

        public static class LotteryDataPeriodViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Data.LotteryDataBean> {

            @BindView(R.id.tv_lottery_no_item_lottey_data)
            TextView tvLotteryNo;
            @BindView(R.id.tv_lottery_number_item_lottey_data)
            NumUseTTFTextView tvLotteryNumber;
            @BindView(R.id.tv_status_remark_item_lottey_data)
            TextView tvStatusRemark;
            @BindView(R.id.layout_content_lottey_data_item)
            LinearLayout layoutContentLotteyDataItem;

            Activity mActivity;

            public LotteryDataPeriodViewHolder(ViewGroup parent) {
                super(parent,R.layout.viewholder_plan_analysis_lottery_data_period);
                mActivity = (Activity) parent.getContext();
            }

            @Override
            public void bind(Data.LotteryDataBean data) {
                tvLotteryNo.setText(data.getLottery_no());
                StringBuilder sb = new StringBuilder();
                if (data.getLottery_number() != null) {
                    for (String s : data.getLottery_number()) {
                        sb.append(s).append("   ");
                    }
                }
                tvLotteryNumber.setText(sb.toString());
                tvStatusRemark.setText(data.getStatus_remark());
            }
        }
    }


    public static class LotteryDataNumberAdapter extends BaseRecyclerViewAdapter<Data.NumberDataBean> {

        public LotteryDataNumberAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new LotteryDataNumberViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


        public static class LotteryDataNumberViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Data.NumberDataBean> {

            @BindView(R.id.tv_title)
            TextView tvTitle;
            @BindView(R.id.rv_content)
            RecyclerView rvContent;
            @BindView(R.id.tv_copy)
            TextView tvCopy;

            NumberAdapter mNumberAdapter;
            Activity mActivity;

            public LotteryDataNumberViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_plan_analysis_lottery_data_number);
                mActivity = (Activity) parent.getContext();
                tvCopy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                rvContent.setLayoutManager(new AutoFixWidthLayoutManager());
                rvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
                    int padding;

                    {
                        padding = SystemUtil.dp2px(4);
                    }

                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.right = padding;
                    }
                });
                mNumberAdapter = new NumberAdapter((Activity) mActivity);
                rvContent.setAdapter(mNumberAdapter);
            }

            @Override
            public void bind(Data.NumberDataBean data) {
                tvTitle.setText(data.getTitle());
                mNumberAdapter.getDataHolder().setList(data.getLottery_number());
                tvCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getLottery_number() != null && !data.getLottery_number().isEmpty()) {
                            ClipboardUtil.clipboardCopyText(mActivity, Arrays.toString(data.getLottery_number().toArray()).trim());
                            ToastUtil.show("复制成功");
                        }
                    }
                });
            }

            public static class NumberAdapter extends BaseRecyclerViewAdapter<String> {

                public NumberAdapter(Activity mActivity) {
                    super(mActivity);
                }

                @Override
                public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
                    return new NumberViewHolder(parent);
                }

                @Override
                public void loadViewHolder(BaseViewHolder holder, int position) {
                    holder.bind(getDataHolder().getList().get(position));
                }


                public static class NumberViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<String> {

                    @BindView(R.id.ntv_num)
                    NumUseTTFTextView ntvNum;

                    public NumberViewHolder(ViewGroup parent) {
                        super(parent,R.layout.viewholder_plan_analysis_lottery_data_number_item);
                        ntvNum.setTextColor(PlanConst.ColorValue.COLD.getTextColor());
                    }

                    @Override
                    public void bind(String s) {
                        ntvNum.setText(s);
                    }
                }
            }
        }
    }

    public static class Data {
        private String title;
        private List<LotteryDataBean> lottery_data;
        private List<NumberDataBean> number_data;

        public Data() {
        }

        public Data(String title, List<LotteryDataBean> lottery_data, List<NumberDataBean> number_data) {
            this.title = title;
            this.lottery_data = lottery_data;
            this.number_data = number_data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<LotteryDataBean> getLottery_data() {
            return lottery_data;
        }

        public void setLottery_data(List<LotteryDataBean> lottery_data) {
            this.lottery_data = lottery_data;
        }

        public List<NumberDataBean> getNumber_data() {
            return number_data;
        }

        public void setNumber_data(List<NumberDataBean> number_data) {
            this.number_data = number_data;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "title='" + title + '\'' +
                    ", lottery_data=" + lottery_data +
                    ", number_data=" + number_data +
                    '}';
        }

        public static class LotteryDataBean {
            /**
             * lottery_no : 0005期
             * lottery_number : ["1","0","7","4","3"]
             * status_remark : 中奖
             * status : 1
             */

            private String lottery_no;
            private String status_remark;
            private int status;
            private List<String> lottery_number;

            public LotteryDataBean() {
            }

            public LotteryDataBean(String lottery_no, String status_remark, int status, List<String> lottery_number) {
                this.lottery_no = lottery_no;
                this.status_remark = status_remark;
                this.status = status;
                this.lottery_number = lottery_number;
            }

            public String getLottery_no() {
                return lottery_no;
            }

            public void setLottery_no(String lottery_no) {
                this.lottery_no = lottery_no;
            }

            public String getStatus_remark() {
                return status_remark;
            }

            public void setStatus_remark(String status_remark) {
                this.status_remark = status_remark;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<String> getLottery_number() {
                return lottery_number;
            }

            public void setLottery_number(List<String> lottery_number) {
                this.lottery_number = lottery_number;
            }
        }

        public static class NumberDataBean {
            private String title;
            private List<String> lottery_number;

            public NumberDataBean() {
            }

            public NumberDataBean(String title, List<String> lottery_number) {
                this.title = title;
                this.lottery_number = lottery_number;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getLottery_number() {
                return lottery_number;
            }

            public void setLottery_number(List<String> lottery_number) {
                this.lottery_number = lottery_number;
            }

            @Override
            public String toString() {
                return "NumberDataBean{" +
                        "title='" + title + '\'' +
                        ", lottery_number=" + lottery_number +
                        '}';
            }
        }
    }

}
