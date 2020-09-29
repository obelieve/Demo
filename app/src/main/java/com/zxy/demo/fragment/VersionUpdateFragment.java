package com.zxy.demo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.zxy.demo.MainActivity;
import com.zxy.demo.R;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.download.DownloadInterfaceImpl;
import com.zxy.frame.utils.LogUtil;
import com.zxy.frame.utils.StringUtil;
import com.zxy.frame.utils.SystemIntentUtil;
import com.zxy.frame.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * 版本更新
 */
public class VersionUpdateFragment extends ApiBaseFragment {

    public static final String EXTRA_VERSION_UPDATE_ENTITY = "extra_version_update_entity";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.rl_close)
    RelativeLayout rlClose;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;

    private VersionUpdateEntity mEntity;
    private DownloadHelper mDownloadHelper;
    private boolean mCancelDownload;
    private Disposable mDisposable;

    public static VersionUpdateFragment getInstance(VersionUpdateEntity entity) {
        VersionUpdateFragment fragment = new VersionUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_VERSION_UPDATE_ENTITY, entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_version_update;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mEntity = (VersionUpdateEntity) getArguments().getSerializable(EXTRA_VERSION_UPDATE_ENTITY);
        }
        if (mEntity == null)
            return;
        tvTitle.setText(String.format("发现新版本V%1$s", mEntity.getVersion_new()));
        tvContent.setText(Html.fromHtml(mEntity.getContent()));
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        rlClose.setVisibility(mEntity.getEnforce() == 1 ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.tv_update, R.id.rl_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                if (getActivity() instanceof ApiBaseActivity) {
                    ((ApiBaseActivity) getActivity()).requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, new ApiBaseActivity.OnRequestPermissionListener() {
                        @Override
                        public void onSuccess() {
                            startDownload();
                        }

                        @Override
                        public void onFailure() {
                            ToastUtil.show("失败");
                        }
                    });
                }
                break;
            case R.id.rl_close:
                if (getActivity() != null)
                    getActivity().finish();
                break;
        }
    }

    /**
     * 开始下载
     */
    private void startDownload() {
        if (mEntity == null || getActivity() == null) return;
        final String downloadUrl = mEntity.getDownload_url();
        final String apkName = mEntity.getVersion_new() + "_release.apk";
        final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + apkName;
        final File apkFile = new File(savePath);
        if (apkFile.exists() && !isBadAPK(getActivity(), savePath)) {
            SystemIntentUtil.openInstall(getActivity(), apkFile);
            if (mEntity.getEnforce() != 1) {
                getActivity().finish();
            }
            return;
        } else {
            rlContent.setVisibility(View.GONE);
        }
        if (mDownloadHelper == null) {
            mDownloadHelper = new DownloadHelper(getActivity(), new DownloadHelper.Callback() {
                @Override
                public void onClickCancel() {
                    cancelDownload();
                    mDownloadHelper.dismiss();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }

                @Override
                public void onClickHide() {
                    ToastUtil.show("已转到后台下载");
                    showNotification(UPDATE_NOTIFY_ID);
                    mDownloadHelper.dismiss();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
        }
        mDownloadHelper.setData(mEntity.getEnforce() == 1).show();
        mDisposable = ApiService.download(savePath, downloadUrl, new DownloadInterfaceImpl.DownloadCallback() {
            @Override
            public void onStart() {
                LogUtil.e("更新下载 开始");
            }

            @Override
            public void onError(Throwable e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadHelper.dismiss();
                            ToastUtil.show("下载失败");
                            rlContent.setVisibility(View.VISIBLE);
                            LogUtil.e("更新下载 失败");
                        }
                    });
                }
            }

            @Override
            public void onProgress(long total, long current) {
                String currentStr = StringUtil.formatFileSize(current);
                String totalStr = StringUtil.formatFileSize(total);
                mDownloadHelper.setProgress(String.format("%1$s/%2$s", currentStr, totalStr), (int) total, (int) current);
                updateNotification(UPDATE_NOTIFY_ID, (int) total, (int) current);
            }

            @Override
            public void onCompleted(File file, long fileSize) {
                if (null != mNotification) {
                    // 修改进度条
                    mNotificationManager.cancel(UPDATE_NOTIFY_ID);
                }
                if (file.exists()) {
                    SystemIntentUtil.openInstall(getActivity(), file);
                }
                if (mEntity.getEnforce() != 1) {
                    mDownloadHelper.dismiss();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            }

            @Override
            public boolean isCancelDownload() {
                return mCancelDownload;
            }
        });
    }

    /**
     * 取消下载
     */
    private void cancelDownload() {
        mCancelDownload = true;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public static class DownloadHelper {

        private Activity mActivity;
        private Callback mCallback;
        private Dialog mDialog;

        private TextView tvProgress;
        private NumberProgressBar progressBar;
        private TextView tvCancel;
        private TextView tvHide;
        private boolean mEnforce;

        public DownloadHelper(Activity activity, Callback callback) {
            this.mActivity = activity;
            this.mCallback = callback;
            this.mDialog = getDialog();
        }

        public DownloadHelper setData(boolean enforce) {
            mEnforce = enforce;
            return this;
        }

        public DownloadHelper setProgress(String text, int max, int progress) {
            // mDialog = getDialog();
            tvProgress.setText(text);
            progressBar.setMax(max);
            progressBar.setProgress(progress);
            LogUtil.e("下载 setProgress:" + tvProgress + " " + text + " max=" + max + " progress " + progress);
            return this;
        }

        public void show() {
            LogUtil.e("下载 show");
            getDialog().show();
        }

        public void dismiss() {
            LogUtil.e("下载 dismiss");
            getDialog().dismiss();
        }

        private Dialog getDialog() {
            if (mDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View view = View.inflate(mActivity, R.layout.layout_download_dialog, null);
                tvProgress = view.findViewById(R.id.download_progress);
                progressBar = view.findViewById(R.id.download_bar);
                tvCancel = view.findViewById(R.id.download_cancel);
                tvHide = view.findViewById(R.id.download_hide);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCallback != null) {
                            mCallback.onClickCancel();
                        }
                    }
                });
                tvHide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCallback != null) {
                            mCallback.onClickHide();
                        }
                    }
                });
                builder.setView(view);
                builder.setCancelable(false);
                mDialog = builder.create();
            }
            tvCancel.setVisibility(mEnforce ? View.GONE : View.VISIBLE);
            tvHide.setVisibility(mEnforce ? View.GONE : View.VISIBLE);
            tvHide.setVisibility(View.GONE);
            return mDialog;
        }

        public interface Callback {
            void onClickCancel();

            void onClickHide();
        }
    }

    //通知栏 显示下载
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private static final int UPDATE_NOTIFY_ID = 100;
    public static final String CALENDAR_ID = "channel_01";

    /**
     * 下载通知栏
     *
     * @param notificationId
     */
    private void showNotification(int notificationId) {
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CALENDAR_ID, "123",
                    NotificationManager.IMPORTANCE_LOW);
            RemoteViews contentView = new RemoteViews(getActivity().getPackageName(),
                    R.layout.layout_update_progress_notification);
            contentView.setTextViewText(R.id.title, "更新包下载中");
            // 是否绕过请勿打扰模式
            notificationChannel.canBypassDnd();
            // 设置绕过请勿打扰模式
            notificationChannel.setBypassDnd(true);
            // 桌面Launcher的消息角标
            notificationChannel.canShowBadge();
            // 设置显示桌面Launcher的消息角标
            notificationChannel.setShowBadge(true);
