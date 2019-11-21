package com.example.variety;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mynotezxy.R;
import com.example.variety.define.DatabaseOperation;
import com.example.variety.define.LinedEditText;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class WriteActivity extends Activity{
	ImageView ivOVerflow,ivBack,ivAccept;
	LinedEditText etContent;
	SQLiteDatabase db;
	DatabaseOperation dOperation;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_second);
		getActionBar().setDisplayShowCustomEnabled(true);//使自定义的普通View能在title栏显示
		dOperation=new DatabaseOperation(this,db);
		etContent=(LinedEditText)findViewById(R.id.etContent);
		ivOVerflow=(ImageView)findViewById(R.id.ivOverflow);
		ivBack=(ImageView)findViewById(R.id.ivBack);
		ivAccept=(ImageView)findViewById(R.id.ivAccept);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WriteActivity.this.finish();
			}
		});
		ivAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				String context=etContent.getText().toString();
				String title=context;
				if(context.isEmpty()){
					Toast.makeText(WriteActivity.this,"记事内容为空!", Toast.LENGTH_LONG).show();
				}else {
					SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH-mm");
					Date curDate =new Date(System.currentTimeMillis());
					String time =dateFormat.format(curDate);
					dOperation.create_db();
					dOperation.insert_db(title, context, time);
					dOperation.close_db();
					WriteActivity.this.finish();
				}
			}
		});
		ivOVerflow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//点击3个点按钮时会出现下拉菜单 分享 和添加提醒按钮
				// TODO Auto-generated method stub
				PopupMenu popup =new PopupMenu(WriteActivity.this, v);  //建立PopupMenu对象
				popup.getMenuInflater().inflate(R.menu.overflow,   //压入XML资源文件
						popup.getMenu());
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem m) {
						// TODO Auto-generated method stub
						switch (m.getItemId()) {
							case R.id.action_alarm:
								break;
							case R.id.action_share:
								Intent intent=new Intent(Intent.ACTION_SEND);
								intent.setType("text/plain");
								intent.putExtra(Intent.EXTRA_TEXT, "记事本内容："+etContent.getText().toString());
								startActivity(intent);
								break;
							default:
								break;
						}
						return true;
					}
				});    //设置点击菜单选项事件
				popup.show();                                            //显示菜单
			}
		});
	}
}
