package com.zxy.view_lifestyle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

/**
 * Created by zxy on 2018/10/20 10:17.
 */

public class LifeStyleViewActivity extends Activity implements View.OnClickListener
{

    Button btn_invalidate, btn_requestLayout;
    LifeStyleView lsView;
    LifeStyleViewGroup lsVP;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        LogUtil.e();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lifestyle);
        btn_invalidate = (Button) findViewById(R.id.btn_invalidate);
        btn_requestLayout = (Button) findViewById(R.id.btn_requestLayout);
        lsView = (LifeStyleView) findViewById(R.id.v_lifeStyle);
        lsVP = (LifeStyleViewGroup) findViewById(R.id.vp_lifeStyle);
        setOnClickListeners(btn_invalidate, btn_requestLayout);

    }

    public void setOnClickListeners(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_invalidate:
                lsView.invalidate();
                break;
            case R.id.btn_requestLayout:
                lsView.requestLayout();
                break;
        }
    }
}
