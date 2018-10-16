package com.zxy.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by zxy on 2018/10/15 17:52.
 */

public class FlowLayout extends ViewGroup
{
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int horizontalSpacing = 0;  //如果子View没有指定，就使用该值设置水平间距
    private int verticalSpacing = 0;    //如果子View没有指定，就使用该值设置垂直间距
    private int orientation = 0;        //设置子View的方向
    private boolean debugDraw = false;  //设置调试子View信息

    public FlowLayout(Context context)
    {
        super(context);

        this.readStyleParameters(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        this.readStyleParameters(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);

        this.readStyleParameters(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**
         * 1.sizeWidth sizeHeight 获取 父View的尺寸
         * 2.modeWidth modeHeight 获取 父View的模式  EXACTLY AT_MOST UNSPECIFIED
         * 3.this.orientation 水平或垂直方向
         * 4.size  根据orientation H  获取 width ->size   V 获取 height -> size
         *   mode  根据orientation H  获取 width ->mode   V 获取 height -> mode
         *
         * 5.lineThicknessWithSpacing 行高+行间距
         * 6.lineThickness 行高
         * 7.lineLengthWithSpacing 行长度+行间距
         * 8.lineLength 行长度
         *
         * 9.prevLinePosition 预先行位置
         * 10.controlMaxLength 最大长度
         * 12.controlMaxThickness 最大高度
         * {子View
         *     hSpacing获取水平的间距
         *     vSpacing获取垂直的间距
         *     childWidth获取宽
         *     childHeight获取高
         *
         *     childLength获取子View长度
         *     childThickness获取子View高度
         *     spacingLength获取水平间距
         *     spacingThickness获取垂直间距
         * }
         */

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int size;
        int mode;

        if (orientation == HORIZONTAL)
        {
            size = sizeWidth;
            mode = modeWidth;
        } else
        {
            size = sizeHeight;
            mode = modeHeight;
        }

        int lineThicknessWithSpacing = 0;
        int lineThickness = 0;
        int lineLengthWithSpacing = 0;
        int lineLength;

        int prevLinePosition = 0;

        int controlMaxLength = 0;
        int controlMaxThickness = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
            {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            /**getChildMeasureSpec
             * 对父控件Mode分类
             * 1.MeasureSpec.EXACTLY:
             * 1.1 如果子View ChildSize>0  ->size = ChildSize; EXACTLY
             * 1.2 如果子View MATCH_PARENT ->size = ParentSize EXACTLY
             * 1.3 如果子View WRAP_CONTENT ->size = ParentSize AT_MOST
             * 2.MeasureSpec.AT_MOST:
             * 2.1 如果子View ChildSize>0   ->size = ChildSize  EXACTLY
             * 2.2 如果子View MATCH_CONTENT ->size = ParentSize AT_MOST
             * 2.3 如果子View WRAP_CONTENT  ->size = ParentSize AT_MOST
             * 3.MeasureSpec.UNSPECIFIED:
             * 3.1 如果子View ChildSize>0   ->size = ChildSize  EXACTLY
             * 3.2 如果子View MATCH_PATCH   ->size = 0          UNSPECIFIED
             * 3.3 如果子View WRAP_CONTENT  ->size = 0          UNSPECIFIED
             */
            //1.计算子View
            child.measure(getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
                    getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height));
            //2.获取h v的边距
            int hSpacing = this.getHorizontalSpacing(lp);
            int vSpacing = this.getVerticalSpacing(lp);
            //3.获取子View 宽高
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childLength;//子View长度
            int childThickness;//子View 高度
            int spacingLength;//子View 水平间距
            int spacingThickness;//子View 垂直间距

            if (orientation == HORIZONTAL)
            {
                childLength = childWidth;
                childThickness = childHeight;
                spacingLength = hSpacing;
                spacingThickness = vSpacing;
            } else
            {
                childLength = childHeight;
                childThickness = childWidth;
                spacingLength = vSpacing;
                spacingThickness = hSpacing;
            }
            //当前每行的长度
            lineLength = lineLengthWithSpacing + childLength;
            //当前每行的长度和间距
            lineLengthWithSpacing = lineLength + spacingLength;
            //是否切换新行
            boolean newLine = lp.newLine || (mode != MeasureSpec.UNSPECIFIED && lineLength > size);
            if (newLine)
            {//在新行设置时，设置行高和行间距的值
                prevLinePosition = prevLinePosition + lineThicknessWithSpacing;

                lineThickness = childThickness;
                lineLength = childLength;
                lineThicknessWithSpacing = childThickness + spacingThickness;
                lineLengthWithSpacing = lineLength + spacingLength;
            }
            //设置行高，对每行中的子View 取出最高的行高和行间距
            lineThicknessWithSpacing = Math.max(lineThicknessWithSpacing, childThickness + spacingThickness);
            lineThickness = Math.max(lineThickness, childThickness);

            int posX;
            int posY;
            if (orientation == HORIZONTAL)
            {
                posX = getPaddingLeft() + lineLength - childLength;
                posY = getPaddingTop() + prevLinePosition;
            } else
            {
                posX = getPaddingLeft() + prevLinePosition;
                posY = getPaddingTop() + lineLength - childHeight;
            }
            //设置子View当前的位置
            lp.setPosition(posX, posY);
            //更新当前最大的长度
            controlMaxLength = Math.max(controlMaxLength, lineLength);
            //更新当前最大的高度
            controlMaxThickness = prevLinePosition + lineThickness;
        }

		/* need to take paddings into account */
        if (orientation == HORIZONTAL)
        {
            controlMaxLength += getPaddingLeft() + getPaddingRight();
            controlMaxThickness += getPaddingBottom() + getPaddingTop();
        } else
        {
            controlMaxLength += getPaddingBottom() + getPaddingTop();
            controlMaxThickness += getPaddingLeft() + getPaddingRight();
        }

        if (orientation == HORIZONTAL)
        {
            this.setMeasuredDimension(resolveSize(controlMaxLength, widthMeasureSpec), resolveSize(controlMaxThickness, heightMeasureSpec));
        } else
        {
            this.setMeasuredDimension(resolveSize(controlMaxThickness, widthMeasureSpec), resolveSize(controlMaxLength, heightMeasureSpec));
        }
    }

    private int getVerticalSpacing(LayoutParams lp)
    {
        int vSpacing;
        if (lp.verticalSpacingSpecified())
        {
            vSpacing = lp.verticalSpacing;
        } else
        {
            vSpacing = this.verticalSpacing;
        }
        return vSpacing;
    }

    private int getHorizontalSpacing(LayoutParams lp)
    {
        int hSpacing;
        if (lp.horizontalSpacingSpecified())
        {
            hSpacing = lp.horizontalSpacing;
        } else
        {
            hSpacing = this.horizontalSpacing;
        }
        return hSpacing;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime)
    {
        boolean more = super.drawChild(canvas, child, drawingTime);
        this.drawDebugInfo(canvas, child);//调试UI使用
        return more;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet)
    {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new LayoutParams(p);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet)
    {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout);
        try
        {
            horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0);
            verticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
            orientation = a.getInteger(R.styleable.FlowLayout_orientation, HORIZONTAL);
            debugDraw = a.getBoolean(R.styleable.FlowLayout_debugDraw, false);
        } finally
        {
            a.recycle();
        }
    }

    private void drawDebugInfo(Canvas canvas, View child)
    {
        if (!debugDraw)
        {
            return;
        }

        Paint childPaint = this.createPaint(0xffffff00);
        Paint layoutPaint = this.createPaint(0xff00ff00);
        Paint newLinePaint = this.createPaint(0xffff0000);

        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        //用于绘制子View 间距的线,类似 “->”符号
        if (lp.horizontalSpacing > 0)
        {
            float x = child.getRight();
            float y = child.getTop() + child.getHeight() / 2.0f;
            canvas.drawLine(x, y, x + lp.horizontalSpacing, y, childPaint);//绘制线 "-"
            canvas.drawLine(x + lp.horizontalSpacing - 4.0f, y - 4.0f, x + lp.horizontalSpacing, y, childPaint);//绘制 “\”
            canvas.drawLine(x + lp.horizontalSpacing - 4.0f, y + 4.0f, x + lp.horizontalSpacing, y, childPaint);//绘制 "/"
        } else if (this.horizontalSpacing > 0)
        {
            float x = child.getRight();
            float y = child.getTop() + child.getHeight() / 2.0f;
            canvas.drawLine(x, y, x + this.horizontalSpacing, y, layoutPaint);
            canvas.drawLine(x + this.horizontalSpacing - 4.0f, y - 4.0f, x + this.horizontalSpacing, y, layoutPaint);
            canvas.drawLine(x + this.horizontalSpacing - 4.0f, y + 4.0f, x + this.horizontalSpacing, y, layoutPaint);
        }

        if (lp.verticalSpacing > 0)
        {
            float x = child.getLeft() + child.getWidth() / 2.0f;
            float y = child.getBottom();
            canvas.drawLine(x, y, x, y + lp.verticalSpacing, childPaint);
            canvas.drawLine(x - 4.0f, y + lp.verticalSpacing - 4.0f, x, y + lp.verticalSpacing, childPaint);
            canvas.drawLine(x + 4.0f, y + lp.verticalSpacing - 4.0f, x, y + lp.verticalSpacing, childPaint);
        } else if (this.verticalSpacing > 0)
        {
            float x = child.getLeft() + child.getWidth() / 2.0f;
            float y = child.getBottom();
            canvas.drawLine(x, y, x, y + this.verticalSpacing, layoutPaint);
            canvas.drawLine(x - 4.0f, y + this.verticalSpacing - 4.0f, x, y + this.verticalSpacing, layoutPaint);
            canvas.drawLine(x + 4.0f, y + this.verticalSpacing - 4.0f, x, y + this.verticalSpacing, layoutPaint);
        }

        if (lp.newLine)
        {
            if (orientation == HORIZONTAL)
            {
                float x = child.getLeft();
                float y = child.getTop() + child.getHeight() / 2.0f;
                canvas.drawLine(x, y - 6.0f, x, y + 6.0f, newLinePaint);
            } else
            {
                float x = child.getLeft() + child.getWidth() / 2.0f;
                float y = child.getTop();
                canvas.drawLine(x - 6.0f, y, x + 6.0f, y, newLinePaint);
            }
        }
    }

    private Paint createPaint(int color)
    {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(2.0f);
        return paint;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams
    {
        private static int NO_SPACING = -1;
        private int x;
        private int y;
        private int horizontalSpacing = NO_SPACING;
        private int verticalSpacing = NO_SPACING;
        private boolean newLine = false;

        public LayoutParams(Context context, AttributeSet attributeSet)
        {
            super(context, attributeSet);
            this.readStyleParameters(context, attributeSet);
        }

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams)
        {
            super(layoutParams);
        }

        public boolean horizontalSpacingSpecified()
        {
            return horizontalSpacing != NO_SPACING;
        }

        public boolean verticalSpacingSpecified()
        {
            return verticalSpacing != NO_SPACING;
        }

        public void setPosition(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        private void readStyleParameters(Context context, AttributeSet attributeSet)
        {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout_LayoutParams);
            try
            {
                horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_LayoutParams_layout_horizontalSpacing, NO_SPACING);
                verticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_LayoutParams_layout_verticalSpacing, NO_SPACING);
                newLine = a.getBoolean(R.styleable.FlowLayout_LayoutParams_layout_newLine, false);
            } finally
            {
                a.recycle();
            }
        }
    }

    // add
    public void setAdapter(BaseAdapter adapter)
    {
        this.removeAllViews();
        if (adapter != null)
        {
            int count = adapter.getCount();
            for (int i = 0; i < count; i++)
            {
                View item = adapter.getView(i, null, this);
                this.addView(item);
            }
        }
    }
}
