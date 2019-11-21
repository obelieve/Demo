package com.example.variety;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mynotezxy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceActivity extends Activity{
	private Button btnVoice;
	private ImageView ivMicrophone,ivVoice_wave_left,ivVoice_wave_right;
	private TextView tvVoiceTime;
	private AnimationDrawable ad_left,ad_right;
	private int isRecording=0;
	private int isPlaying=0;
	private Timer timer;
	//语音
	private MediaPlayer mediaPlayer=null;
	private MediaRecorder mediaRecorder=null;
	private String voicePath=null;//保存路径
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		btnVoice= (Button)findViewById(R.id.btnVoice);
		btnVoice.setOnClickListener(new ClickEvent());
		ivMicrophone = (ImageView)findViewById(R.id.ivMicrophone);
		ivMicrophone.setOnClickListener(new ClickEvent());

		ivVoice_wave_left = (ImageView)findViewById(R.id.ivVoice_wave_left);
		ivVoice_wave_right = (ImageView)findViewById(R.id.ivVoice_wave_right);

		ad_left = (AnimationDrawable)ivVoice_wave_left.getBackground();

		ad_right = (AnimationDrawable)ivVoice_wave_right.getBackground();

		tvVoiceTime = (TextView)findViewById(R.id.tvVoiceTime);
		initActionbar();

	}
	public void initActionbar() {
		View customView =getLayoutInflater().inflate(R.layout.actionbar_third, null);
		ImageView ivBack,ivAccept;
		ivBack=(ImageView)customView.findViewById(R.id.ivBack);
		ivAccept=(ImageView)customView.findViewById(R.id.ivAccept);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(customView);//自定义view替换到actionbar中
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (voicePath!=null) {
					File oldFile=new File(voicePath);
					oldFile.delete();
				}
				VoiceActivity.this.finish();
			}
		});
		ivAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("voicePath",voicePath);
				intent.putExtras(bundle);
				setResult(RESULT_OK,intent);
				VoiceActivity.this.finish();
			}
		});
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			    case 1 :
			    	String time[] = tvVoiceTime.getText().toString().split(":");
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
					tvVoiceTime.setText(time[0]+":"+time[1]+":"+time[2]);

					break;

			}

		}
	};
	class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnVoice:
				if(isRecording==0){
					if(voicePath!=null){
						File oldFlFile=new File(voicePath);
						oldFlFile.delete();
					}
				//获得系统当前时间，并以该时间作为文件名
		  		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
		        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
		        String   str   =   formatter.format(curDate);

		        str = str + "voice.amr";
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

				voicePath = dir.getPath() +"/"+ str;
				//计时器
				timer = new Timer();
				//将麦克图标设置成不可点击，
				ivMicrophone.setClickable(false);
				//将显示的时间设置为00:00:00
				tvVoiceTime.setText("00:00:00");
				//将按钮换成停止录音
				isRecording = 1;
				btnVoice.setBackgroundResource(R.drawable.voice_stop);

				mediaRecorder = new MediaRecorder();
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mediaRecorder.setOutputFile(voicePath);
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

				try {
					mediaRecorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mediaRecorder.start();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				},1000, 1000);
				//播放动画
				ad_left.start();
				ad_right.start();
			}
			//停止录音
			else{
				//将按钮换成开始录音
				isRecording = 0;
				btnVoice.setBackgroundResource(R.drawable.voice_start);
				mediaRecorder.stop();
				timer.cancel();
				timer = null;

				mediaRecorder.release();
				mediaRecorder = null;

				//将麦克图标设置成可点击，
				ivMicrophone.setClickable(true);
				//停止动画
				ad_left.stop();
				ad_right.stop();
				Toast.makeText(VoiceActivity.this, "单击麦克图标试听!", Toast.LENGTH_LONG).show();
			}
			break;
			//如果单击的是麦克图标，则可以是进入试听模式，再次点击，停止播放
			case R.id.ivMicrophone :
				if(voicePath == null)
					Toast.makeText(VoiceActivity.this, "没有录音广播可以播放，请先录音", Toast.LENGTH_LONG).show();
				else{
					//试听
					if(isPlaying == 0){
						isPlaying = 1;
						mediaPlayer = new MediaPlayer();
						tvVoiceTime.setText("00:00:00");
						timer = new Timer();
						mediaPlayer.setOnCompletionListener(new MediaCompletion());//
						try {
							mediaPlayer.setDataSource(voicePath);
							mediaPlayer.prepare();
							mediaPlayer.start();
							btnVoice.setClickable(false);
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
						timer.schedule(new TimerTask() {

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
						mediaPlayer.stop();
						mediaPlayer.release();
						mediaPlayer = null;
						timer.cancel();
						timer = null;
						//停止动画
						ad_left.stop();
						ad_right.stop();
					}
				}
				break;
			default:
				break;
			}

		}

	}
	class MediaCompletion implements OnCompletionListener{

		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			timer.cancel();
			timer=null;
			isPlaying=0;
			ad_left.stop();
			ad_right.stop();
			btnVoice.setClickable(true);
			Toast.makeText(VoiceActivity.this, "播放完毕！",Toast.LENGTH_SHORT).show();
			tvVoiceTime.setText("00:00:00");
		}

	}

}
