package com.example.variety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.mynotezxy.R;
import com.example.variety.define.GraffitoView;
import com.example.variety.define.PaintColorPopupWindow;
import com.example.variety.define.PencilSizePopupWindow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GraffitoActivity extends Activity{
	GridView gvBottomMenu;
	GraffitoView graffitoView;
	int [] gvItems={//GridView里面的 图片项
			R.drawable.pencil,
			R.drawable.paint_color,
			R.drawable.paint_erazer,
			R.drawable.paint_delete,
	};
	PencilSizePopupWindow sizePopupWindow;//pop弹出框
	PaintColorPopupWindow colorPopupWindow;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graffito);
		graffitoView=(GraffitoView)findViewById(R.id.graffitoView);
		initActionbar();
		initBottomMenu();
	}
	@SuppressLint("NewApi")
	public void initActionbar() {//自定义actionbar菜单
		View customView=getLayoutInflater().inflate(R.layout.actionbar_third, null);
		ImageView ivBack,ivAccept;
		ivBack=(ImageView)customView.findViewById(R.id.ivBack);
		ivAccept=(ImageView)customView.findViewById(R.id.ivAccept);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(customView);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GraffitoActivity.this.finish();
			}
		});
		ivAccept.setOnClickListener(new OnClickListener() {
			//返回编辑页面
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				String path =graffitoView.saveBitmap();
				bundle.putString("graffitoPath", path);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				GraffitoActivity.this.finish();
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
		SimpleAdapter mAdapter = new SimpleAdapter(GraffitoActivity.this, menus,R.layout.bottommenu_item_button, new String[]{"image"}, new int[]{R.id.ivItem});
		gvBottomMenu.setAdapter(mAdapter);
	}
	class BottomMenuClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
								long id) {
			// TODO Auto-generated method stub
			switch (position) {
				case 0://画笔
					sizePopupWindow=new PencilSizePopupWindow(GraffitoActivity.this, sizeItemOnClick);
					sizePopupWindow.showAtLocation(gvBottomMenu,Gravity.BOTTOM, 0,v.getHeight());//view为要参考的组件，gravity表示相对位置，intx，iny表示偏移。
					//设置layout在PopupWindow中显示的位置
					break;
				case 1://颜色
					colorPopupWindow=new PaintColorPopupWindow(GraffitoActivity.this, colorItemOnClick);
					colorPopupWindow.showAtLocation(gvBottomMenu, Gravity.BOTTOM, 0, v.getHeight());
					break;
				case 2://橡皮擦
					graffitoView.eraser();
					break;
				case 3://清屏
					graffitoView.deleteAll();
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
					graffitoView.selectHandWritetSize(0);
					break;
				case R.id.ivSize2:
					graffitoView.selectHandWritetSize(1);
					break;
				case R.id.ivSize3:
					graffitoView.selectHandWritetSize(2);
					break;
				case R.id.ivSize4:
					graffitoView.selectHandWritetSize(3);
					break;
				case R.id.ivSize5:
					graffitoView.selectHandWritetSize(4);
					break;
				case R.id.ivSize6:
					graffitoView.selectHandWritetSize(5);
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
			colorPopupWindow.dismiss();
			switch (v.getId()) {
				case R.id.ivBlack:
					graffitoView.selectHandWriteColor(0);
					break;
				case R.id.ivRed:
					graffitoView.selectHandWriteColor(1);
					break;
				case R.id.ivYellow:
					graffitoView.selectHandWriteColor(2);
					break;
				case R.id.ivGreen:
					graffitoView.selectHandWriteColor(3);
					break;
				case R.id.ivBlue:
					graffitoView.selectHandWriteColor(4);
					break;
				default:
					break;
			}

		}
	};

}
