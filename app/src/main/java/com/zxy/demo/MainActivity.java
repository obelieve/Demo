package com.zxy.demo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxy.utility.LogUtil;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RxPermissions rxPermissions = new RxPermissions(this);
        mTv = findViewById(R.id.tv);
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode2(rxPermissions);
            }
        });
    }

    /**
     *  主要四个操作符
     *  requestEach requestEachCombined
     *  ensureEach ensureEachCombined  这2个和RxView关联使用
     *  其中Combined是多个权限全部授权才会granted，Each是按每个算的
     * @param rxPermissions
     */
    private void mode2(RxPermissions rxPermissions) {
        rxPermissions
                .requestEachCombined(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> { // will emit 1 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !
                        Toast.makeText(getBaseContext(),"已授权:"+permission.name,Toast.LENGTH_SHORT).show();
                        LogUtil.e("已授权:"+permission.name);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        Toast.makeText(getBaseContext(),"未经允许不再询问"+permission.name,Toast.LENGTH_SHORT).show();
                        LogUtil.e("未经允许不再询问:"+permission.name);
                    } else {
                        // Denied permission with ask never again //永不询问
                        // Need to go to the settings]

                        LogUtil.e("永不询问:"+permission.name);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);

                        intent.setData(uri);

                        startActivityForResult(intent, 1);
                    }
                });
    }

    private void mode1(RxPermissions rxPermissions) {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        Toast.makeText(getBaseContext(),"已授权",Toast.LENGTH_SHORT).show();
                    } else {
                        // At least one permission is denied
                        Toast.makeText(getBaseContext(),"拒绝",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
