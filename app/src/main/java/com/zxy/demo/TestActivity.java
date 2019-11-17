package com.zxy.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zxy.frame.dialog.BaseDialog;
import com.zxy.frame.dialog.SimpleAlertDialog;
import com.zxy.utility.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        LogUtil.e(getSupportFragmentManager().getFragments() + "============");

    }

    @OnClick(R.id.fl_content)
    public void onViewClicked() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setMessage("确定退出吗？")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
        new SimpleAlertDialog(this).setContent("你好？").show();
    }


}
