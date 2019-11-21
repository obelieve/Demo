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

public class PencilSizePopupWindow extends PopupWindow{
	ImageView ivSize1,ivSize2,ivSize3,ivSize4,ivSize5,ivSize6;
	View view;
	@SuppressLint("InlinedApi")
	public PencilSizePopupWindow(Context context,OnClickListener itemOnClick){
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popupwindow_pencil_size, null);
		ivSize1=(ImageView)view.findViewById(R.id.ivSize1);
		ivSize2=(ImageView)view.findViewById(R.id.ivSize2);
		ivSize3=(ImageView)view.findViewById(R.id.ivSize3);
		ivSize4=(ImageView)view.findViewById(R.id.ivSize4);
		ivSize5=(ImageView)view.findViewById(R.id.ivSize5);
		ivSize6=(ImageView)view.findViewById(R.id.ivSize6);
		ivSize1.setOnClickListener(itemOnClick);
		ivSize2.setOnClickListener(itemOnClick);
		ivSize3.setOnClickListener(itemOnClick);
		ivSize4.setOnClickListener(itemOnClick);
		ivSize5.setOnClickListener(itemOnClick);
		ivSize6.setOnClickListener(itemOnClick);
		this.setContentView(view);//设置SelectPicPopupWindow的View
		this.setWidth(LayoutParams.MATCH_PARENT); //设置SelectPicPopupWindow弹出窗体的宽
		this.setHeight(LayoutParams.WRAP_CONTENT); //设置SelectPicPopupWindow弹出窗体的高
		this.setFocusable(true);//设置SelectPicPopupWindow弹出窗体可点击
		ColorDrawable dw = new ColorDrawable(0xffaaaaaa);//设置颜色
		this.setBackgroundDrawable(dw);
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = v.findViewById(R.id.size_Layout).getTop();
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
