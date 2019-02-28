package com.zxy.demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zxy.utility.FileUtil;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    EditText et_src, et_des, et_file;
    Button btn_shift;
    TextView tv;
    boolean isRun;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_src = findViewById(R.id.et_src);
        et_des = findViewById(R.id.et_des);
        et_file = findViewById(R.id.et_file);
        btn_shift = findViewById(R.id.btn_shift);
        tv = findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        et_src.setText(Environment.getExternalStorageDirectory() + "/Download");
        et_des.setText(Environment.getExternalStorageDirectory() + "/Android/data/com.amazon.kindlefc");
        et_file.setText(".mobi");

        btn_shift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isRun){
                    return;
                }
                final String src = et_src.getText().toString();
                final String des = et_des.getText().toString();
                final String file = et_file.getText().toString();
                tv.setText("拷贝成功：\n");
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        isRun = true;
                        File file1 = new File(src);
                        File file2 = new File(des);
                        for (final File cFile : file1.listFiles())
                        {
                            if(cFile.getName().contains(file)){
                                try
                                {
                                    FileUtil.copyFile(cFile,new File(file2,cFile.getName()));
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            tv.append(cFile.getAbsolutePath()+"\n");
                                        }
                                    });
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        isRun = false;
                    }
                }).start();
            }
        });
    }
}
