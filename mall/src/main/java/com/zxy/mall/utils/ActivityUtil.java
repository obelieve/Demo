package com.zxy.mall.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

import com.zxy.utility.SystemUtil;

public class ActivityUtil {

    public static Bitmap takeScreenShot(Activity activity){
        return takeScreenShot(activity,true);
    }

    public static Bitmap takeScreenShot(Activity activity,boolean compress) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        // 获取屏幕长和高
        int width = SystemUtil.screenWidth();
        int height = SystemUtil.getRealHeight(activity);
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
        Matrix matrix = new Matrix();
        if(compress){
            matrix.postScale(0.2f,0.2f); //长和宽放大缩小的比例
        }else{
            matrix.postScale(1.0f,1.0f); //长和宽放大缩小的比例
        }
        // 压缩 丢弃一部分像素点
        b = Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
        view.destroyDrawingCache();
        return b;
    }
}
