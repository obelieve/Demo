package com.zxy.demo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.obelieve.frame.base.ApiBaseActivity2;
import com.obelieve.frame.utils.ToastUtil;
import com.zxy.demo.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends ApiBaseActivity2<ActivityMainBinding> {
    ExecutorService mExecutorService = Executors.newCachedThreadPool();
    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mViewBinding.tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        mViewBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpReq();
            }
        });
    }

    private void httpReq() {
        App.getServiceInterface().get("名字","随机:"+new Random().nextBoolean())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            String s = response.body().string();
                            mViewBinding.tv.setText(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ToastUtil.show(t.getMessage());
                    }
                });
    }

    private void socketReq() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.2.102",1090);
                    socket.setSoTimeout(10000);
                    OutputStream outputStream = socket.getOutputStream();
                    boolean autoflush = true;
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),autoflush);
                    out.println("GET /index.html HTTP/1.1");
                    out.println("Host: 192.168.2.102:1090");
                    out.println("Connection: Close");
                    out.println();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line=reader.readLine())!=null){
                        sb.append(line);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewBinding.tv.append("响应内容："+sb.toString()+"\n");
                        }
                    });
                    System.out.println("响应内容："+sb.toString());
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
