package com.example.variety.define;

import com.example.mynotezxy.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PaintColorPopupWindow extends PopupWindow{
    ImageView ivBlack,ivRed,ivYellow,ivGreen,ivBlue;
    View view;
    @SuppressLint("InlinedApi")
    public PaintColorPopupWindow(Context context,OnClickListener itemOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popupwindow_paint_color, null);
        ivBlack=(ImageView)view.findViewById(R.id.ivBlack);
        ivRed=(ImageView)view.findViewById(R.id.ivRed);
        ivYellow=(ImageView)view.findViewById(R.id.ivYellow);
        ivGreen=(ImageView)view.findViewById(R.id.ivGreen);
        ivBlue=(ImageView)view.findViewById(R.id.ivBlue);
        ivBlack.setOnClickListener(itemOnClick);
        ivRed.setOnClickListener(itemOnClick);
        ivYellow.setOnClickListener(itemOnClick);
        ivGreen.setOnClickListener(itemOnClick);
        ivBlue.setOnClickListener(itemOnClick);
        this.setContentView(view);//设置SelectPicPopupWindow的View
        this.setWidth(LayoutParams.MATCH_PARENT); //设置SelectPicPopupWindow弹出窗体的宽
        this.setHeight(LayoutParams.WRAP_CONTENT); //设置SelectPicPopupWindow弹出窗体的高
        this.setFocusable(true);//设置SelectPicPopupWindow弹出窗体可点击
        ColorDrawable dw = new ColorDrawable(0xffebebeb);//设置颜色
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = v.findViewById(R.id.color_Layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
