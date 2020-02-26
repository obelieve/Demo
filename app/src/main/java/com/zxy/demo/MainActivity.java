package com.zxy.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.chart_line)
    SchemeRecordChart chartLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Float> floatList = new ArrayList<>();
        float[] entryVal = new float[]{0, 1, 1.66f, 2};
        for (int i = 0; i < 30; i++) {
            floatList.add(entryVal[new Random().nextInt(4)]);
        }
        chartLine.setDataAndRefresh(floatList);

    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }
}