//            // 设置渠道描述
//            notificationChannel.setDescription("正在下载更新包");
            // 设置通知出现时声音，默认通知是有声音的
//            notificationChannel.setSound(null, null);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400,
//                    300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CALENDAR_ID);
            builder.setAutoCancel(true);
            builder.setCustomContentView(contentView);
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 11,
                    new Intent(getContext(), MainActivity.class), 0);
            mNotification = builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            // 创建通知对象
            mNotification = new Notification();
            // 设置通知栏滚动显示文字
            mNotification.tickerText = "更新包下载中";
            // 设置显示时间
            mNotification.when = System.currentTimeMillis();
            // 设置通知显示的图标
            mNotification.icon = R.mipmap.ic_launcher;
            // 设置通知的特性: 通知被点击后，自动消失
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
            // 设置通知的显示视图
            mNotification.contentView = new RemoteViews(
                    getActivity().getPackageName(),
                    R.layout.layout_update_progress_notification);
        }
        // 发出通知
        mNotificationManager.notify(notificationId, mNotification);
    }


    /**
     * 更新通知栏
     *
     * @param notificationId
     * @param total
     * @param progress
     */
    private void updateNotification(int notificationId, int total, int progress) {
        if (null != mNotification) {
            // 修改进度条
            mNotification.contentView.setProgressBar(R.id.pBar, total, progress, false);
            mNotificationManager.notify(notificationId, mNotification);
        }
    }


    /**
     * 获取安装包状态
     *
     * @param ctx
     * @param apkPath
     * @return
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
}
