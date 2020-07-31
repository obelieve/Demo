package com.zxy.demo.test.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.zxy.demo.R;

import java.util.ArrayList;

/**
 * 单个条形图  绘制号码出现次数
 * on 2020/7/29
 */
public class NumberOfTimesBarChartView extends FrameLayout {

    BarChart barChart;

    public NumberOfTimesBarChartView(@NonNull Context context) {
        this(context, null);
    }

    public NumberOfTimesBarChartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberOfTimesBarChartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_number_of_times, this, true);
        barChart = view.findViewById(R.id.bar_chart);
        initSettingChart();
        loadData();
    }




        private void loadData() {
        final int[] COLOR = new int[]{Color.parseColor("#F53F3F"),Color.parseColor("#FF8800"),Color.parseColor("#0077FF")};
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            float val = (int) (Math.random() * 10);
            int color;
            if(val>=0&&val<2){
                if(val==0){
                    val = 0.1f;
                }
                color = COLOR[2];
            }else if(val>2&&val<5){
                color = COLOR[1];
            }else{
                color = COLOR[0];
            }
            colors.add(color);
            values.add(new BarEntry(i, val));
        }
        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setColors(colors);
            set1.setDrawValues(true);
            set1.setValueTextColors(colors);
            set1.setValueTextSize(10f);
            set1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int v = Float.valueOf(value).intValue();
                    return String.valueOf(v);
                }
            });
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
            barChart.getBarData().setBarWidth(0.35f);
            barChart.setFitBars(true);
        }
        //设置一页最大显示个数为10，超出部分就滑动
        final int COUNT = 10;//一页最大显示个数为10
        float ratio = (float) values.size() / (float) COUNT;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        barChart.zoom(ratio, 0f, 0, 0);
        barChart.setViewPortOffsets(80, 0, 0, 50);
        barChart.invalidate();
    }

    private void initSettingChart() {
        //1.Setting
        barChart.getDescription().setEnabled(false);// 图描述Text
        barChart.setTouchEnabled(true); //触屏
        barChart.setDragEnabled(true); //拽拖
        barChart.setPinchZoom(false);  // X,Y轴同时 伸缩
        barChart.setDrawBarShadow(false); //每个条形柱 阴影效果
        barChart.setDrawGridBackground(false); //条形柱 网格背景
        barChart.setDoubleTapToZoomEnabled(false); //双击点击放大
        barChart.setScaleEnabled(false);//是否缩放

        //2.Legend 图例说明
        Legend l = barChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(15f);
        l.setXOffset(0f);
        l.setXEntrySpace(80f);
        l.setTextSize(11f);
        l.setTextColor(getResources().getColor(R.color.color_333333));
        //3.点击条形图项显示
//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(chart);
//        barChart.setMarker(mv);
        //4.X、Y轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(getResources().getColor(R.color.color_999999));
        xAxis.setAxisLineWidth(0.5f);
        xAxis.setAxisLineColor(getResources().getColor(R.color.color_line));
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value==0){
                    return "";
                }else{
                    return Float.valueOf(value).intValue()+"";
                }
            }
        });

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setXOffset(10f);
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(getResources().getColor(R.color.color_999999));
        leftAxis.setAxisLineWidth(0.5f);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.color_line));
        leftAxis.setDrawGridLines(true);
        leftAxis.enableAxisLineDashedLine(15, 0, 0);
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setGridColor(getResources().getColor(R.color.color_line));
        leftAxis.setSpaceTop(50f);
        leftAxis.setGranularity(2f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(10.5f);
    }
}
