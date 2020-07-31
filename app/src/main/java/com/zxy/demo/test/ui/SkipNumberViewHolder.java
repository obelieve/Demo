package com.zxy.demo.test.ui;

import android.app.Activity;
import android.view.ViewGroup;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/30
 */
public class SkipNumberViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<SkipNumberViewHolder.Data> {


    @BindView(R.id.ntv_number)
    NumUseTTFTextView ntvNumber;
    @BindView(R.id.tpb_progress)
    TextProgressBar tpbProgress;
    Activity mActivity;

    public SkipNumberViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_skip_number);
        mActivity = (Activity) parent.getContext();
    }

    @Override
    public void bind(Data data) {
        ntvNumber.setText(data.getNumber());
        tpbProgress.setText(data.getCurrent()+"/"+data.getAverage()+"/"+data.getMax());
        tpbProgress.setMax(data.getMax());
        tpbProgress.setProgressColor(data.getColor());
        tpbProgress.setProgress(data.getCurrent());
        tpbProgress.refreshUI(SystemUtil.screenWidth()-SystemUtil.dp2px(64),SystemUtil.dp2px(14));
    }

    public static class Data {
        private int color;
        private String number;
        private int current;
        private int average;
        private int max;

        public Data() {
        }

        public Data(int color, String number, int current, int average, int max) {
            this.color = color;
            this.number = number;
            this.current = current;
            this.average = average;
            this.max = max;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getAverage() {
            return average;
        }

        public void setAverage(int average) {
            this.average = average;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "color=" + color +
                    ", number='" + number + '\'' +
                    ", current=" + current +
                    ", average=" + average +
                    ", max=" + max +
                    '}';
        }
    }
}
