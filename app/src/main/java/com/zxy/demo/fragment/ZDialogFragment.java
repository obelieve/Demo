package com.zxy.demo.fragment;

import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.zxy.demo.R;
import com.zxy.demo.databinding.FragmentDialogBinding;
import com.zxy.demo.dialog.DelOrReportDialog;
import com.zxy.demo.dialog.SendCommentDialog;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.dialog.SimpleAlertDialog;
import com.zxy.frame.utils.PopupMenuUtil;

public class ZDialogFragment extends ApiBaseFragment<FragmentDialogBinding> implements View.OnClickListener{



    @Override
    protected void initView() {
        mViewBinding.btnAlert.setOnClickListener(this);
        mViewBinding.btnBottom.setOnClickListener(this);
        mViewBinding.btnBottom2.setOnClickListener(this);
        mViewBinding.btnLocation.setOnClickListener(this);
        mViewBinding.btnSystem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
                new PopupMenuUtil(getActivity()).showShadowPopup(mViewBinding.btnLocation, ll);
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
