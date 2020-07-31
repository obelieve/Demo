package com.zxy.demo.test.ui;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/29
 */
public class NumberPercentValueRightViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<NumberPercentValueRightViewHolder.Data> {

    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_percent)
    TextView tvPercent;

    public NumberPercentValueRightViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_number_percent_value_right);
    }

    @Override
    public void bind(Data data) {
        tvRate.setText(data.rate);
        tvPercent.setText(data.percent);
        tvPercent.setTextColor(data.color);
    }

    public static class Data {

        private String rate;
        private String percent;
        private int color;

        public Data() {
        }

        public Data(String rate, String percent, int color) {
            this.rate = rate;
            this.percent = percent;
            this.color = color;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "rate='" + rate + '\'' +
                    ", percent='" + percent + '\'' +
                    ", color=" + color +
                    '}';
        }
    }
}
