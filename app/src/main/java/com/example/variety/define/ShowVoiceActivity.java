package com.example.variety.define;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mynotezxy.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowVoiceActivity extends Activity{

	private String voicePath;
	private int isPlaying = 0;
	private AnimationDrawable ad_left,ad_right;
	private Timer mTimer;
	//语音操作对象
	private MediaPlayer mPlayer = null;
	private ImageView ivVoice_wave_left,ivVoice_wave_right,ivMicrophone;
	private TextView tvTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_voice);
		Intent intent = this.getIntent();
		voicePath = intent.getStringExtra("voicePath");
		ivMicrophone = (ImageView)findViewById(R.id.ivMicrophone);
		ivMicrophone.setOnClickListener(new ClickEvent());

		ivVoice_wave_left = (ImageView)findViewById(R.id.ivVoice_wave_left);
		ivVoice_wave_right = (ImageView)findViewById(R.id.ivVoice_wave_right);

		ad_left = (AnimationDrawable)ivVoice_wave_left.getBackground();
		ad_right = (AnimationDrawable)ivVoice_wave_right.getBackground();
		tvTime = (TextView)findViewById(R.id.tvTime);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.back, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.action_back:
				ShowVoiceActivity.this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	final Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1 :
					String time[] = tvTime.getText().toString().split(":");
					int hour = Integer.parseInt(time[0]);
					int minute = Integer.parseInt(time[1]);
					int second = Integer.parseInt(time[2]);

					if(second < 59){
						second++;

					}
					else if(second == 59 && minute < 59){
						minute++;
						second = 0;

					}
					if(second == 59 && minute == 59 && hour < 98){
						hour++;
						minute = 0;
						second = 0;
					}

					time[0] = hour + "";
					time[1] = minute + "";
					time[2] = second + "";
					//调整格式显示到屏幕上
					if(second < 10)
						time[2] = "0" + second;
					if(minute < 10)
						time[1] = "0" + minute;
					if(hour < 10)
						time[0] = "0" + hour;

					//显示在TextView中
					tvTime.setText(time[0]+":"+time[1]+":"+time[2]);

					break;

			}

		}
	};

	class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//试听
			if(isPlaying == 0){
				isPlaying = 1;
				mPlayer = new MediaPlayer();
				tvTime.setText("00:00:00");
				mTimer = new Timer();
				mPlayer.setOnCompletionListener(new MediaCompletion());
				try {
					mPlayer.setDataSource(voicePath);
					mPlayer.prepare();
					mPlayer.start();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}, 1000,1000);
				//播放动画
				ad_left.start();
				ad_right.start();
			}
			//结束试听
			else{
				isPlaying = 0;
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
				mTimer.cancel();
				mTimer = null;
				//停止动画
				ad_left.stop();
				ad_right.stop();
			}
		}
	}

	class MediaCompletion implements OnCompletionListener{

		@Override
		public void onCompletion(MediaPlayer mp) {
			mTimer.cancel();
			mTimer = null;
			isPlaying = 0;
			//停止动画
			ad_left.stop();
			ad_right.stop();
			Toast.makeText(ShowVoiceActivity.this, "播放完毕", Toast.LENGTH_SHORT).show();
			tvTime.setText("00:00:00");
		}
	}
}
