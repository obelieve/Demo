package com.zxy.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zxy.demo.dialog.DelOrReportDialog;
import com.zxy.demo.dialog.SendCommentDialog;
import com.zxy.frame.dialog.SimpleAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends AppCompatActivity {


    @BindView(R.id.btn_alert)
    Button mBtnAlert;
    @BindView(R.id.btn_bottom)
    Button mBtnBottom;
    @BindView(R.id.btn_bottom2)
    Button mBtnBottom2;
    @BindView(R.id.btn_location)
    Button mBtnLocation;
    @BindView(R.id.btn_system)
    Button mBtnSystem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_alert, R.id.btn_bottom, R.id.btn_bottom2, R.id.btn_location, R.id.btn_system})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alert:
                new SimpleAlertDialog(this).setContent("你好？").show();
                break;
            case R.id.btn_bottom:
                new SendCommentDialog(this).show();
                break;
            case R.id.btn_bottom2:
                new DelOrReportDialog(this).show();
                break;
            case R.id.btn_location:
                break;
            case R.id.btn_system:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("确定退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
        }
    }

}
