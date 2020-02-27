package com.zxy.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.luozm.captcha.Captcha;
import com.luozm.captcha.DefaultCaptchaStrategy;
import com.zxy.demo.captcha.CharCaptcha;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.et_identify)
    EditText etIdentify;
    @BindView(R.id.iv_identify)
    ImageView ivIdentify;
    @BindView(R.id.captcha)
    Captcha captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ivIdentify.setImageBitmap(CharCaptcha.getInstance().createBitmap(this, 80, 40));
        captcha.setBitmap("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3203825941,2278025487&fm=26&gp=0.jpg");
        captcha.setCaptchaStrategy(new DefaultCaptchaStrategy(this));
        captcha.setSeekBarStyle(R.drawable.po_seekbar,R.drawable.thumb);
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Toast.makeText(MainActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                return "验证通过,耗时"+time+"毫秒";
            }

            @Override
            public String onFailed(int failedCount) {
                Toast.makeText(MainActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                return "验证失败,已失败"+failedCount+"次";
            }
            @Override
            public String onMaxFailed() {
                Toast.makeText(MainActivity.this,"验证超过次数，你的帐号被封锁",Toast.LENGTH_SHORT).show();
                return "验证失败,帐号已封锁";
            }

        });
    }

    @OnClick({R.id.tv, R.id.iv_identify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv:
                String code = CharCaptcha.getInstance().getCode();
                String tips = "错误";
                if (etIdentify.getText().toString().toLowerCase().equals(code.toLowerCase())) {
                    Toast.makeText(this, "正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_identify:
                ivIdentify.setImageBitmap(CharCaptcha.getInstance().createBitmap(this, 80, 40));
                break;
        }
    }
}
