package com.example.variety.define;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mynotezxy.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View{
	private Handler bitmapHandler;
	public void  setBitmapHandle(Handler handler) {
		this.bitmapHandler=handler;
	}
	private DisplayMetrics dm;
	private Bitmap canvasBitmap;//位图
	private Bitmap backBitmap;//存放最后位图，传回给Activity
	private Canvas canvas;//画布
	private Paint canvasBitmapPaint;//位图画笔
	private Paint paint;//手写字的画笔
	private Path path;
	private float xi,yi;//保存TouchEven事件的当前坐标
	private DrawArea drawArea;//保存绘制区域的坐标
	private Timer timer;//设置定时器
	private int currentColor = Color.BLACK;
	private int currentSize = 5;
	//画笔颜色
	private int[] paintColor = {
			getResources().getColor(R.color.black),
			getResources().getColor(R.color.red),
			getResources().getColor(R.color.yellow),
			getResources().getColor(R.color.green),
			getResources().getColor(R.color.blue)
	};
	public TouchView(Context context){
		super(context);
		dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		init(dm.widthPixels, dm.heightPixels);
	}
	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		init(dm.widthPixels, dm.heightPixels);
	}
	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		init(dm.widthPixels, dm.heightPixels);
	}
	public void init(int w,int h){//初始化函数
		//Log.i("初始化", "初始化");
		canvasBitmap=Bitmap.createBitmap(w, h, Config.ARGB_8888);//画布大小
		canvas=new Canvas(canvasBitmap);
		canvasBitmapPaint=new Paint(Paint.DITHER_FLAG);//Paint.DITHER_FLAG防抖动
		path = new Path();
		drawArea=new DrawArea();
		timer=new Timer();
		setPaintStyle();//设置画笔样式
	}
	public void setPaintStyle() {
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);//Paint.Style.STROKE中空属性
		paint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
		paint.setStrokeCap(Paint.Cap.ROUND);//笔刷类型
		paint.setStrokeWidth(currentSize);// 画笔宽度
		paint.setColor(currentColor);
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
	Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what){
				case 1:
					Log.i("msg", "to msg1");
					backBitmap=cutBitmap(canvasBitmap);
					Message message=new Message();
					message.what=1;
					Bundle bundle=new Bundle();
					bundle.putParcelable("bitmap",backBitmap);//用bundle保存myBitmap
					message.setData(bundle);
					bitmapHandler.sendMessage(message);//传消息到Activity处理
					reFreshCanvas();//刷新画布
					break;
			}
			super.handleMessage(msg);
		}
	};
	TimerTask task=new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message=new Message();
			message.what=1;
			handler.sendMessage(message);//
		}
	};
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);
		canvas.drawPath(path, paint);//画时显示画的轨迹
		super.onDraw(canvas);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				touchDown(x,y);
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x,y);
				break;
			case MotionEvent.ACTION_UP:
				touchUp(x,y);
				break;
		}
		return true;
	}
	public void touchDown(float x,float y){
		path.reset();
		path.moveTo(x, y);
		if(task!=null){
			task.cancel();
			task=new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message=new Message();
					message.what=1;
					handler.sendMessage(message);
				}
			};
		}
		drawArea.setXY(x, y);
		xi=x;//
		yi=y;//当前位置坐标
		invalidate();//刷新,调用invalidate()才能看到重新绘制的界面
	}
	public void touchMove(float x,float y){
		float xm=x;
		float ym=y;
		if(task!=null){
			task.cancel();
			task=new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message=new Message();
					message.what=1;
					handler.sendMessage(message);
				}
			};
		}
		drawArea.setXY(xm, ym);
		float xAbs=Math.abs(xm-xi);//返回参数绝对值
		float yAbs=Math.abs(ym-yi);
		if(xAbs>=3||yAbs>=3){//两点之间的距离大于等于3时，生成贝塞尔绘制曲线
			path.quadTo(xi, yi, xm, ym);//连接到下一点,二次贝塞尔，实现平滑曲线；xi, yi为操作点，xm, ym为终点
			xi=xm;
			yi=ym;
		}
		invalidate();//刷新
	}
	public void touchUp(float x,float y){
		drawArea.setXY(x, y);
		canvas.drawPath(path, paint);//这个drawPath是为了TouchUp后显示最后画的然后截取为Bitmap
		if(timer!=null){
			if(task!=null){
				task.cancel();
				task=new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message=new Message();
						message.what=1;
						handler.sendMessage(message);
					}
				};
			}
			timer.schedule(task, 500, 500);//task里面有handler.sendMessage(message);
			//0.5秒后发送消息给handler更新Activity
		}else {
			timer=new Timer();
			timer.schedule(task, 500, 500);
		}
		invalidate();//刷新
	}

	public Bitmap cutBitmap(Bitmap bitmap) {//剪切图片
		float cutLeft=drawArea.getMinx()-10;
		float cutTop=drawArea.getMinY()-10;
		float cutRight=drawArea.getMaxX()+10;
		float cutBottom=drawArea.getMaxY()+10;
		cutLeft = (0 > cutLeft ? 0 : cutLeft);
		cutTop = (0 > cutTop ? 0 : cutTop);
		cutRight=(cutRight >bitmap.getWidth() ? bitmap.getWidth():cutRight);
		cutBottom=(cutBottom >bitmap.getHeight() ? bitmap.getHeight():cutBottom);
		int cutWidth =(int)(cutRight-cutLeft);
		int cutHeight =(int)(cutBottom-cutTop);
		Bitmap cBitmap ;
		cBitmap =Bitmap.createBitmap(bitmap,(int)cutLeft,(int)cutTop,cutWidth,cutHeight);
		if (backBitmap!=null ) {
			backBitmap.recycle();//回收Bitmap
			backBitmap= null;
		}
		return cBitmap;
	}
	public void reFreshCanvas(){//刷新画布
		init(dm.widthPixels,dm.heightPixels);
		if (task!=null) {
			task.cancel();
		}
		invalidate();
	}
}
