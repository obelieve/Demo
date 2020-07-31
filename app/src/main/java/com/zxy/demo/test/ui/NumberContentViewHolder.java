package com.zxy.demo.test.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.utility.SystemUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/7/29
 */
public class NumberContentViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<NumberContentViewHolder.Data> {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    NumberContentItemAdapter mAdapter;


    public NumberContentViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_number_content);
        mAdapter = new NumberContentItemAdapter((Activity) parent.getContext());
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
                outRect.bottom = padding;
            }
        });
        rvContent.setAdapter(mAdapter);
    }

    @Override
    public void bind(Data data) {
        tvTitle.setText(data.getTitle());
        tvTitle.setTextColor(data.getColorValue().getTextColor());
        mAdapter.setUIStyle(data.getColorValue().getTextColor(),PlanConst.genGradientDrawable(data.getColorValue().getBgColor(),SystemUtil.dp2px(12)));
        mAdapter.getDataHolder().setList(data.getNumbers());
    }

    public static class NumberContentItemAdapter extends BaseRecyclerViewAdapter<String> {

        private int mTextColor;
        private Drawable mBgDrawable;

        public NumberContentItemAdapter(Activity mActivity) {
            super(mActivity);
        }

        public void setUIStyle(@ColorInt int textColor, Drawable bgDrawable){
            mTextColor = textColor;
            mBgDrawable = bgDrawable;
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


        public class ItemViewHolder extends BaseViewHolder<String> {

            @BindView(R.id.ntv_num)
            NumUseTTFTextView ntvNum;

            public ItemViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_number_content_item);
            }

            @Override
            public void bind(String s) {
                ntvNum.setTextColor(mTextColor);
                ntvNum.setBackground(mBgDrawable);
                ntvNum.setText(s);
            }
        }
    }



    public static class Data {

        private PlanConst.ColorValue mColorValue;
        private String title;
        private List<String> numbers;

        public Data() {
        }

        public Data(PlanConst.ColorValue colorValue, String title, List<String> numbers) {
            this.mColorValue = colorValue;
            this.title = title;
            this.numbers = numbers;
        }

        public PlanConst.ColorValue getColorValue() {
            return mColorValue;
        }

        public void setColorValue(PlanConst.ColorValue colorValue) {
            mColorValue = colorValue;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<String> numbers) {
            this.numbers = numbers;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "mColorValue=" + mColorValue +
                    ", title='" + title + '\'' +
                    ", numbers=" + numbers +
                    '}';
        }
    }
}
