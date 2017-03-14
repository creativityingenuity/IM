package com.gxs.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gxs.im.R;
import com.hyphenate.util.DensityUtil;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class QuickIndexBar extends View {
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private Paint mPaint;
    private int x;
    private float cellHeight;
    private int index = -1;

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置文本对齐。控制文本相对于其原点位置 LEFT对齐意味着所有文本将被绘制到其原点的右边
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(
                widthMode == MeasureSpec.AT_MOST ? 0 : widthSize,
                heightMode == MeasureSpec.AT_MOST ? 0 : heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //每个字母x坐标
        x = getMeasuredWidth() / 2;
        //把每个字母的位置看成一个格子，这个是每个格子的高度
        cellHeight = getMeasuredHeight() * 1f / LETTERS.length;
    }

    //获取文字高度
    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);//方法执行完，bounds就有值了
        return bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //遍历数组，将27个字母全部绘制上来
        for (int i = 0; i < LETTERS.length; i++) {
            //判断如果当前正在绘制的字母和index相同，那么就改变颜色
            mPaint.setColor(index==i?getResources().getColor(R.color.btn_normal):Color.GRAY);
            canvas.drawText(
                    LETTERS[i],
                    x,
                    //y:格子高度的一半 + 文字高度的一半 + position*格子高度
                    cellHeight / 2 + getTextHeight(LETTERS[i]) / 2 + i * cellHeight,
                    mPaint
            );
        }
    }

    //绘制完26个字母之后，需要求出当前触摸点对应的是哪个字母，
    // 思路是：直接将触摸点的y坐标除以格子的高度，得到的值就是字母对应的索引
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (index != y / cellHeight) {
                    index = (int) (y / cellHeight);
                    //对index进行安全性检测
                    if (index >= 0 && index < LETTERS.length) {
                        //将按下的字母改变颜色
                        invalidate();
                        if(listner!=null) {
                            listner.onLetterChange(LETTERS[index]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起进行重置，否则再次点击不会出现
                index = -1;
                listner.onTouchUp();
                break;
        }
        return true;
    }

    //计算出当前触摸的字母之后，需要将触摸字母改变的事件暴露给外界，所以定义监听器，并在字母改变的时候进行回调
    private OnTouchLetterChangeListner listner;

    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListner listner) {
        this.listner = listner;
    }

    public interface OnTouchLetterChangeListner {
        void onLetterChange(String letter);
        void onTouchUp();
    }
}
