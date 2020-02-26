package com.zxy.demo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.zxy.utility.LogUtil;

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
    LineChart chartLine;

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
        List<RecordEntry> entryList = convertRecordEntry(floatList,new int[]{Color.parseColor("#FF4C44"), Color.parseColor("#FFA200"), Color.parseColor("#333333")});
        LogUtil.e(entryList + " 萨达所");
        //设置图表
        settingChart(chartLine);//配置图表
        setDataChart(chartLine, entryList);//设置数据
        chartLine.invalidate();

    }

    private static List<RecordEntry> convertRecordEntry(List<Float> floatList,int[] colors)  {
        List<RecordEntry> entryList = new ArrayList<>();
        for (int i = 0; i < floatList.size(); i++) {
            float x = i;
            float y = floatList.get(i);
            int color = getEntryColor(y,colors);
            RecordEntry entry = new RecordEntry(x, y, color);
            entryList.add(entry);
        }
        return entryList;
    }

    public static int getEntryColor(float val, int[] colors) {
        int index;
        if (val == -2) {
            index = 0;
        } else if (val == 1) {
            index = 1;
        } else if (val == 0) {
            index = 2;
        } else {
            index = 0;
        }
        return colors[index];
    }

    /**
     * 设置图表数据
     *
     * @param lineChart
     * @param entryList
     */
    private static void setDataChart(LineChart lineChart, List<RecordEntry> entryList) {
        LineDataSet lineDataSet = genLineDataSet(entryList);//生成LineDataSet
        LineData lineData = new LineData(lineDataSet);
        //设置一页最大显示个数为10，超出部分就滑动
        final int COUNT = 10;//一页最大显示个数为10
        float ratio = (float) entryList.size() / (float) COUNT;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        lineChart.zoom(ratio, 0f, 0, 0);
        lineChart.setData(lineData);
    }

    /**
     * 配置图表
     *
     * @param lineChart
     */
    private static void settingChart(LineChart lineChart) {
        setLegend(lineChart.getLegend());//设置图例说明
        axisStyle(lineChart);//设置轴线样式
        lineChart.getDescription().setEnabled(false);//不显示描述
        lineChart.setTouchEnabled(true);//是否Touch
        lineChart.setDragEnabled(true);//是否拽拖
        lineChart.setPinchZoom(false);//是否设置缩放
        lineChart.setScaleEnabled(false);//是否缩放

        lineChart.setDrawGridBackground(false);//绘制网格背景
        lineChart.getLegend().setEnabled(true);//是否绘制说明（标记-线的颜色和描述）
    }

    /**
     * 生成LineDataSet
     *
     * @param entryList
     * @return
     */
    private static LineDataSet genLineDataSet(List<RecordEntry> entryList) {
        final List<Integer> COLORS = new ArrayList<>();
        for (RecordEntry recordEntry : entryList) {
            COLORS.add(recordEntry.getColor());
        }
        final int[] GRADIENT_COLOR = new int[]{Color.parseColor("#FFC6C6C6"), Color.parseColor("#0EE6E6E6")};
        LineDataSet lineDataSet = new LineDataSet((List) entryList, "");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//贝塞尔曲线
        //绘制连接线下面填充区域
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(GRADIENT_COLOR);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setDrawFilled(true);
        //点的绘制设置
        lineDataSet.setDrawValues(false);//是否绘制点的值
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColors(COLORS);
        //点的连接线设置
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(Color.BLACK);
        //高亮设置
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setHighLightColor(Color.TRANSPARENT);
        return lineDataSet;
    }

    /**
     * 设置图例说明Legend
     *
     * @param legend
     */
    public static void setLegend(Legend legend) {
        final int TEXT_COLOR = Color.parseColor("#999999");
        final String[] LABELS = new String[]{"红", "水", "黑"};//3个
        final int[] COLORS = new int[]{Color.parseColor("#FF4C44"), Color.parseColor("#FFA200"), Color.parseColor("#333333")};//3个
        final float FORM_SIZE = 11f;
        final float FORM_LINE_WIDTH = 30f;
        final float Y_ENTRY_SPACE = 120.0f / (float) (2.5 * 3);//Legend间距
        legend.setTextColor(TEXT_COLOR);
        List<LegendEntry> entries = new ArrayList<>();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        entries.add(new LegendEntry(
                LABELS[0],
                Legend.LegendForm.CIRCLE,
                FORM_SIZE,
                FORM_LINE_WIDTH,
                null,
                COLORS[0])
        );
        entries.add(new LegendEntry(
                LABELS[1],
                Legend.LegendForm.CIRCLE,
                FORM_SIZE,
                FORM_LINE_WIDTH,
                null,
                COLORS[1])
        );
        entries.add(new LegendEntry(
                LABELS[2],
                Legend.LegendForm.CIRCLE,
                FORM_SIZE,
                FORM_LINE_WIDTH,
                null,
                COLORS[2]));

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setYEntrySpace(Y_ENTRY_SPACE);
        legend.setXOffset(15);
        legend.setYOffset(12);
        legend.setCustom(entries);
    }

    public static void axisStyle(LineChart chartLine) {
        final int LINE_COLOR = Color.parseColor("#F0F0F0");//线颜色
        final float GRANULARITY = 1.0f;//Y轴间隔
        final float Y_AXIS_MINIMUM = -0.1f;//Y轴最小值
        final float Y_AXIS_MAXIMUM = 2.5f;//Y轴最大值
        //Axis X轴样式
        XAxis xAxis = chartLine.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.TRANSPARENT);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setAxisLineColor(LINE_COLOR);
        //Axis Y轴样式
        YAxis leftAxis = chartLine.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(false);
        leftAxis.setGranularity(GRANULARITY);
        leftAxis.setAxisMinimum(Y_AXIS_MINIMUM);
        leftAxis.setAxisMaximum(Y_AXIS_MAXIMUM);
        leftAxis.setAxisLineColor(LINE_COLOR);
        leftAxis.setGridColor(LINE_COLOR);
        YAxis rightAxis = chartLine.getAxisRight();
        rightAxis.setEnabled(false);
        chartLine.invalidate();
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {

    }

    public static class RecordEntry extends Entry {
        private int color;

        public RecordEntry(float x, float y, int color) {
            super(x, y);
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        @Override
        public String toString() {
            return "RecordEntry(color=" + color + "," + getX() + "," + getY() + ')';
        }
    }
}
