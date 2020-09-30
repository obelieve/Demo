package com.github.obelieve.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.event.login.LoginNotifyEvent;
import com.github.obelieve.login.entity.SendSMSEntity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.LoginType;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.utils.ActivityUtil;
import com.zxy.frame.utils.secure.MD5Util;
import com.news.captchalib.SwipeCaptchaHelper;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.utils.storage.SPUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginTypeActivity extends ApiBaseActivity {

    public static final String EXTRA_INT_LOGIN_TYPE = "EXTRA_INT_LOGIN_TYPE";
    public static final String EXTRA_STRING_PHONE_OF_CODE = "EXTRA_STRING_PHONE_OF_CODE";
    public static final String EXTRA_STRING_DATA_OPENID = "EXTRA_STRING_DATA_OPENID";
    public static final String EXTRA_STRING_DATA_TYPE = "EXTRA_STRING_DATA_TYPE";

    public static final int TYPE_BIND_INPUT_PHONE = 0;
    public static final int TYPE_BIND_INPUT_CODE = 1;
    public static final int TYPE_LOGIN_INPUT_PHONE = 2;
    public static final int TYPE_LOGIN_INPUT_CODE = 3;
    public static final int TYPE_UNBIND_PHONE_INPUT_CODE = 4;
    public static final int TYPE_SETTINGS_BIND_INPUT_PHONE = 5;
    public static final int TYPE_SETTINGS_BIND_INPUT_CODE = 6;
    public static final int TYPE_CANCEL_ACCOUNT_INPUT_PHONE = 7;
    public static final int TYPE_CANCEL_ACCOUNT_INPUT_CODE = 8;

    @BindView(R.id.view_status_bar)
    View statusBarView;
    @BindView(R.id.right_icon_layout_2)
    RelativeLayout rightIconLayout2;


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_phone_tip)
    LinearLayout llPhoneTip;
    @BindView(R.id.tv_phone_tip)
    TextView tvPhoneTip;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.tv_switch_login)
    TextView tvSwitchLogin;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sms_time)
    TextView tvSmsTime;
    @BindView(R.id.tv_retry_code)
    TextView tvRetryCode;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.text_tip)
    TextView tvTip;
    @BindView(R.id.iv_agree)
    ImageView ivAgree;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;

    CountDownTimer mTimer;

    private int mType;
    private String mPhoneOfCode;
    private String mOpenID;
    private String mOpenType;
    LoginViewModel mLoginViewModel;
    //滑动验证码
    private String mValidateCode;//触发滑动验证码
    private CaptchaDialog mCaptchaDialog;//滑动验证码弹窗

    public static void startLoginInputPhone(Activity activity) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_LOGIN_INPUT_PHONE);
        activity.startActivity(intent);
    }

    public static void starCancelAccountInputPhone(Activity activity, String phone) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_CANCEL_ACCOUNT_INPUT_PHONE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        activity.startActivity(intent);
    }

    public static void starCancelAccountInputCode(Activity activity, String phone) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_CANCEL_ACCOUNT_INPUT_CODE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        activity.startActivity(intent);
    }

    public static void startLoginInputCode(Activity activity, String phone) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_LOGIN_INPUT_CODE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        activity.startActivity(intent);
    }

    public static void startBindPhone(Activity activity, String openId, String openType) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_BIND_INPUT_PHONE);
        intent.putExtra(EXTRA_STRING_DATA_OPENID, openId);
        intent.putExtra(EXTRA_STRING_DATA_TYPE, openType);
        activity.startActivity(intent);
    }

    public static void startBindCode(Activity activity, String phone, String openId, String openType) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_BIND_INPUT_CODE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        intent.putExtra(EXTRA_STRING_DATA_OPENID, openId);
        intent.putExtra(EXTRA_STRING_DATA_TYPE, openType);
        activity.startActivity(intent);
    }

    public static void startUnBindCode(Activity activity, String phone) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_UNBIND_PHONE_INPUT_CODE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        activity.startActivity(intent);
    }

    public static void startSettingsBindPhone(Activity activity) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_SETTINGS_BIND_INPUT_PHONE);
        activity.startActivity(intent);
    }

    public static void startSettingsBindCode(Activity activity, String phone) {
        Intent intent = new Intent(activity, LoginTypeActivity.class);
        intent.putExtra(EXTRA_INT_LOGIN_TYPE, TYPE_SETTINGS_BIND_INPUT_CODE);
        intent.putExtra(EXTRA_STRING_PHONE_OF_CODE, phone);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        mLightStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_login_type;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mType = getIntent().getIntExtra(EXTRA_INT_LOGIN_TYPE, 0);
            mPhoneOfCode = getIntent().getStringExtra(EXTRA_STRING_PHONE_OF_CODE);
            mOpenID = getIntent().getStringExtra(EXTRA_STRING_DATA_OPENID);
            mOpenType = getIntent().getStringExtra(EXTRA_STRING_DATA_TYPE);
        }
        setStatusBarHeight(statusBarView);
        initNav();
        initView();
        initType();
        initDataObserver();
    }

    private void initNav() {
        setNeedNavigate();
        setRightNavigate(R.drawable.dr_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.gotoMainActivity(mActivity);
            }
        });
    }

    private void initView() {
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
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 4) {
                    tvNext.setEnabled(true);
                } else {
                    tvNext.setEnabled(false);
                }
            }
        });
        if (mType == TYPE_BIND_INPUT_CODE || mType == TYPE_LOGIN_INPUT_CODE || mType == TYPE_UNBIND_PHONE_INPUT_CODE || mType == TYPE_SETTINGS_BIND_INPUT_CODE || mType == TYPE_CANCEL_ACCOUNT_INPUT_CODE) {
            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @Override
                public void onTick(long l) {
                    tvSmsTime.setText(l / 1000 + "");
                }

                @Override
                public void onFinish() {
                    tvSmsTime.setVisibility(View.GONE);
                    tvRetryCode.setVisibility(View.VISIBLE);
                }
            }.start();
        }
    }

    private void initType() {
        switch (mType) {
            case TYPE_BIND_INPUT_PHONE:
            case TYPE_SETTINGS_BIND_INPUT_PHONE:
                if (mType == TYPE_SETTINGS_BIND_INPUT_PHONE) {
                    rightIconLayout2.setVisibility(View.GONE);
                }
                tvTitle.setText("请绑定手机号");
                llPhone.setVisibility(View.VISIBLE);
                rlCode.setVisibility(View.GONE);
                tvPolicy.setText("");
                ivAgree.setVisibility(View.GONE);
                break;
            case TYPE_BIND_INPUT_CODE:
            case TYPE_UNBIND_PHONE_INPUT_CODE:
            case TYPE_SETTINGS_BIND_INPUT_CODE:
                if (mType == TYPE_UNBIND_PHONE_INPUT_CODE || mType == TYPE_SETTINGS_BIND_INPUT_CODE) {
                    rightIconLayout2.setVisibility(View.GONE);
                }
                tvTitle.setText("请输入验证码");
                llPhone.setVisibility(View.GONE);
                rlCode.setVisibility(View.VISIBLE);
                tvPolicy.setText("");
                ivAgree.setVisibility(View.GONE);
                tvPhoneTip.setText(mPhoneOfCode);
                llPhoneTip.setVisibility(View.VISIBLE);
                break;
            case TYPE_LOGIN_INPUT_PHONE:
                tvTitle.setText("上上比分登录");
                llPhone.setVisibility(View.VISIBLE);
                rlCode.setVisibility(View.GONE);
                ivAgree.setVisibility(View.VISIBLE);
                tvPolicy.setText(Html.fromHtml("我已阅读同意<font color='#5789FF'>《软件许可及隐私政策》</font>"));
                break;
            case TYPE_LOGIN_INPUT_CODE:
                tvTitle.setText("请输入验证码");
                llPhone.setVisibility(View.GONE);
                rlCode.setVisibility(View.VISIBLE);
                ivAgree.setVisibility(View.GONE);
                tvPolicy.setText("未注册手机号验证后直接登录");
                tvPhoneTip.setText(mPhoneOfCode);
                llPhoneTip.setVisibility(View.VISIBLE);
                break;
            case TYPE_CANCEL_ACCOUNT_INPUT_PHONE:
                tvTitle.setText("请输入您的手机号");
                llPhone.setVisibility(View.VISIBLE);
                rlCode.setVisibility(View.GONE);
                ivAgree.setVisibility(View.GONE);
                tvPolicy.setVisibility(View.GONE);
                tvTip.setText("您正在注销");
                tvPhoneTip.setText(mPhoneOfCode + "所绑定的账号");
                llPhoneTip.setVisibility(View.VISIBLE);
                break;
            case TYPE_CANCEL_ACCOUNT_INPUT_CODE:
                tvTitle.setText("请输入验证码");
                llPhone.setVisibility(View.GONE);
                rlCode.setVisibility(View.VISIBLE);
                ivAgree.setVisibility(View.GONE);
                tvPolicy.setVisibility(View.GONE);
                tvPhoneTip.setText(mPhoneOfCode);
                llPhoneTip.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void initDataObserver() {
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.getShowDialogMutableLiveData().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
        mLoginViewModel.getLoadErrorMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvError.setText(s);
            }
        });
        mLoginViewModel.getBindPhoneMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    tvSwitchLogin.setVisibility(View.VISIBLE);
                } else {
                    tvSwitchLogin.setVisibility(View.GONE);
                }
            }
        });
        mLoginViewModel.getUserEntityMutableLiveData().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity entity) {
                switch (mType) {
                    case TYPE_BIND_INPUT_CODE:
                    case TYPE_LOGIN_INPUT_CODE:
                        if (entity.do_type.equals("register")) {
                            ActivityUtil.gotoUserInfoActivity(mActivity, true);
                            SPUtil.getInstance().putInteger(PreferenceConst.SP_LOGIN_TYPE, LoginType.LOGIN_PHONE);
                        } else if (entity.do_type.equals("wechat") || entity.do_type.equals("qq")) {
                            startBindPhone(mActivity, entity.open_id, entity.do_type);
                            SPUtil.getInstance().putInteger(PreferenceConst.SP_LOGIN_TYPE, entity.do_type.equals("wechat") ? LoginType.LOGIN_WECHAT : LoginType.LOGIN_QQ);
                        } else {
                            CacheRepository.getInstance().refreshUserEntity(mActivity);
                            if (App.getContext() instanceof App) {
                                App application = (App) App.getContext();
                                application.finishActivity(2);
                            }
                            SPUtil.getInstance().putInteger(PreferenceConst.SP_LOGIN_TYPE, LoginType.LOGIN_PHONE);
                        }
                        EventBus.getDefault().post(new LoginNotifyEvent());
                        finish();
                        break;
                    case TYPE_CANCEL_ACCOUNT_INPUT_CODE:
//                        CancelAccountResultActivity.start(mActivity); //todo 用户注销
                        finish();
                        break;
                }
            }
        });
        mLoginViewModel.getUnBindMobileSuccessMutableLiveData().observe(this, new Observer<ApiBaseResponse<String>>() {
            @Override
            public void onChanged(ApiBaseResponse<String> response) {
                UserEntity userEntity2 = CacheRepository.getInstance().getUserEntity();
                if (userEntity2 != null) {
                    userEntity2.mobile = "";
                }
//                EventBus.getDefault().post(new UnbindAccountEvent(UnbindAccountEvent.PHONE_UNBIND_SUCCESS)); //todo 解绑成功 发送通知消息
                finish();
            }
        });
        mLoginViewModel.getBindMobileSuccessMutableLiveData().observe(this, new Observer<ApiBaseResponse<String>>() {
            @Override
            public void onChanged(ApiBaseResponse<String> response) {
                UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
                if (userEntity != null) {//绑定手机号成功，存储手机号
                    userEntity.mobile = mPhoneOfCode;
                }
                String value = "";
                try {
                    JSONObject jsonObject = new JSONObject(response.getData());
                    if (jsonObject.has("value")) {
                        value = jsonObject.getString("value");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
//                AccountSettingsActivity.start(mActivity, AccountSettingsActivity.ACCOUNT_TYPE); //todo 账号设置
//                EventBus.getDefault().post(new BindMobileSuccessEvent(value, response.getMsg())); //todo 手机号绑定成功 发送通知消息
                finish();
            }
        });
        mLoginViewModel.getSendSMSEntityMutableLiveData().observe(this, new Observer<SendSMSEntity>() {
            @Override
            public void onChanged(SendSMSEntity entity) {
                if (isTriggerCaptcha(entity.getValidate_code())) {
                    return;
                }
                switch (mType) {
                    case TYPE_LOGIN_INPUT_PHONE:
                        startLoginInputCode(mActivity, etPhone.getText().toString());
                        break;
                    case TYPE_BIND_INPUT_PHONE:
                        startBindCode(mActivity, etPhone.getText().toString(), mOpenID, mOpenType);
                        break;
                    case TYPE_SETTINGS_BIND_INPUT_PHONE:
                        startSettingsBindCode(mActivity, etPhone.getText().toString());
                        break;
                    case TYPE_BIND_INPUT_CODE:
                    case TYPE_LOGIN_INPUT_CODE:
                    case TYPE_UNBIND_PHONE_INPUT_CODE:
                    case TYPE_SETTINGS_BIND_INPUT_CODE:
                    case TYPE_CANCEL_ACCOUNT_INPUT_CODE:
                        if (mTimer != null) {
                            mTimer.start();
                            tvSmsTime.setVisibility(View.VISIBLE);
                            tvRetryCode.setVisibility(View.GONE);
                        }
                        break;
                    case TYPE_CANCEL_ACCOUNT_INPUT_PHONE:
                        starCancelAccountInputCode(mActivity, etPhone.getText().toString());
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        //第三方登录内存泄漏解决方案
//        UMShareAPI.get(this).release(); //todo 第三方登录注销
    }

    @OnClick({R.id.tv_next, R.id.tv_switch_login, R.id.tv_retry_code, R.id.iv_agree, R.id.tv_policy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                switch (mType) {
                    case TYPE_BIND_INPUT_PHONE:
                        mLoginViewModel.getThirdBindSmsCode(mActivity, etPhone.getText().toString(), mOpenType, mOpenID);
                        break;
                    case TYPE_BIND_INPUT_CODE:
                        mLoginViewModel.thridBindPhone(mActivity, mPhoneOfCode, etCode.getText().toString(), mOpenType, mOpenID);
                        break;
                    case TYPE_LOGIN_INPUT_PHONE:
                        if (!ivAgree.isSelected()) {
                            ToastUtil.show("请先同意《软件许可及隐私政策》");
                            return;
                        }
                        mLoginViewModel.getSmsCode(mActivity, etPhone.getText().toString(), 1);
                        break;
                    case TYPE_LOGIN_INPUT_CODE:
                        mLoginViewModel.login(mActivity, mPhoneOfCode, etCode.getText().toString());
                        break;
                    case TYPE_UNBIND_PHONE_INPUT_CODE:
                        mLoginViewModel.unbindMobile(mActivity, etCode.getText().toString());
                        break;
                    case TYPE_SETTINGS_BIND_INPUT_PHONE:
                        mLoginViewModel.getSmsCode(mActivity, etPhone.getText().toString(), 4);
                        break;
                    case TYPE_SETTINGS_BIND_INPUT_CODE:
                        mLoginViewModel.bindMobile(mActivity, mPhoneOfCode, etCode.getText().toString());
                        break;
                    case TYPE_CANCEL_ACCOUNT_INPUT_PHONE:
                        mLoginViewModel.getSmsCode(mActivity, etPhone.getText().toString(), 5);
                        break;
                    case TYPE_CANCEL_ACCOUNT_INPUT_CODE:
                        mLoginViewModel.cancelAccount(mActivity, mPhoneOfCode, etCode.getText().toString());
                        break;
                }
                break;
            case R.id.tv_switch_login:
                startLoginInputPhone(this);
                break;
            case R.id.tv_retry_code:
                switch (mType) {
                    case TYPE_LOGIN_INPUT_CODE:
                        mLoginViewModel.getSmsCode(mActivity, mPhoneOfCode, 1);
                        break;
                    case TYPE_UNBIND_PHONE_INPUT_CODE:
                        UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
                        if (userEntity != null) {
                            //绑定手机号成功，存储手机号
                            mLoginViewModel.getSmsCode(mActivity, userEntity.mobile, 3);
                        }

                        break;
                    case TYPE_BIND_INPUT_CODE:
                        mLoginViewModel.getThirdBindSmsCode(mActivity, mPhoneOfCode, mOpenType, mOpenID);
                        break;
                    case TYPE_SETTINGS_BIND_INPUT_CODE:
                        mLoginViewModel.getSmsCode(mActivity, mPhoneOfCode, 4);
                        break;
                    case TYPE_CANCEL_ACCOUNT_INPUT_CODE:
                        mLoginViewModel.getSmsCode(mActivity, mPhoneOfCode, 5);
                        break;
                }
                break;
            case R.id.iv_agree:
                ivAgree.setSelected(!ivAgree.isSelected());
                break;
            case R.id.tv_policy:
                if (mType == TYPE_LOGIN_INPUT_PHONE) { //隐私政策
//                    String privacy_url = SPUtil.getInstance().getString(mActivity, PreferenceConst.COPYRIGHT_URL, UrlConst.PRIVACY_URL);
//                    ActivityUtil.gotoWebActivity(mActivity, privacy_url, getResources().getString(R.string.settings_privacy_policy), false);
                }
                break;
        }
    }

    public boolean isTriggerCaptcha(String validateCode) {
        if ((mType == TYPE_LOGIN_INPUT_PHONE | mType == TYPE_LOGIN_INPUT_CODE) && isOpenCaptcha(validateCode)) {
            getCaptchaDialog().show();
            return true;
        }
        return false;
    }

    public boolean isOpenCaptcha(String validateCode) {
        mValidateCode = validateCode;
        if (!TextUtils.isEmpty(validateCode)) {
            return true;
        }
        return false;
    }

    public CaptchaDialog getCaptchaDialog() {
        if (mCaptchaDialog != null) {
            mCaptchaDialog.dismiss();
        }
        if (mCaptchaDialog == null) {
            mCaptchaDialog = new CaptchaDialog(mActivity);
            mCaptchaDialog.setEndCallback(new SwipeCaptchaHelper.EndCallback() {
                @Override
                public void onSuccess() {
                    String phone;
                    if (mType == TYPE_LOGIN_INPUT_PHONE) {
                        phone = etPhone.getText().toString();
                    } else {
                        phone = mPhoneOfCode;
                    }
                    if (phone != null && phone.length() == 11) {
                        String md5Value = MD5Util.md5(mValidateCode + phone.substring(8));
                        if (!TextUtils.isEmpty(md5Value)) {
                            md5Value = md5Value.toLowerCase();
                            mLoginViewModel.getSmsCode(mActivity, phone, 1, md5Value);
                        }
                    }

                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onMaxFailed() {

                }
            });
        }
        return mCaptchaDialog;
    }
}
