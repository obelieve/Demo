package com.zxy.demo.fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends ApiBaseFragment {

    @BindView(R.id.tv_phone_tip)
    TextView tvPhoneTip;
    @BindView(R.id.ll_phone_tip)
    LinearLayout llPhoneTip;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sms_time)
    TextView tvSmsTime;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_retry_code)
    TextView tvRetryCode;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;

    CountDownTimer mTimer;

    @Override
    public int layoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11) {
                    tvNext.setEnabled(true);
                } else {
                    tvNext.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.tv_retry_code, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_retry_code:
                if (!tvNext.isEnabled()) return;
                tvSmsTime.setVisibility(View.VISIBLE);
                tvRetryCode.setVisibility(View.GONE);
                if (mTimer == null) {
                    mTimer = new CountDownTimer(10 * 1000, 1000) {

                        @Override
                        public void onTick(long l) {
                            tvSmsTime.setText(l / 1000 + "");
                        }

                        @Override
                        public void onFinish() {
                            tvSmsTime.setVisibility(View.GONE);
                            tvRetryCode.setVisibility(View.VISIBLE);
                        }
                    };
                }
                mTimer.start();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(etCode.getText().toString())) {
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
