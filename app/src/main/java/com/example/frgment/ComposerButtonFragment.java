package com.example.frgment;

import com.example.mynotezxy.R;
import com.example.variety.GraffitoActivity;
import com.example.variety.HandWriteActivity;
import com.example.variety.VariedActivity;
import com.example.variety.VoiceActivity;
import com.example.variety.WriteActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

@SuppressLint("NewApi")
public class ComposerButtonFragment extends Fragment{
	View rootView;
	Button btn_composer,btn_write,btn_handwrite,btn_graffito,btn_camera,btn_voice;
	int screen_width,screen_height;
	Animation rotateAnimation,translateAnimation,scaleAnimation,alphaAnimation;
	AnimationSet animationSet;
	boolean isPopup;//是否弹出(组合器)按钮
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView=inflater.inflate(R.layout.composerbutton, container,false);
		return rootView;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}
	BtnOnclick listener=new BtnOnclick();
	int composeX[],composeY[];//存储每一个按钮的坐标
	//    int getY(float degress, float r) { //参数：角度,半径
//        return (int)(r * Math.sin(Math.PI*(degress/180f)));
//    }
//    int getX(float degress, float r) {
//        return (int)(r * Math.cos(Math.PI*(degress/180f)));//Math.PI*(degress/180f算出该角度的弧度,360度弧度2 pi(pi:3.1415926)
//    }  圆弧式样
	void init(){
		isPopup=false;
		DisplayMetrics dm=new DisplayMetrics();
		Context context=getActivity();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_height=dm.heightPixels;
		screen_width=dm.widthPixels;
		composeX=new int[5];
		composeY=new int[5];
		for (int i = 0; i < 5; i++) {
//            composeX[i] = getX(90-i*18,screen_width/2);
//            composeY[i] = getY(90-i*18,screen_width/2);圆弧式样
			composeX[i] =(screen_width/6)*(i+1);
			composeY[i]=0;
			//Log.i("x,yzuobiao","x:"+composeX[i]+"y:"+composeY[i]);
		}
		/***以上主要存储每个按钮的坐标*/
		btn_composer=(Button)rootView.findViewById(R.id.btn_composer);
		btn_camera=(Button)rootView.findViewById(R.id.btn_composer_camera);
		btn_write=(Button)rootView.findViewById(R.id.btn_composer_write);
		btn_voice=(Button)rootView.findViewById(R.id.btn_composer_voice);
		btn_handwrite=(Button)rootView.findViewById(R.id.btn_composer_handwrite);
		btn_graffito=(Button)rootView.findViewById(R.id.btn_composer_graffito);
		btn_composer.setOnClickListener(listener);
		btn_camera.setOnClickListener(listener);
		btn_write.setOnClickListener(listener);
		btn_voice.setOnClickListener(listener);
		btn_handwrite.setOnClickListener(listener);
		btn_graffito.setOnClickListener(listener);
		setOnclick(false);
		initAnimation();
	}
	void initAnimation(){
		animationSet=new AnimationSet(true);
		animationSet.addAnimation(getAlphaAnimation());
		animationSet.addAnimation(getScaleAnimation());
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub、
				closeButton();
			}
		});
	}
	void setOnclick(boolean isSet){
		btn_camera.setClickable(isSet);
		btn_write.setClickable(isSet);
		btn_voice.setClickable(isSet);
		btn_handwrite.setClickable(isSet);
		btn_graffito.setClickable(isSet);
	}
	void openButton(){
		isPopup=true;
		btn_composer.startAnimation(getRotateAnimation(0f, -225f));
		btn_voice.startAnimation(getTranslateAnimation(-composeX[0], -composeY[0],btn_voice,composeX[0],composeY[0],160));
		//Log.i("x,yzuobiao","x:"+composeX[0]+"y:"+composeY[0]);
		btn_camera.startAnimation(getTranslateAnimation(-composeX[1], -composeY[1],btn_camera,composeX[1],composeY[1],160));
		btn_graffito.startAnimation(getTranslateAnimation(-composeX[2], -composeY[2],btn_graffito ,composeX[2],composeY[2],160));
		btn_handwrite.startAnimation(getTranslateAnimation(-composeX[3], -composeY[3],btn_handwrite , composeX[3],composeY[3],160));
		btn_write.startAnimation(getTranslateAnimation(-composeX[4], -composeY[4], btn_write, composeX[4],composeY[4],160));
		setOnclick(true);
	}
	void closeButton(){
		isPopup=false;
		btn_composer.startAnimation(getRotateAnimation(-225f, 0f));
		btn_voice.startAnimation(getTranslateAnimation(composeX[0], composeY[0],btn_voice,-composeX[0],-composeY[0],160));
		btn_camera.startAnimation(getTranslateAnimation(composeX[1], composeY[1],btn_camera,-composeX[1],-composeY[1],160));
		btn_graffito.startAnimation(getTranslateAnimation(composeX[2], composeY[2],btn_graffito ,-composeX[2],-composeY[2],160));
		btn_handwrite.startAnimation(getTranslateAnimation(composeX[3], composeY[3], btn_handwrite, -composeX[3],-composeY[3],160));
		btn_write.startAnimation(getTranslateAnimation(composeX[4], composeY[4], btn_write, -composeX[4],-composeY[4],160));
		setOnclick(false);
	}
	class BtnOnclick implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (v.getId()) {
				case R.id.btn_composer:
					if (isPopup) {
						closeButton();//收回效果
					} else {
						//弹出效果
						openButton();
					}
					break;
				case R.id.btn_composer_write:
					btn_write.startAnimation(animationSet);
					intent=new Intent(getActivity(),WriteActivity.class);
					startActivity(intent);
					break;
				case R.id.btn_composer_handwrite:
					intent=new Intent(getActivity(),VariedActivity.class);
					intent.putExtra("back", "handwrite");
					startActivity(intent);
					closeButton();//收回效果
					break;
				case R.id.btn_composer_graffito :
					intent=new Intent(getActivity(),VariedActivity.class);
					intent.putExtra("back", "graffito");
					startActivity(intent);
					closeButton();//收回效果
					break;
				case R.id.btn_composer_camera:
					btn_camera.startAnimation(animationSet);
					intent=new Intent(getActivity(),VariedActivity.class);
					intent.putExtra("back", "camera");
					startActivity(intent);
					closeButton();//收回效果
					break;
				case R.id.btn_composer_voice:
					btn_voice.startAnimation(animationSet);
					intent=new Intent(getActivity(),VariedActivity.class);
					intent.putExtra("back", "voice");
					startActivity(intent);
					closeButton();//收回效果
					break;
				default:
					break;
			}
		}
	}
	public Animation getRotateAnimation(float fromDegrees,float toDegrees) {
		rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(300);
		rotateAnimation.setFillAfter(true);
		return rotateAnimation;
	}
	public Animation getTranslateAnimation(float toXDelta, float toYDelta,final View view, final int marginRight, final int marginBottom, long time){
		translateAnimation=new TranslateAnimation(0, toXDelta, 0, toYDelta);
		translateAnimation.setDuration(time);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				MarginLayoutParams params=(MarginLayoutParams)view.getLayoutParams();
				params.width=70;
				params.height=70;
				//System.out.println(params.rightMargin + " " + params.bottomMargin);
				params.setMargins(0, 0, params.rightMargin+marginRight, params.bottomMargin+marginBottom);
				//System.out.println(params.rightMargin+"mr:"+marginRight + "  " + params.bottomMargin+"mb:"+marginBottom);
				view.setLayoutParams(params);
				view.clearAnimation();
			}
		}, translateAnimation.getDuration());
