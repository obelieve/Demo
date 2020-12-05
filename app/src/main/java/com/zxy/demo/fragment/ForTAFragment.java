package com.zxy.demo.fragment;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.zxy.demo.databinding.FragmentForTaBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.helper.KeyEditTextForATHelper;

/**
 * @其他人 使用例子
 */
public class ForTAFragment extends ApiBaseFragment<FragmentForTaBinding> {

    KeyEditTextForATHelper<ForegroundColorSpan> mKeyEditTextForATHelper;

    @Override
    protected void initView() {
        mKeyEditTextForATHelper = new KeyEditTextForATHelper<>(mViewBinding.et, ForegroundColorSpan.class);
        mKeyEditTextForATHelper.setCallback(new KeyEditTextForATHelper.Callback<ForegroundColorSpan>() {
            @Override
            public void onDeletedSpan(ForegroundColorSpan span) {
                Toast.makeText(getContext(), "delete " + span, Toast.LENGTH_LONG).show();
            }
        });
        mViewBinding.btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), mViewBinding.et.getText().toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mViewBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
                mKeyEditTextForATHelper.insertSpan(colorSpan, "@【123】");
//        mEditTextForATHelper.insertSpan(new A(), "@【AAA】");
            }
        });
    }

}
