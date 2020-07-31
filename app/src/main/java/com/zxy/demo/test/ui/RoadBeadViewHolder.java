package com.zxy.demo.test.ui;

import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.utility.SystemUtil;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/31
 */
public class RoadBeadViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<RoadBeadViewHolder.Data> {

    @BindView(R.id.tpb_left)
    TextProgressBar tpbLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tpb_right)
    TextProgressBar tpbRight;

    public RoadBeadViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_road_bead);
    }

    @Override
    public void bind(Data data) {
        tpbLeft.setMax(data.getLeftMax());
        tpbLeft.setProgress(data.getLeftCurrent());
        tpbLeft.setOrientation(TextProgressBar.RIGHT_ORIENTATION);
        tpbLeft.refreshUI(SystemUtil.dp2px(80),SystemUtil.dp2px(8));
        tvLeft.setText(data.getLeftCurrent()+"珠");
        tvTitle.setText(data.getTitle());
        tpbRight.setMax(data.getRightMax());
        tpbRight.setProgress(data.getRightCurrent());
        tpbRight.setOrientation(TextProgressBar.LEFT_ORIENTATION);
        tpbRight.refreshUI(SystemUtil.dp2px(80),SystemUtil.dp2px(8));
        tvRight.setText(data.getRightCurrent()+"珠");
    }

    public static class Data {
        private String title;
        private int leftCurrent;
        private int leftMax;
        private int rightCurrent;
        private int rightMax;

        public Data() {
        }

        public Data(String title, int leftCurrent, int leftMax, int rightCurrent, int rightMax) {
            this.title = title;
            this.leftCurrent = leftCurrent;
            this.leftMax = leftMax;
            this.rightCurrent = rightCurrent;
            this.rightMax = rightMax;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getLeftMax() {
            return leftMax;
        }

        public void setLeftMax(int leftMax) {
            this.leftMax = leftMax;
        }

        public int getLeftCurrent() {
            return leftCurrent;
        }

        public void setLeftCurrent(int leftCurrent) {
            this.leftCurrent = leftCurrent;
        }

        public int getRightMax() {
            return rightMax;
        }

        public void setRightMax(int rightMax) {
            this.rightMax = rightMax;
        }

        public int getRightCurrent() {
            return rightCurrent;
        }

        public void setRightCurrent(int rightCurrent) {
            this.rightCurrent = rightCurrent;
        }
    }
}
