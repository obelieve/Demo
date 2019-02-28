package com.zxy.demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.utility.FileUtil;
import com.zxy.utility.LogUtil;
import com.zxy.utility.SPUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * android 权限
 * 1. AndroidManifest.xml 权限声明都要写
 * 2.权限授权差异
 * 2.1 android 5.0以及以下版本 只要在AndroidManifest.xml上标识，安装时会列出权限列表，让用户确定安装。
 * 2.2 android 6.0以及以上版本 权限声明
 * 在安全权限只要xml声明下，(PS:网络权限等)
 * 在危险权限还需要运行时用户授权处理，权限组中的权限只有一个有授权，其他组内的权限都可以使用。
 * 统一提供弹窗提示是否授权。
 * 主要方法
 * Manifest.permission.WRITE_EXTERNAL_STORAGE 权限常量
 * PackageManager.PERMISSION_GRANTED int 已授权
 * 1.ContextCompat.checkSelfPermission(...) 是否授权
 * 2.ActivityCompat.shouldShowRequestPermissionRationale()解释权限时，这个返回会在一次用户授权禁止后，下次进入会是true返回。
 * 如果用户(勾选) 禁止后不再询问，会返回false 如果设备规范禁止应用具有该权限，此方法也会返回 false。
 * **3.禁止后不再询问 也会不再弹出权限授权的对话框；
 * 4.ActivityCompat.requestPermissions(...)请求权限会弹出权限对话框，并且异步执行后返回；
 * 5.Activity.onRequestPermissionsResult(...)授权结果返回；
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_SETTING = 0;

    public static final int REQUEST_PERMISSION_STORAGE = 0;

    EditText et_src, et_des, et_file;
    Button btn_shift, btn_save, btn_reset;
    TextView tv;
    boolean isRun;
    boolean isGranted;

    public Activity getActivity(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e();
        ToastUtil.init(this.getApplicationContext());
        SPUtil.init(this.getApplicationContext(), "mobi");
        setContentView(R.layout.activity_main);
        et_src = findViewById(R.id.et_src);
        et_des = findViewById(R.id.et_des);
        et_file = findViewById(R.id.et_file);
        btn_shift = findViewById(R.id.btn_shift);
        btn_save = findViewById(R.id.btn_save);
        btn_reset = findViewById(R.id.btn_reset);
        tv = findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        setTextContent();

        btn_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tv.getText().toString();
                try {
                    String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss").format(new Date()) + ".txt";
                    File file = new File(getExternalFilesDir(null), fileName);
                    FileUtil.writeBytesDescriptionFile(new StringBufferInputStream(s),file);
                    tv.append("成功：保存日志文件->"+file.getAbsolutePath()+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    tv.append("\n 保存日志错误:" + Log.getStackTraceString(e));
                }
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.getInstance().getSP().edit().clear().apply();
                setTextContent();
            }
        });
        requestPermission();
    }

    private void setTextContent() {
        et_src.setText(SPUtil.getInstance().getSP().getString("src", Environment.getExternalStorageDirectory() + "/Download"));
        et_des.setText(SPUtil.getInstance().getSP().getString("des", Environment.getExternalStorageDirectory() + "/Android/data/com.amazon.kindlefc"));
        et_file.setText(SPUtil.getInstance().getSP().getString("file", ".mobi"));
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                LogUtil.e(Thread.currentThread() + "");
                new AlertDialog.Builder(this).setTitle("标题")
                        .setMessage("使用存储权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                    }
                }).show();
                ToastUtil.show("介绍");
                LogUtil.e("询问");

            } else {
                LogUtil.e("不再询问");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
//
        } else {
            isGranted = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.e("授权" + Thread.currentThread());
                isGranted = true;
            } else {
                LogUtil.e("拒绝" + Thread.currentThread());
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,

                        permissions[0])) {

                    ToastUtil.show("点击权限，并打开全部权限");

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                    Uri uri = Uri.fromParts("package", getPackageName(), null);

                    intent.setData(uri);

                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                } else {
                    finish();
                }
                isGranted = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            LogUtil.e("requestCode:" + requestCode + " CANCEL:" + Activity.RESULT_CANCELED);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                LogUtil.e("返回未授权");
                isGranted = false;
            } else {
                LogUtil.e("返回已授权");
                isGranted = true;
            }
        }
    }

    private void clickBtn() {
        if (isRun || !isGranted) {
            return;
        }
        final String src = et_src.getText().toString();
        final String des = et_des.getText().toString();
        final String file = et_file.getText().toString();
        SPUtil.getInstance().getSP().edit()
                .putString("scr", src)
                .putString("des", des)
                .putString("file", file).apply();
        tv.setText("移动：\n");
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRun = true;
                File file1 = new File(src);
                File file2 = new File(des);
                if (file1.listFiles() == null) {
                    return;
                }
                for (final File cFile : file1.listFiles()) {
                    if (cFile.getName().contains(file)) {
                        final File desFile = new File(file2, cFile.getName());
                        try {
                            FileUtil.copyFile(cFile, desFile);
                            cFile.delete();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.append("成功:" + cFile.getAbsolutePath() + "->\n"
                                            + desFile.getAbsolutePath() + "\n"
                                            + cFile.getName() + " 是否已经删除:" + (!cFile.exists()) + "\n"
                                            + "==============================================\n");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            final String errorMsg = e.getMessage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.append("失败:" + errorMsg + "\n" + cFile.getAbsolutePath() + "->\n"
                                            + desFile.getAbsolutePath() + "\n"
                                            + cFile.getName() + " 是否已经删除:" + (!cFile.exists()) + "\n"
                                            + "==============================================\n");
                                }
                            });
                        }
                    }
                }
                isRun = false;
            }
        }).start();
    }
}
