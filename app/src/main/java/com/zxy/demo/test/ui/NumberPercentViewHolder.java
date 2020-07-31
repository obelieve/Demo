package com.zxy.demo.test.ui;

import android.view.ViewGroup;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/28
 */
public class NumberPercentViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<NumberPercentViewHolder.Data> {


    @BindView(R.id.ntv_number)
    NumUseTTFTextView ntvNumber;
    @BindView(R.id.rp_percent)
    RoundProgress rpPercent;

    public NumberPercentViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_number_percent);
    }

    @Override
    public void bind(Data data) {
        ntvNumber.setText(data.getNumbers());
        ntvNumber.setTextColor(data.getColor());
        rpPercent.setProgress(data.getProgress());
        rpPercent.setHorizontalProgressTextColor(data.getColor());
        rpPercent.setHorizontalProgresReachColor(data.getColor());
        rpPercent.setHorizontalProgresUnReachColor(data.getColor());
    }

    public static class Data {

        private String numbers;
        private int progress;
        private int color;

        public Data() {
        }

        public Data(String numbers, int progress, int color) {
            this.numbers = numbers;
            this.progress = progress;
            this.color = color;
        }

        public String getNumbers() {
            return numbers;
        }

        public void setNumbers(String numbers) {
            this.numbers = numbers;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
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
                    "numbers='" + numbers + '\'' +
                    ", progress='" + progress + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }
}
