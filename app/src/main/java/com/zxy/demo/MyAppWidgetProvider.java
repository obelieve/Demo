package com.zxy.demo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.zxy.utility.LogUtil;

/**
 * Created by Admin
 * on 2020/11/9
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        LogUtil.e();
        for(int id:appWidgetIds){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_my);
            remoteViews.setTextViewText(R.id.text, "OK");
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        LogUtil.e();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtil.e();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        LogUtil.e();
    }
}
