package com.example.variety.define;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.mynotezxy.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class GraffitoView extends View{
	private Canvas canvas;
	private Path path;
	private Bitmap bitmap;
	private Paint bitmapPaint;
	private Paint myPaint;
	private int currentColor=Color.BLACK;
	private int currentSize=5;
	private int bitmapWidth,bitmapHeight;
	float xi,yi;
	//画笔颜色
	private int[] paintColor = {
			getResources().getColor(R.color.black),
			getResources().getColor(R.color.red),
			getResources().getColor(R.color.yellow),
			getResources().getColor(R.color.green),
			getResources().getColor(R.color.blue)
	};
	public GraffitoView(Context context){
		super(context);
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		bitmapWidth=dm.widthPixels;
		bitmapHeight=dm.heightPixels-100;
		initCanvas();
	}
	public GraffitoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		bitmapWidth=dm.widthPixels;
		bitmapHeight=dm.heightPixels-100;
		initCanvas();
	}
	public void initCanvas() {
		bitmap=Bitmap.createBitmap(bitmapWidth, bitmapHeight,Config.ARGB_8888);
		canvas=new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		path=new Path();
		bitmapPaint=new Paint(Paint.DITHER_FLAG);
		setPaintStyle();

	}
	public void setPaintStyle() {
		myPaint=new Paint();
		myPaint.setAntiAlias(true);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeJoin(Paint.Join.ROUND);
		myPaint.setStrokeCap(Paint.Cap.ROUND);
		myPaint.setStrokeWidth(currentSize);
		myPaint.setColor(currentColor);
	}
	//设置画笔的大小
	public void selectHandWritetSize(int which){
		int size =Integer.parseInt(this.getResources().getStringArray(R.array.paint_size)[which]);//把array.xml文件里面的数组拿出来
		currentSize = size;
		setPaintStyle();
	}
	//设置画笔颜色
	public void selectHandWriteColor(int which){
		currentColor = paintColor[which];
		setPaintStyle();
	}
	public void deleteAll() {//清空画布
		initCanvas();
		invalidate();
	}
	public void eraser() {
		currentColor=Color.WHITE;
		setPaintStyle();
	}
	//	public String saveBitmap() {
//		return ;
//	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
		canvas.drawPath(path, myPaint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x=event.getX();
		float y=event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchDown(x,y);
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x,y);
				break;
			case MotionEvent.ACTION_UP:
				touchUP(x,y);
				break;
			default:
				break;
		}
		return true;
	}
	public void touchDown(float x,float y){
		path.reset();
		path.moveTo(x, y);
		xi=x;
		yi=y;
		invalidate();
	}
	public void touchMove(float x,float y){
		float xAbs=Math.abs(x-xi);
		float yAbs=Math.abs(y-yi);
		if(xAbs>=3||yAbs>=3){
			path.quadTo(xi,yi,x,y);
			xi=x;
			yi=y;
		}
		invalidate();
	}
	public void touchUP(float x,float y){
		canvas.drawPath(path, myPaint);
		invalidate();
	}
	public String  saveBitmap() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate=new Date(System.currentTimeMillis());
		String str = dateFormat.format(curDate);
		String graffitoPath="";
		str=str+"graffito.png";
		File dir=new File("/sdcard/MyNotezxy/");
		File file=new File("/sdcard/MyNotezxy/",str);
		if(!dir.exists()){
			dir.mkdir();
		}else {
			if(file.exists()){
				file.delete();
			}
		}
		try {
			graffitoPath="/sdcard/MyNotezxy/"+str;
			FileOutputStream out= new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();//强行输出缓存
			out.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return graffitoPath;

	}
}