//		translateAnimation.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void onAnimationEnd(Animation animation) {//如果该动画还没执行完，又重新执行一个新的动画的话，此方法可能不被调用
//				// TODO Auto-generated method stub
//				MarginLayoutParams params=(MarginLayoutParams)view.getLayoutParams();
//				params.width=70;
//				params.height=70;
//		        System.out.println(params.rightMargin + " " + params.bottomMargin);
//				//params.setMargins(0, 0, params.rightMargin+marginRight, params.bottomMargin+marginBottom);
//		        params.setMargins(0, 0, params.rightMargin+marginRight, params.bottomMargin+marginBottom);
//		        System.out.println(params.rightMargin+"mr:"+marginRight + "  " + params.bottomMargin+"mb:"+marginBottom);
//				view.setLayoutParams(params);
//				view.clearAnimation();
//			}
//		});
		return translateAnimation;
	}
	public Animation getScaleAnimation() {
		scaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f);
		scaleAnimation.setDuration(500);
		return scaleAnimation;
	}
	public Animation getAlphaAnimation(){
		alphaAnimation = new AlphaAnimation(1f, 0.3f);
		alphaAnimation.setDuration(500);
		return alphaAnimation;
	}
	@Override
	public void onPause() {//Activity展厅时就应该收回按钮
		// TODO Auto-generated method stub
		super.onPause();
		if(isPopup==true){
			closeButton();
		}
	}
}
