package com.zxy.demo.fragment;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.zxy.demo.R;
import com.zxy.demo.dialog.DelOrReportDialog;
import com.zxy.demo.dialog.SendCommentDialog;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.dialog.SimpleAlertDialog;
import com.zxy.frame.utils.PopupMenuUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ZDialogFragment extends BaseFragment {

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
    public int layoutId() {
        return R.layout.fragment_dialog;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.btn_alert, R.id.btn_bottom, R.id.btn_bottom2, R.id.btn_location, R.id.btn_system})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alert:
                new SimpleAlertDialog(getActivity()).setContent("你好？").show();
                break;
            case R.id.btn_bottom:
                new SendCommentDialog(getActivity()).show();
                break;
            case R.id.btn_bottom2:
                new DelOrReportDialog(getActivity()).show();
                break;
            case R.id.btn_location:
                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackgroundResource(R.drawable.ic_bg_popup_left);
                TextView tv = new TextView(getContext());
                tv.setText("内容1");
                TextView tv2 = new TextView(getContext());
                tv2.setText("内容2");
                ll.addView(tv);
                ll.addView(tv2);
                new PopupMenuUtil(getActivity()).showShadowPopup(mBtnLocation, ll);
                break;
            case R.id.btn_system:
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
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
