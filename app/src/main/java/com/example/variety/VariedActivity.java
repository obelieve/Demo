package com.example.variety;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.mynotezxy.R;
import com.example.variety.define.DatabaseOperation;
import com.example.variety.define.ShowPictureActivity;
import com.example.variety.define.ShowVoiceActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class VariedActivity extends Activity{
	GridView gvBottomMenu;
	EditText etContent;
	ImageView ivBack,ivAccept,ivOVerflow;
	int screen_width,screen_height;
	int [] gvItems={//GridView里面的 图片项
			R.drawable.varied_pencil,
			R.drawable.varied_graffito,
			R.drawable.varied_album,
			R.drawable.varied_camera,
			R.drawable.varied_vioce
	};
	InputMethodManager imm;//虚拟键盘管理,在文本框点击事件有处理
	SQLiteDatabase db;
	DatabaseOperation dOperation;//自定义数据库操作类
	String backIntent;//把Activity  intent返回值进行操作
	int item_Id;//获取HomeFragment传来的值,panduan id
	List<Map<String,String>> imgList = new ArrayList<Map<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_varied);
		etContent=(EditText)findViewById(R.id.etContent);
		etContent.setOnClickListener(new TextClickEvent());
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);//获取
		imm.hideSoftInputFromWindow(etContent.getWindowToken(),0);//隐藏虚拟键盘
		DisplayMetrics dm=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_height=dm.heightPixels;
		screen_width=dm.widthPixels;
		dOperation=new DatabaseOperation(this, db);
		initActionbar();
		initBottomMenu();
		processIntent();//处理主屏幕+按钮返回的值来，触发点击事件跳到相应的Activity
		loadData();//加载
	}

	public void initActionbar() {
		View customView =getLayoutInflater().inflate(R.layout.actionbar_second, null);
		ivBack=(ImageView)customView.findViewById(R.id.ivBack);
		ivAccept=(ImageView)customView.findViewById(R.id.ivAccept);
		ivOVerflow=(ImageView)customView.findViewById(R.id.ivOverflow);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(customView);//自定义view替换到actionbar中
		//getActionBar().setDisplayShowCustomEnabled(true);//使自定义的普通View能在title栏显示
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				VariedActivity.this.finish();
			}
		});
		ivAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String context =etContent.getText().toString();
				if(context.isEmpty()){
					Toast.makeText(VariedActivity.this,"记事内容为空!", Toast.LENGTH_LONG).show();
				}else {
					SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH-mm");
					Date curDate =new Date(System.currentTimeMillis());
					String time =dateFormat.format(curDate);
					String title=getTitle(context);
//					Log.i("qwq", "h:"+backIntent.equals("handwrite")+backIntent.equals("graffito")+
//							backIntent.equals("camera")	+backIntent.equals("voice")	);
					dOperation.create_db();
					if(backIntent.equals("handwrite")||backIntent.equals("graffito")||backIntent.equals("camera")||backIntent.equals("voice")){
						dOperation.insert_db(title, context, time);
					}else if (backIntent.equals("update")) {
						dOperation.update_db(title, context, time, item_Id);
					}
					dOperation.close_db();
					VariedActivity.this.finish();
				}

			}
		});
		ivOVerflow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//点击3个点按钮时会出现下拉菜单 分享 和添加提醒按钮
				// TODO Auto-generated method stub
				PopupMenu popup =new PopupMenu(VariedActivity.this, v);  //建立PopupMenu对象
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
								File file = new File(saveSendJpg());
								Log.i("q", ""+saveSendJpg()+file);
								Uri uri=Uri.fromFile(file);
								intent.setType("image/*");
								intent.putExtra(Intent.EXTRA_STREAM, uri);
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
		SimpleAdapter mAdapter = new SimpleAdapter(VariedActivity.this, menus,R.layout.bottommenu_item_button, new String[]{"image"}, new int[]{R.id.ivItem});
		gvBottomMenu.setAdapter(mAdapter);
	}
	public void processIntent(){
		Intent intent=getIntent();
		backIntent=intent.getStringExtra("back");
		item_Id = intent.getIntExtra("noteId", 0);//获取HomeFragment传来的值
		int backIntent_Int=0;
		if(backIntent.equals("handwrite")){
			backIntent_Int=1;
		}else {
			if (backIntent.equals("graffito")){
				backIntent_Int=2;
			}else if (backIntent.equals("camera")) {
				backIntent_Int=3;
			}else if(backIntent.equals("voice")){
				backIntent_Int=4;
			}
		}
		switch (backIntent_Int) {
			case 1://手写
				intent = new Intent(VariedActivity.this,HandWriteActivity.class);
				startActivityForResult(intent, 1);
				break;
			case 2://涂鸦
				intent = new Intent(VariedActivity.this,GraffitoActivity.class);
				startActivityForResult(intent, 2);
				break;
			case 3://拍照
				//调用系统拍照界面
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				//区分选择相片
				startActivityForResult(intent, 4);
				break;
			case 4://语音
				intent = new Intent(VariedActivity.this,VoiceActivity.class);
				startActivityForResult(intent, 5);
				break;
			default:
				break;
		}
	}
	private void loadData() {//加载数据
		if(backIntent.equals("handwrite")||backIntent.equals("graffito")||backIntent.equals("camera")||backIntent.equals("voice")){
			etContent.setText("");
		}else if (backIntent.equals("update")) {
			dOperation.create_db();
			Cursor cursor=dOperation.query_db(item_Id);
			cursor.moveToFirst();
			String context =cursor.getString(cursor.getColumnIndex("context"));
			Pattern p=Pattern.compile("/([^\\.]*)\\.\\w{3}");
			Matcher m=p.matcher(context);
			int startIndex=0;
			while(m.find()){
				//取出路径前的文字
				if(m.start() > 0){
					etContent.append(context.substring(startIndex, m.start()));
				}

				SpannableString ss = new SpannableString(m.group().toString());

				//取出路径
				String path = m.group().toString();
				//取出路径的后缀
				String type = path.substring(path.length() - 3, path.length());
				Bitmap bm = null;
				Bitmap rbm = null;
				//判断附件的类型，如果是录音文件，则从资源文件中加载图片
				if(type.equals("amr")){
					bm = BitmapFactory.decodeResource(getResources(), R.drawable.voice_icon);
					//缩放图片
					rbm = resize(bm);
				}
				else{
					//取出图片
					bm = BitmapFactory.decodeFile(m.group());
					//缩放图片
					rbm = resize(bm);
				}
				ImageSpan span = new ImageSpan(this, rbm);
				ss.setSpan(span,0, m.end() - m.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				etContent.append(ss);
				startIndex = m.end();
				//用List记录该录音的位置及所在路径，用于单击事件
				Map<String,String> map = new HashMap<String,String>();
				map.put("location", m.start()+"-"+m.end());
				map.put("path", path);
				imgList.add(map);
			}
			etContent.append(context.substring(startIndex,context.length()));
			dOperation.close_db();
		}

	}
	private String getTitle(String context){//截取EditText的前面内容做标题
		Pattern p=Pattern.compile("/([^\\.]*)\\.\\w{3}");//正则表达式
		Matcher m=p.matcher(context);
		StringBuffer stringBuffer=new StringBuffer();
		String title="";
		int startIndex=0;
		while(m.find()){
			if(m.start()>0){
				stringBuffer.append(context.substring(startIndex,m.start()));
			}
			//取路径
			String path=m.group().toString();
			String type=path.substring(path.length()-3,path.length());//取后缀
			if(type.equals("amr")){
				stringBuffer.append("[录音]");
			}else {
				stringBuffer.append("[图片]");
			}
			startIndex=m.end();
			if (stringBuffer.length()>10) {//取标题
				title=stringBuffer.toString().replaceAll("\r|n|\t", " ");
				return title;
			}
		}
		stringBuffer.append(context.substring(startIndex,context.length()));
		title = stringBuffer.toString().replaceAll("\r|\n|\t", " ");
		return title;
	}
	void InsertBitmap(Bitmap bitmap,String path){
		bitmap=resize(bitmap);
		ImageSpan imageSpan=new ImageSpan(this,bitmap);
		SpannableString spannableString=new SpannableString(path);
		spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
		Editable editable =etContent.getEditableText();
		int selectionIndex =etContent.getSelectionStart();
		spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
		editable.insert(selectionIndex, spannableString);
		etContent.append("\n");//空行
//		//用List记录该录音的位置及所在路径，用于单击事件
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("location", selectionIndex+"-"+(selectionIndex+spannableString.length()));
//        map.put("path", imgPath);
//        imgList.add(map);
	}
	public Bitmap resize(Bitmap bitmap) {//缩放图片
		int imgWidth=bitmap.getWidth();
		int imgHeight=bitmap.getHeight();
		float scaleW,scaleH;
		Matrix mx=new Matrix();
		if(imgWidth>screen_width||imgHeight>screen_height){
			scaleW=(screen_width-72)/(float)imgWidth;
			scaleH=scaleW;
		}else if ((imgWidth<300)&&(imgHeight<300)) {
			scaleH=1;
			scaleW=1;
		}else {
			scaleW=(imgWidth-48)/(float)imgWidth;
			scaleH=(imgHeight-72)/(float)imgHeight;
		}
		mx.postScale(scaleW, scaleH);
		bitmap=Bitmap.createBitmap(bitmap, 0, 0, imgWidth,imgHeight,mx,true);
		return bitmap;
	}
	class TextClickEvent implements OnClickListener{//EditText设定点击事件监听器

		@Override
		public void onClick(View v) {
			Spanned s = etContent.getText();
			ImageSpan[] imageSpans;
			imageSpans = s.getSpans(0, s.length(), ImageSpan.class);

			int selectionStart = etContent.getSelectionStart();
			for(ImageSpan span : imageSpans){

				int start = s.getSpanStart(span);
				int end = s.getSpanEnd(span);
				//找到图片
				if(selectionStart >= start && selectionStart < end){
					//Bitmap bitmap = ((BitmapDrawable)span.getDrawable()).getBitmap();
					//查找当前单击的图片是哪一个图片
					//System.out.println(start+"-----------"+end);
					String path=null;
					for(int i = 0;i < imgList.size();i++){
						Map map = imgList.get(i);
						//找到了
						if(map.get("location").equals(start+"-"+end)){
							path = imgList.get(i).get("path");
							break;
						}
					}
					//Log.i("aa", path+"+"+etContent.getText());
					//接着判断当前图片是否是录音，如果为录音，则跳转到试听录音的Activity，如果不是，则跳转到查看图片的界面
					//录音，则跳转到试听录音的Activity
					if(path.substring(path.length()-3, path.length()).equals("amr")){
						Intent intent = new Intent(VariedActivity.this,ShowVoiceActivity.class);
						intent.putExtra("voicePath", path);
						startActivity(intent);
					}
					//图片，则跳转到查看图片的界面
					else{
						//有两种方法，查看图片，第一种就是直接调用系统的图库查看图片，第二种是自定义Activity
						//调用系统图库查看图片
						/*Intent intent = new Intent(Intent.ACTION_VIEW);
						File file = new File(path);
						Uri uri = Uri.fromFile(file);
						intent.setDataAndType(uri, "image/*");*/
						//使用自定义Activity
						Intent intent = new Intent(VariedActivity.this,ShowPictureActivity.class);
						intent.putExtra("imgPath", path);
						startActivity(intent);
					}
				}else {
					imm.showSoftInput(etContent, 0);//显示虚拟键盘
				}
			}
		}
	}
	class BottomMenuClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
								long id) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (position) {
				case 0://手写
					intent = new Intent(VariedActivity.this,HandWriteActivity.class);
					startActivityForResult(intent, 1);
					break;
				case 1://涂鸦
					intent = new Intent(VariedActivity.this,GraffitoActivity.class);
					startActivityForResult(intent, 2);
					break;
				case 2://相册
					//添加图片的主要代码
					intent = new Intent();
					//设定类型为image
					intent.setType("image/*");
					//设置action
					intent.setAction(Intent.ACTION_GET_CONTENT);
					//选中相片后返回本Activity
					startActivityForResult(intent, 3);
					break;
				case 3://拍照
					//调用系统拍照界面
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					//区分选择相片
					startActivityForResult(intent, 4);
					break;
				case 4://录音
					intent = new Intent(VariedActivity.this,VoiceActivity.class);
					startActivityForResult(intent, 5);
					break;
				default:
					break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK){
			Uri uri=data.getData();
			ContentResolver cr=VariedActivity.this.getContentResolver();
			Bitmap bitmap=null;
			Bundle extras=null;
			switch (requestCode) {
				case 1://手写
				{
					extras=data.getExtras();
					String path =extras.getString("handWritePath");
					bitmap=BitmapFactory.decodeFile(path);
					InsertBitmap(bitmap, path);
					break;
				}
				case 2://涂鸦
				{
					extras =data.getExtras();
					String path=extras.getString("graffitoPath");
					bitmap=BitmapFactory.decodeFile(path);
					InsertBitmap(bitmap, path);
					break;
				}
				case 3://相册
				{
					//取得选择照片的路径
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
					int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String path = actualimagecursor.getString(actual_image_column_index);
					try {
						//将对象存入Bitmap中
						bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//插入图片
					InsertBitmap(bitmap,path);
					break;
				}
				case 4://拍照
				{
					try {
						if(uri != null)//这个方法是根据Uri获取Bitmap图片的静态方法
							bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
							//这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						else{
							extras = data.getExtras();
							bitmap = extras.getParcelable("data");
						}
						//将拍的照片存入指定的文件夹下
						//获得系统当前时间，并以该时间作为文件名
						SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
						Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
						String   str   =   formatter.format(curDate);
						String cameraPath = "";
						str = str + "camera.png";
						File dir = new File("/sdcard/MyNotezxy/");
						File file = new File("/sdcard/MyNotezxy/",str);
						if (!dir.exists()) {
							dir.mkdir();
						}
						else{
							if(file.exists()){
								file.delete();
							}
						}
						FileOutputStream fos = new FileOutputStream(file);
						// 将 bitmap 压缩成其他格式的图片数据
						bitmap.compress(CompressFormat.PNG, 100, fos);
						fos.flush();
						fos.close();
						String path = "/sdcard/MyNotezxy/" + str;
						//插入图片
						InsertBitmap(bitmap,path);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case 5://语音
				{
					extras=data.getExtras();
					String path =extras.getString("voicePath");
					bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.voice_icon);
					InsertBitmap(bitmap, path);
					break;
				}
				default:
					break;
			}

		}
	}
	private String saveSendJpg(){//保存分享图片,提供分享功能Uri
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");//时间格式
		Date curDate =new Date(System.currentTimeMillis());//获取当前时间
		String str=dateFormat.format(curDate);
		String path="";
		str =str+"send.png";
		File file =new File("/sdcard/MyNotezxy/",str);
		if(file.exists()){
			file.delete();
		}
		etContent.setDrawingCacheEnabled(true);//开启cache缓存
		Bitmap bitmap =Bitmap.createBitmap(etContent.getDrawingCache());//保存
		etContent.setDrawingCacheEnabled(false);//销毁cache缓存
		try{
			path="/sdcard/MyNotezxy/"+str;//保存图片路径
			FileOutputStream out=new FileOutputStream(file);//
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);//压缩图片
			out.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return path;
	}
}
