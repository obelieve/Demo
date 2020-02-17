package com.zxy.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zxy.utility.SystemUtil;

import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ivIdentify.setImageBitmap(IdentifyingCode.getInstance().createBitmap(this,80,40));
    }

    @OnClick({R.id.tv,R.id.iv_identify})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv:
                String code = IdentifyingCode.getInstance().getCode();
                String tips = "错误";
                if(etIdentify.getText().toString().toLowerCase().equals(code.toLowerCase())){
                    Toast.makeText(this,"正确",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,tips,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_identify:
                ivIdentify.setImageBitmap(IdentifyingCode.getInstance().createBitmap(this,80,40));
                break;
        }
    }
}
