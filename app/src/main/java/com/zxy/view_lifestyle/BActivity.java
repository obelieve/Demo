package com.zxy.view_lifestyle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zxy.utility.LogUtil;

/**
 * Created by admin on 2019/4/8.
 */

public class BActivity extends Activity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.e();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e();
        Button button = new Button(this);
        button.setText(getClass().getSimpleName()+" taskId:"+getTaskId());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BActivity.this,CActivity.class));
            }
        });
        setContentView(button);
    }
}
