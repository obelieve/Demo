package com.zxy.demo;

import android.animation.ObjectAnimator;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

public class MainActivity extends Activity
{
    /**
     * 商品大小分类2个List切换
     * -----------
     * 1.curL,curR
     * 2.Left 选中状态
     * 3.获取右边数据，进行右边显示更新
     * 4.更新标题栏（右边0，显示左边的title，右边>0显示右边的title）
     * 5.更新右边选中状态
     * 6.设置setSelection
     * ----------------
     * SDViewNavigatorManager
     * 1.使用2个状态值，保存上一次和当前选中项；
     * 2.onClick进行状态变更显示；
     */
    ScrollView sv_content;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DeviceInfoUtil.init(this);
        LogUtil.e();
        setContentView(R.layout.activity_main);
        final ImageView iv_dismiss = (ImageView) findViewById(R.id.iv_dismiss);
        Button btn = (Button) findViewById(R.id.btn);
        final Button nav = (Button) findViewById(R.id.nav);
        Button btn_dismiss = (Button) findViewById(R.id.btn_dismiss);
        btn_dismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                float i = iv_dismiss.getTranslationY();
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv_dismiss, "translationY", i - 200f, i, i - 150f, i, i - 100f, i, i - 50f, i);
                animator.setDuration(2000);
                animator.start();

            }
        });
        final Button top = (Button) findViewById(R.id.top);
        top.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sv_content.smoothScrollTo(0, 0);
            }
        });
        HeadLinesView v = (HeadLinesView)findViewById(R.id.view);
        sv_content = (ScrollView) findViewById(R.id.sv_content);
        sv_content.setOnScrollChangeListener(new View.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                float alpha = scrollY / 400.0f;
                if (scrollY > 10)
                {
                    if (alpha <= 0.7)
                    {
                        LogUtil.e("TAGG", "alpha:" + alpha + " scrollY:" + scrollY);
                        nav.setAlpha(alpha);
                    }
                    nav.setBackgroundColor(Color.parseColor("#ff55aa"));
                } else
                {
                    nav.setAlpha(0.3f);
                    nav.setBackgroundColor(Color.parseColor("#666666"));
                }
                if (scrollY >= 400)
                {
                    top.setVisibility(View.VISIBLE);
                } else
                {
                    top.setVisibility(View.GONE);
                }
            }
        });
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
        initViewPager();
    }

    public void initViewPager()
    {
        FViewPager fvp = (FViewPager) findViewById(R.id.fvp);
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
