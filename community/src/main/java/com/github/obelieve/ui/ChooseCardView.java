package com.github.obelieve.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.obelieve.community.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseCardView extends FrameLayout {

    @BindView(R.id.chose_title)
    TextView mTitle;
    @BindView(R.id.chose_content)
    EditText mContent;
    @BindView(R.id.chose_arrow)
    ImageView mArrow;

    public ChooseCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_choose_card, null);
        ButterKnife.bind(this, view);
        this.addView(view);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChooseCardView);
        String title = ta.getString(R.styleable.ChooseCardView_chose_title);
        String hint = ta.getString(R.styleable.ChooseCardView_chose_hint);
        int arrowVisibility = ta.getInt(R.styleable.ChooseCardView_chose_arrow_visibility,0);
        mArrow.setVisibility(arrowVisibility);
        mTitle.setText(title);
        mContent.setHint(hint);

        ta.recycle();
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
        }
    }

    public void setOnCardViewClick(OnClickListener listener) {
        mContent.setOnClickListener(listener);
        this.setOnClickListener(listener);
    }

    public String getCount() {
        return mContent.getText().toString();
    }

}
