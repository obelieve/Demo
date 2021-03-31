package com.zxy.demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *@Description
 *@author          Administrator
 *@create          2021-03-29 17:33
 */
class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.setStrokeWidth(20f)
//        paint.setStrokeCap(Paint.Cap.SQUARE)
//        canvas?.drawPoint(50f, 50f, paint)
//
//        val points = floatArrayOf(0f, 0f, 50f, 50f, 50f, 100f, 100f, 50f, 100f, 100f, 150f, 50f, 150f, 100f)
//// 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
//// 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
//        canvas!!.drawPoints(points, 1 /* 跳过两个数，即前两个 0 */,
//                8 /* 一共绘制 8 个数（4 个点）*/, paint)
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawOval(50f, 50f, 350f, 200f, paint);

//        val points = floatArrayOf(20f, 20f, 120f, 20f, 70f, 20f, 70f, 120f, 20f, 120f, 120f, 120f, 150f, 20f, 250f, 20f, 150f, 20f, 150f, 120f, 250f, 20f, 250f, 120f, 150f, 120f, 250f, 120f)
//        canvas?.drawLines(points, paint)
//        canvas?.drawRoundRect(100f, 100f, 500f, 300f, 50f, 50f, paint);

        paint.setStyle(Paint.Style.FILL); // 填充模式
        canvas?.drawArc(200f, 100f, 800f, 500f, -110f, 100f, true, paint);
        canvas?.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, paint);
    }
}