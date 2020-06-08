package com.zxy.frame.base;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zxy.frame.dialog.LoadDialog;
import com.zxy.frame.utils.ActivityUtil;
import com.zxy.frame.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10240;

    private int mStatusBarColor = Color.WHITE;
    protected boolean mNeedInsetStatusBar = true;
    protected boolean mLightStatusBar = true;

    private LoadDialog mLoadDialog;
    /**
     * Activity 实例.
     */
    protected AppCompatActivity mActivity;
    private OnRequestPermissionListener mPermissionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setScreenOrientation();
        if (mNeedInsetStatusBar) {
            StatusBarUtil.setStatusBarColor(this, getStatusBarColor());
        } else {
            StatusBarUtil.setStatusBarTranslucentStatus(this);
        }
        StatusBarUtil.setWindowLightStatusBar(this, isLightStatusBar());
        if (layoutId() != 0) {
            setContentView(layoutId());
        }
        ButterKnife.bind(this);
        initCreateAfterView(savedInstanceState);
    }

    protected abstract int layoutId();

    protected abstract void initCreateAfterView(Bundle savedInstanceState);

    protected void setScreenOrientation() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        } else {
            try {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
            } catch (Exception e) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            }
        }
    }

    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    public boolean isLightStatusBar() {
        return mLightStatusBar;
    }

    public void showLoading(){
        if(mLoadDialog==null){
            mLoadDialog = new LoadDialog(this);
        }
        mLoadDialog.show();
    }
    public void dismissLoading(){
        if(mLoadDialog!=null){
            mLoadDialog.dismiss();
        }
    }
    /**
     * 请求权限BaseActivity$OnRequestPermissionListener
     *
     * @param permissions
     * @param listener
     */
    public void requestPermission(final String[] permissions, OnRequestPermissionListener listener) {
        this.mPermissionListener = listener;
        //收集未授权或者拒绝过的权限
        ArrayList<String> deniedPermissionList = new ArrayList<>();
        for (String per : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, per);
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                deniedPermissionList.add(per);
            }
        }

        if (deniedPermissionList.isEmpty()) {
            if (mPermissionListener != null) {
                mPermissionListener.onSuccess();
            }
        } else {
            String[] permissionArray = deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            ActivityCompat.requestPermissions(this, permissionArray, PERMISSION_REQUEST_CODE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE == requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        showTipsDialog(permissions);
                    } else {
                        showSettingDialog();
                    }
                    return;
                }
            }

            if (mPermissionListener != null) {
                mPermissionListener.onSuccess();
            }
        }
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog(final String[] permissions) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要授权相关权限")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mPermissionListener != null) {
                            mPermissionListener.onFailure();
                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                    }
                }).show();
    }

    private void showSettingDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mPermissionListener != null) {
                            mPermissionListener.onFailure();
                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtil.openAppSettings(mActivity);

                    }
                }).show();
    }

    public interface OnRequestPermissionListener {

        void onSuccess();

        void onFailure();
    }
}
