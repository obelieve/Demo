package com.example.variety;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.mynotezxy.R;
import com.example.mynotezxy.R.drawable;
import com.example.variety.define.PaintColorPopupWindow;
import com.example.variety.define.PencilSizePopupWindow;
import com.example.variety.define.TouchView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class HandWriteActivity extends Activity{
	private GridView gvBottomMenu;
	private EditText etContent;
	private TouchView touchView;
	int [] gvItems={//GridView里面的 图片项
			R.drawable.pencil,
			R.drawable.paint_color,
			R.drawable.handwrite_space,
			R.drawable.handwrite_enter,
			R.drawable.handwrite_back
	};
	PencilSizePopupWindow sizePopupWindow;//pop弹出框
	PaintColorPopupWindow colorPopupWindow;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handwrite);
		etContent=(EditText)findViewById(R.id.etContent);
		touchView=(TouchView)findViewById(R.id.touchView);
		touchView.setBitmapHandle(handler);
		initActionbar();
		initBottomMenu();
	}
	@SuppressLint("NewApi")
	public void initActionbar() {
		View customView =getLayoutInflater().inflate(R.layout.actionbar_third, null);
		ImageView ivBack,ivAccept;
		ivBack=(ImageView)customView.findViewById(R.id.ivBack);
		ivAccept=(ImageView)customView.findViewById(R.id.ivAccept);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(customView);//自定义view替换到actionbar中
		//getActionBar().setDisplayShowCustomEnabled(true);//使自定义的普通View能在title栏显示
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HandWriteActivity.this.finish();
			}
		});
		ivAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =getIntent();
				Bundle bundle=new Bundle();
				String path=saveBitmap();
				bundle.putString("handWritePath",path);
				intent.putExtras(bundle);
				setResult(RESULT_OK,intent);
				HandWriteActivity.this.finish();
			}
		});
	}
	public void initBottomMenu() {//初始化底下菜单
		gvBottomMenu=(GridView)findViewById(R.id.gvBottomMenu);
		gvBottomMenu.setOnItemClickListener(new BottomMenuClickEvent());
		ArrayList<Map<String,Object>> menus = new ArrayList<Map<String,Object>>();
		for(int i = 0;i < gvItems.length;i++){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("image",gvItems[i]);
			menus.add(item);
		}
		gvBottomMenu.setNumColumns(gvItems.length);
		SimpleAdapter mAdapter = new SimpleAdapter(HandWriteActivity.this, menus,R.layout.bottommenu_item_button, new String[]{"image"}, new int[]{R.id.ivItem});
		gvBottomMenu.setAdapter(mAdapter);
	}
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = new Bundle();
			bundle = msg.getData();
			Bitmap myBitmap = bundle.getParcelable("bitmap");
			etInsertBitmap(myBitmap);
		}
	};
	public void etInsertBitmap(Bitmap bitmap) {
		int w=bitmap.getWidth();
		int h=bitmap.getHeight();
		float scaleW = (float) (72f/w);
		float scaleH = (float) (108f/h);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleW, scaleH);//放大或缩小图片，改成图片都是72，108px
		bitmap=Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		SpannableString ss = new SpannableString("1");
		ImageSpan span = new ImageSpan(bitmap, ImageSpan.ALIGN_BOTTOM);//align匹配底部
		ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//inclusive 包括的，exclusive独有的
		etContent.append(ss);//.append在原来的地方后面添加，.settext是原来变空白，再添加
	}
	public String saveBitmap() {//保存手写图片
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");//时间格式
		Date curDate =new Date(System.currentTimeMillis());//获取当前时间
		String str=dateFormat.format(curDate);
		String handWritePath="";
		str =str+"handWrite.png";
		File dir =new File("/sdcard/MyNotezxy/");
		File file =new File("/sdcard/MyNotezxy/",str);
		if(!dir.exists()){
			dir.mkdir();//如果目录不存在，创建目录
		}
		else {
			if(file.exists()){
				file.delete();
			}
		}
		etContent.setDrawingCacheEnabled(true);//开启cache缓存
		Bitmap cutHandWriteBitmap =Bitmap.createBitmap(etContent.getDrawingCache());//保存
		etContent.setDrawingCacheEnabled(false);//销毁cache缓存
		try{
			handWritePath="/sdcard/MyNotezxy/"+str;//保存图片路径
			FileOutputStream out=new FileOutputStream(file);//
			cutHandWriteBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//压缩图片
			out.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return handWritePath;
	}
	class BottomMenuClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
								long id) {
			// TODO Auto-generated method stub
			switch (position) {
				case 0://画笔
					sizePopupWindow=new PencilSizePopupWindow(HandWriteActivity.this, sizeItemOnClick);
					sizePopupWindow.showAtLocation(gvBottomMenu,Gravity.BOTTOM, 0,v.getHeight());//view为要参考的组件，gravity表示相对位置，intx，iny表示偏移。
					//设置layout在PopupWindow中显示的位置
					break;
				case 1://颜色
					colorPopupWindow=new PaintColorPopupWindow(HandWriteActivity.this, colorItemOnClick);
					colorPopupWindow.showAtLocation(gvBottomMenu, Gravity.BOTTOM, 0, v.getHeight());
					break;
				case 2://空格
					etContent.append(" ");
					break;
				case 3://换行
					etContent.append("\n");
					break;
				case 4://删除
					Editable editable=etContent.getText();
					int selectionEnd=etContent.getSelectionEnd();//返回选定文本的结束位置
					Log.i("weiz", ""+selectionEnd);
					if(selectionEnd<=1){
						etContent.setText("");
					}else {
						CharSequence charSequence=editable.subSequence(0, selectionEnd-1);
						etContent.setText(charSequence);
						etContent.setSelection(selectionEnd-1);
					}
					break;
				default:
					break;
			}
		}
	}
	OnClickListener sizeItemOnClick=new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sizePopupWindow.dismiss();
			switch (v.getId()) {
				case R.id.ivSize1:
					touchView.selectHandWritetSize(0);
					break;
				case R.id.ivSize2:
					touchView.selectHandWritetSize(1);
					break;
				case R.id.ivSize3:
					touchView.selectHandWritetSize(2);
					break;
				case R.id.ivSize4:
					touchView.selectHandWritetSize(3);
					break;
				case R.id.ivSize5:
					touchView.selectHandWritetSize(4);
					break;
				case R.id.ivSize6:
					touchView.selectHandWritetSize(5);
					break;

				default:
					break;
			}
		}
	};
	OnClickListener colorItemOnClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			colorPopupWindow.dismiss();//撤销菜单
			switch (v.getId()) {
				case R.id.ivBlack:
					touchView.selectHandWriteColor(0);
					break;
				case R.id.ivRed:
					touchView.selectHandWriteColor(1);
					break;
				case R.id.ivYellow:
					touchView.selectHandWriteColor(2);
					break;
				case R.id.ivGreen:
					touchView.selectHandWriteColor(3);
					break;
				case R.id.ivBlue:
					touchView.selectHandWriteColor(4);
					break;
				default:
					break;
			}

		}
	};
}
