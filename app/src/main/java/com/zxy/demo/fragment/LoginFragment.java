package com.zxy.demo.fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.zxy.demo.R;
import com.zxy.demo.databinding.FragmentLoginBinding;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

public class LoginFragment extends ApiBaseFragment<FragmentLoginBinding> implements View.OnClickListener {

    CountDownTimer mTimer;

    @Override
    protected void initView() {
        mViewBinding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11) {
                    mViewBinding.tvNext.setEnabled(true);
                } else {
                    mViewBinding.tvNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_retry_code:
                if (!mViewBinding.tvNext.isEnabled()) return;
                mViewBinding.tvSmsTime.setVisibility(View.VISIBLE);
                mViewBinding.tvRetryCode.setVisibility(View.GONE);
                if (mTimer == null) {
                    mTimer = new CountDownTimer(10 * 1000, 1000) {

                        @Override
                        public void onTick(long l) {
                            mViewBinding.tvSmsTime.setText(l / 1000 + "");
                        }

                        @Override
                        public void onFinish() {
                            mViewBinding.tvSmsTime.setVisibility(View.GONE);
                            mViewBinding.tvRetryCode.setVisibility(View.VISIBLE);
                        }
                    };
                }
                mTimer.start();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(mViewBinding.etCode.getText().toString())) {
                    ToastUtil.show("验证码不能为空");
                } else {
                    ToastUtil.show("验证登录...");
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
