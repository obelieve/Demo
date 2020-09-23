package com.github.obelieve.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.obelieve.App;
import com.github.obelieve.bean.VersionUpdateEntity;
import com.github.obelieve.community.R;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.utils.others.SystemConstant;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.net.download.DownloadInterfaceImpl;
import com.zxy.frame.utils.FileUtil;
import com.zxy.frame.utils.StatusBarUtil;
import com.zxy.frame.utils.StringUtil;
import com.zxy.frame.utils.SystemIntentUtil;
import com.zxy.frame.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;

/**
 * 自升级对话框
 * Created by TQ on 2017/11/28.
 */

public class UpdateSelfActivity extends ApiBaseActivity {

    public static final String EXTRA_UPDATE_DATA = "EXTRA_UPDATE_DATA";
    //    自升级显示对话框布局
    @BindView(R.id.update_dialog_layout)
    RelativeLayout mDialogLayout;
    //    显示对话框标题
    @BindView(R.id.update_tile)
    TextView title;
    //    显示对话框内容
    @BindView(R.id.update_content)
    TextView content;
    //    立即升级按钮
    @BindView(R.id.update_now)
    TextView confirmBtn;
    //    取消按钮
    @BindView(R.id.update_wait)
    RelativeLayout cancelBtn;
    //    自升级信息
    VersionUpdateEntity mUpdateEntity;

    @Override
    protected int layoutId() {
        return R.layout.activity_update;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarTranslucentStatus(this);
        if (getIntent() != null) {
            mUpdateEntity = (VersionUpdateEntity) getIntent().getSerializableExtra(EXTRA_UPDATE_DATA);
        }
        title.setText(getString(R.string.update_title, mUpdateEntity.getVersion_new()));
        content.setText(Html.fromHtml(mUpdateEntity.getRemark()));
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        cancelBtn.setVisibility(mUpdateEntity.getEnforce() == 1 ? View.GONE : View.VISIBLE);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, new OnRequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        showDownloadDialog();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }


    public void finishActivity() {
        finish();
        overridePendingTransition(0, 0);
    }

    public void showDialogLayout(boolean isShow) {
        if (mDialogLayout != null) {
            mDialogLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }


    private static final int UPDATE_NOTIFY_ID = 100;

    //    进度文本
    private TextView mProgressTv;
    //    进度条
    private NumberProgressBar mProgressBar;
    //    下载对话框
    private AlertDialog mDownloadDialog;
    //    网络请求类

    private NotificationManager manager;
    private Notification mNotification;
    private boolean mCancelDownload;

    /**
     * 显示下载对话框
     */
    public void showDownloadDialog() {
        String apkName = mUpdateEntity.getVersion_new() + "_Gemini.apk";
        final File apkFile = FileUtil.getFile(SystemConstant.TEMP_APK_PATH, apkName);
        if (FileUtil.exist(apkFile) && !isBadAPK(mActivity, SystemConstant.TEMP_APK_PATH + apkName)) {
            SystemIntentUtil.openInstall(mActivity, apkFile);
            if (mUpdateEntity.getEnforce() != 1) {
                finishActivity();
            }
            return;
        } else {
            showDialogLayout(false);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_download, null);
        mProgressTv = view.findViewById(R.id.download_progress);
        mProgressBar = view.findViewById(R.id.download_bar);
        TextView cancelBtn = view.findViewById(R.id.download_cancel);
        TextView hideBtn = view.findViewById(R.id.download_hide);
        cancelBtn.setVisibility(mUpdateEntity.getEnforce() == 1 ? View.GONE : View.VISIBLE);
        hideBtn.setVisibility(mUpdateEntity.getEnforce() == 1 ? View.GONE : View.VISIBLE);
        hideBtn.setVisibility(View.GONE);
        builder.setView(view);
        builder.setCancelable(false);
        mDownloadDialog = builder.show();
        downloadAPK();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCancelDownload = true;
                mDownloadDialog.dismiss();
                finishActivity();
            }
        });
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(App.getContext(), R.string.update_hide_toast, Toast.LENGTH_SHORT).show();
                showNotification(UPDATE_NOTIFY_ID);
                mDownloadDialog.dismiss();
                finishActivity();
            }
        });
    }

    /**
     * 描述：获取安装包状态
     * 2016/12/20 10:44 by MJ
     */
    private boolean isBadAPK(Context ctx, String apkPath) {
        PackageInfo apkPackageInfo = null;
        try {
            PackageManager pm = ctx.getPackageManager();
            apkPackageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkPackageInfo == null;
    }

    private void showNotification(int notificationId) {
        manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知对象
        mNotification = new Notification();
        // 设置通知栏滚动显示文字
        mNotification.tickerText = "正在下载更新包";
        // 设置显示时间
        mNotification.when = System.currentTimeMillis();
        // 设置通知显示的图标
        mNotification.icon = R.mipmap.ic_launcher;
        // 设置通知的特性: 通知被点击后，自动消失
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        // 设置通知的显示视图

        // 设置通知的显示视图
        mNotification.contentView = new RemoteViews(
                mActivity.getPackageName(),
                R.layout.notification_update_progress);
        // 发出通知
        manager.notify(notificationId, mNotification);
    }

    private void updateProgress(int notificationId, int total, int progress) {
        if (null != mNotification) {
            // 修改进度条
            mNotification.contentView.setProgressBar(R.id.pBar, total, progress, false);
            manager.notify(notificationId, mNotification);
        }
    }

    /**
     * 开始下载apk
     */
    private void downloadAPK() {
        ApiServiceWrapper.download(SystemConstant.TEMP_APK_PATH + mUpdateEntity.getVersion_new() + "_Aries.apk", mUpdateEntity.getDownload_url(),
                new DownloadInterfaceImpl.DownloadCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDownloadDialog.dismiss();
                                ToastUtil.show(App.getContext().getResources().getString(R.string.update_download_fail));
                                showDialogLayout(true);
                            }
                        });
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String doneStr = StringUtil.formatFileSize(current);
                                String totalStr = StringUtil.formatFileSize(total);
                                if (mProgressTv != null) {
                                    mProgressTv.setText(mActivity.getString(R.string.update_progress, doneStr, totalStr));
                                }
                                if (mProgressBar != null) {
                                    mProgressBar.setMax((int) total);
                                    mProgressBar.setProgress((int) current);
                                }
                                updateProgress(UPDATE_NOTIFY_ID, (int) total, (int) current);
                            }
                        });
                    }

                    @Override
                    public void onCompleted(File file, long fileSize) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != mNotification) {
                                    // 修改进度条
                                    manager.cancel(UPDATE_NOTIFY_ID);
                                }
                                if (file.exists()) {
                                    SystemIntentUtil.openInstall(mActivity, file);
                                }
                                if (mUpdateEntity.getEnforce() != 1) {
                                    mDownloadDialog.dismiss();
                                    finishActivity();
                                }
                            }
                        });
                    }

                    @Override
                    public boolean isCancelDownload() {
                        return mCancelDownload;
                    }
                });
    }

}
