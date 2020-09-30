package com.github.obelieve.me.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.obelieve.community.R;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.me.viewmodel.UserInfoViewModel;
import com.github.obelieve.repository.cache.UserHelper;
import com.github.obelieve.thirdsdklib.ImageSelectorUtil;
import com.github.obelieve.thirdsdklib.picker.TimerPickerDialog;
import com.github.obelieve.ui.BottomMenuDialog;
import com.github.obelieve.ui.ChooseCardView;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.thirdsdklib.picker.CityPickerDialog;
import com.github.obelieve.utils.others.SystemConstant;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.image.GlideUtil;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends ApiBaseActivity {

    public static final String EXTRA_FROM_REGISTER = "extra_from_register";
    public static final String EXTRA_FROM_SET_THE_NAME = "extra_from_set_the_name";
    private static final int REQUEST_CODE = 0x00000011;
    private static final int REQUEST_CAMERA_CODE = 0x00000010;
    private static final int REQUEST_CAMERA_CLIP_CODE = 0x00000012;

    @BindView(R.id.user_info_face)
    ImageView mFace;
    @BindView(R.id.user_info_nick)
    EditText mNick;
    @BindView(R.id.user_info_sex)
    RadioGroup mSexGroup;
    @BindView(R.id.user_info_sex_m)
    RadioButton mSexM;
    @BindView(R.id.user_info_sex_w)
    RadioButton mSexW;
    @BindView(R.id.user_info_sex_none)
    RadioButton mSexNoe;
    @BindView(R.id.user_info_city)
    ChooseCardView mCityCard;
    @BindView(R.id.user_info_day)
    ChooseCardView mDayCard;
    @BindView(R.id.user_info_next)
    TextView mNext;
    BottomMenuDialog mBottomMenuDialog;
    TimerPickerDialog mTimerPickerDialog;
    CityPickerDialog mCityPickerDialog;

    boolean mRegister = false;
    boolean mSetTheName = false;
    String mCameraTempFileName;
    String mImagePath;
    int mSex = 0;
    String mDate;
    String[] mCityData = new String[2];//省，市名称
    UserInfoViewModel mUserInfoViewModel;

    @Override
    protected int layoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mRegister = getIntent().getBooleanExtra(EXTRA_FROM_REGISTER, false);
            mSetTheName = getIntent().getBooleanExtra(EXTRA_FROM_SET_THE_NAME, false);
        }
        initViewModel();
        initPickerView();
        if (mRegister) {
            setMyTitle(R.string.user_info_title);
            setRightNavigate(R.string.user_info_pass, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHelper.getInstance().getUserInfo(mActivity);
                    ActivityUtil.gotoMainActivity(mActivity);
                    finish();
                }
            });
        } else {
            setMyTitle(R.string.user_info_update_title);
            setNeedNavigate(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mSetTheName) {
                        ActivityUtil.gotoMainActivity(mActivity);
                    }
                    finish();
                }
            });
        }
        //TODO 用户信息
        UserEntity entity = UserHelper.getInstance().getUserEntity();
        if (entity != null) {
            mSex = entity.sex;
            mDate = entity.birthday;
            if (!TextUtils.isEmpty(entity.city)) {
                String[] strings = entity.city.split(",");
                if (strings.length == 1) {
                    mCityData[0] = null;
                    mCityData[1] = strings[0];
                    mCityCard.setContent(mCityData[1]);
                } else if (strings.length == 2) {
                    mCityData[0] = strings[0];
                    mCityData[1] = strings[1];
                    mCityCard.setContent(mCityData[0] + mCityData[1]);
                }
            }
            mNick.setText(entity.nickname);
            mNick.setSelection(entity.nickname.length());
            mDayCard.setContent(entity.birthday);
            if (!TextUtils.isEmpty(entity.avatar)) {
                GlideUtil.loadImageCircle(mActivity, entity.avatar, mFace, R.drawable.me_face);
            }
            if (entity.sex == 1) {
                mSexM.setChecked(true);
            } else if (entity.sex == 2) {
                mSexW.setChecked(true);
            } else {
                mSexNoe.setChecked(true);
            }
        }
        mCityCard.setOnCardViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCityPickerDialog.setSelectedData(mCityData[1]);
                mCityPickerDialog.show();
            }
        });
        mDayCard.setOnCardViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimerPickerDialog.setSelectedData(mDayCard.getCount());
                mTimerPickerDialog.show();
            }
        });
    }

    private void initViewModel() {
        mUserInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        mUserInfoViewModel.getShowProgressMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading();
                } else {
                    dismissLoading();
                }
            }
        });
        mUserInfoViewModel.getSuccessMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (mRegister) {
                    ActivityUtil.gotoMainActivity(mActivity);
                }
                finish();
            }
        });
    }

    private void initPickerView() {
        mCityPickerDialog = new CityPickerDialog(this, new CityPickerDialog.Callback() {
            @Override
            public void selected(String province, String city) {
                if (province != null) {
                    if (province.equals(city)) {
                        mCityData[0] = null;
                        mCityData[1] = province;
                        mCityCard.setContent(province);
                    } else {
                        mCityData[0] = province;
                        mCityData[1] = city;
                        mCityCard.setContent(province + city);
                    }
                }
            }
        });
        mTimerPickerDialog = new TimerPickerDialog(this, new TimerPickerDialog.Callback() {
            @Override
            public void onTimeSelect(String date) {
                mDate = date;
                mDayCard.setContent(date);
            }
        });
    }

    @OnClick({R.id.user_info_face, R.id.user_info_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_info_face:
                clickUserInfoFace();
                break;
            case R.id.user_info_next:
                clickUserInfoNext();
                break;
        }
    }

    private void clickUserInfoFace() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new OnRequestPermissionListener() {
                @Override
                public void onSuccess() {
                    imageGrantSuccess();
                }

                @Override
                public void onFailure() {

                }
            });
        } else {
            imageGrantSuccess();
        }
    }

    private void imageGrantSuccess() {
        if (mBottomMenuDialog == null) {
            mBottomMenuDialog = new BottomMenuDialog.Builder(mActivity)
                    .addMenu("拍照", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCameraTempFileName = UUID.randomUUID() + ".png";
                            ImageSelectorUtil.openActivityByCamera(mActivity, REQUEST_CAMERA_CLIP_CODE, SystemConstant.TEMP_IMAGE_PATH + mCameraTempFileName);
                            mBottomMenuDialog.dismiss();
                        }
                    }).addMenu("从相册选择", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImageSelectorUtil.openPhotoAndClip(mActivity, REQUEST_CODE);
                            mBottomMenuDialog.dismiss();
                        }
                    }).create();
        }
        mBottomMenuDialog.show();
    }

    private void clickUserInfoNext() {
        String nickName = mNick.getText().toString();
        if (TextUtils.isEmpty(nickName)) {//&& !mRegister
            ToastUtil.show("请输入昵称");
            return;
        }

        if (mSexM.isChecked()) {
            mSex = 1;
        } else if (mSexW.isChecked()) {
            mSex = 2;
        } else {
            mSex = 0;
        }
        mUserInfoViewModel.updateUserInfo(mActivity, nickName, mSex + "", !TextUtils.isEmpty(mCityData[1]) ? mCityData[1] : "",
                !TextUtils.isEmpty(mCityData[0]) ? mCityData[0] : "", mDate == null ? "" : mDate, mImagePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerPickerDialog != null) {
            mTimerPickerDialog.dismiss();
        }
        if (mCityPickerDialog != null) {
            mCityPickerDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRegister) {
            UserHelper.getInstance().getUserInfo(mActivity);
        }
        if (!mSetTheName) {
            ActivityUtil.gotoMainActivity(mActivity);
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO 处理返回结果
        switch (requestCode) {
            case REQUEST_CODE:
            case REQUEST_CAMERA_CLIP_CODE:
                if (data != null) {
                    List<String> list = data.getStringArrayListExtra(ImageSelectorUtil.SELECT_RESULT);
                    mImagePath = (list != null && list.size() > 0) ? list.get(0) : null;
                }
                break;
            case REQUEST_CAMERA_CODE:
                ImageSelectorUtil.openActivityByCamera(mActivity, REQUEST_CAMERA_CLIP_CODE, SystemConstant.TEMP_IMAGE_PATH + mCameraTempFileName);
                break;
        }
        if (!TextUtils.isEmpty(mImagePath)) {
            GlideUtil.loadImageCircle(mActivity, mImagePath, mFace);
        }
    }
}
