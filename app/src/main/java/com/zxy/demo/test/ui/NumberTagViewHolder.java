package com.zxy.demo.test.ui;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/29
 */
public class NumberTagViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<NumberTagViewHolder.Data> {

    @BindView(R.id.iv_tag)
    ImageView ivTag;
    @BindView(R.id.tv_tag)
    TextView tvTag;

    public NumberTagViewHolder(ViewGroup parent) {
        super(parent,R.layout.viewholder_number_tag);
    }

    @Override
    public void bind(Data data) {
        ivTag.setImageDrawable(PlanConst.genGradientDrawable(data.getColor(),3));
        tvTag.setText(data.getContent());
    }

    public static class Data {
        private int color;
        private String content;

        public Data() {
        }

        public Data(int color, String content) {
            this.color = color;
            this.content = content;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
