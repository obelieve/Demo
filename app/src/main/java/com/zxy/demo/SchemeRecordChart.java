package com.zxy.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SchemeRecordChart extends LinearLayout {

    private static final int[] COLORS = new int[]{
            Color.parseColor("#FF4C44"),
            Color.parseColor("#FFA200"),
            Color.parseColor("#333333")};

    private static final String[] NAMES = new String[]{"红", "水", "黑"};

    private int[] mTagCounts = new int[3];
    private List<Float> mRecordList;

    private LineChart mLineChart;

    public SchemeRecordChart(@NonNull Context context) {
        super(context);
        init();
    }

    public SchemeRecordChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        mLineChart = new LineChart(getContext());
        mLineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        settingChart(mLineChart);
        addView(mLineChart);
        addView(genSchemeTagView(getContext()));
    }

    public void setRecordList(List<Float> recordList) {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        recordList = mRecordList;
        List<RecordEntry> recordEntryList = convertRecordEntry(recordList);
        setDataChart(mLineChart,recordEntryList);
        mLineChart.invalidate();
    }


    private LinearLayout genSchemeTagView(Context context) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(0, 1);
        emptyParams.weight = 1;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        View emptyView = new View(context);
        emptyView.setLayoutParams(emptyParams);
        root.addView(emptyView);
        root.addView(genSchemeTagItemView(context, params, NAMES[0], COLORS[0], mTagCounts[0]));
        root.addView(genSchemeTagItemView(context, params, NAMES[1], COLORS[1], mTagCounts[1]));
        root.addView(genSchemeTagItemView(context, params, NAMES[2], COLORS[2], mTagCounts[2]));
        return root;
    }

    private View genSchemeTagItemView(Context context, ViewGroup.LayoutParams params, String name, @ColorInt int color, int count) {
        final int WIDTH = dp2px(24);
        final int TEXT_SIZE = dp2px(12);
        final int TAG_TEXT_COLOR = Color.WHITE;
        final int TAG_PADDING_LEFT = dp2px(10);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);
        TextView tvTitle = new TextView(context);
        tvTitle.setLayoutParams(new LinearLayout.LayoutParams(WIDTH, WIDTH));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(TEXT_SIZE);
        tvTitle.setTextColor(TAG_TEXT_COLOR);
        tvTitle.setPadding(TAG_PADDING_LEFT, 0, 0, 0);
        tvTitle.setBackground(genCircleShapeDrawable(color, 12));
        tvTitle.setText(name);

        TextView tvCount = new TextView(context);
        tvCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvCount.setTextSize(TEXT_SIZE);
        tvCount.setTextColor(color);
        tvCount.setText(String.valueOf(count));
        layout.addView(tvTitle);
        layout.addView(tvCount);
        return layout;
    }

    private int dp2px(int size) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (size * density);
    }

    private ShapeDrawable genCircleShapeDrawable(@ColorInt int color, int radius) {
        radius = dp2px(radius);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        Rect rect = new Rect();
        rect.top = 0;
        rect.left = 0;
        rect.bottom = radius * 2;
        rect.right = radius * 2;
        drawable.setBounds(rect);
        return drawable;
    }

    /**
     * 转为RecordEntry列表
     *
     * @param floatList
     * @return
     */
    private static List<RecordEntry> convertRecordEntry(List<Float> floatList) {
        List<RecordEntry> entryList = new ArrayList<>();
        for (int i = 0; i < floatList.size(); i++) {
            float x = i;
            float y = floatList.get(i);
            int color = getEntryColor(y, COLORS);
            RecordEntry entry = new RecordEntry(x, y, color);
            entryList.add(entry);
        }
        return entryList;
    }

    /**
     * 设置点的颜色
     *
     * @param val
     * @param colors
     * @return
     */
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
    private static void setLegend(Legend legend) {
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

    /**
     * LineChart图表轴线样式
     *
     * @param chartLine
     */
    private static void axisStyle(LineChart chartLine) {
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
