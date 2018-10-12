package com.zxy.demo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

public class MainActivity extends Activity
{

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtil.e();
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn);
        HeadLinesView v = (HeadLinesView)findViewById(R.id.view);
        v.setAdapter(new HeadAdapter());
        v.start();
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(MainActivity.this,R.style.dialog_theme);
                LinearLayout linearLayout = new LinearLayout(v.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.BOTTOM);
                linearLayout.setBackgroundColor(Color.parseColor("#00000000"));
                linearLayout.setPadding(30, 30, 30, 30);
                dialog.getWindow().setWindowAnimations(R.style.anim);
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog, null);
                TextView tv_cancel,tv_certain;
                LinearLayout ll_dismiss = (LinearLayout)view.findViewById(R.id.ll_dismiss);
                ll_dismiss.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                tv_cancel = (TextView)view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                tv_certain = (TextView)view.findViewById(R.id.tv_certain);
                tv_certain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                //view.setBackground(getDrawable());
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.addView(view);
                dialog.setContentView(linearLayout, new ViewGroup.LayoutParams(screenWidth(),screenHeight()-76));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    public int screenHeight()
    {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;

    }

    public int screenWidth()
    {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GradientDrawable getDrawable()
    {
        GradientDrawable drawable = (GradientDrawable) getDrawable(R.drawable.draw1);
        drawable.mutate();
        int i = 30;
        drawable.setCornerRadii(new float[]{i,i,i,i,i,i,i,i});
//        drawable.setColor(Color.WHITE);
        return drawable;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        LogUtil.e();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LogUtil.e();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LogUtil.e();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        LogUtil.e();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.e();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        LogUtil.e();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        LogUtil.e();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LogUtil.e();
    }
}
