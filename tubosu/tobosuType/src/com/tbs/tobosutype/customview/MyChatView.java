package com.tbs.tobosutype.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tbs.tobosutype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class MyChatView extends View {
    //-------------必须给的数据相关-------------
    //分配比例大小，总比例大小为100
    public List<Float> floatList = new ArrayList<>();

    public List<Float> getFloatList() {
        return floatList;
    }

    public void setFloatList(List<Float> floatList) {
        this.floatList = floatList;
    }

    //圆的直径
    private float mRadius;
    //圆的粗细
    private float mStrokeWidth;
    //-------------画笔相关-------------
    //圆环的画笔
    private Paint cyclePaint;
    //标注的画笔
    private Paint labelPaint;
    //-------------颜色相关-------------
    //边框颜色和标注颜色

    //颜色顺序 绿色（82d09c五金）  湖蓝（62AAE5家具） 暗红（dcb09e其他） 橙黄（f1c663厨卫） 水蓝（96b6d6人工） 橙色（ff7f5b建材）
    public int[] mColor = new int[]{Color.parseColor("#a6c1dd"), Color.parseColor("#ffa36f"),
            Color.parseColor("#8fd6a7"), Color.parseColor("#78bfe6"),
            Color.parseColor("#ffd77f"), Color.parseColor("#dcb09e")};
    //文字颜色
    private int textColor = 0xFF000000;
    //-------------View相关-------------
    //View自身的宽和高
    private int mHeight;
    private int mWidth;


    public MyChatView(Context context) {
        super(context);
    }

    public MyChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
    }

    public MyChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动画布到圆环的左上角
        canvas.translate(mWidth / 2 - mRadius / 2, mHeight / 2 - mRadius / 2);
        //初始化画笔
        initPaint();
        //画圆环
        drawCycle(canvas);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //边框画笔
        cyclePaint = new Paint();
        cyclePaint.setAntiAlias(true);//边缘抗锯齿
        cyclePaint.setStyle(Paint.Style.STROKE);//画笔的类型
        cyclePaint.setStrokeWidth(mStrokeWidth);//画笔笔触宽度
        //标注画笔
        labelPaint = new Paint();
        labelPaint.setAntiAlias(true);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setStrokeWidth(2);
    }

    /**
     * 画圆环
     *
     * @param canvas
     */
    //测试
//    Float[] floats = {38.89f, 50.0f, 6.11f, 0f, 0f, 5.0f};
//
//    private void drawCycle(Canvas canvas) {
//        float startPercent = 0;
//        float sweepPercent = 0;
//        for (int i = 0; i < floats.length; i++) {
//            cyclePaint.setColor(mColor[i]);
//            startPercent = sweepPercent + startPercent;
//            //这里采用比例占100的百分比乘于360的来计算出占用的角度，使用先乘再除可以算出值
//            sweepPercent = floats[i] * 360 / 100;
//            canvas.drawArc(new RectF(0, 0, mRadius, mRadius), startPercent, sweepPercent, false, cyclePaint);
//        }
//    }

    //正式
    private void drawCycle(Canvas canvas) {
        float startPercent = 0;
        float sweepPercent = 0;
        for (int i = 0; i < floatList.size(); i++) {
            cyclePaint.setColor(mColor[i]);
            startPercent = sweepPercent + startPercent;
            //这里采用比例占100的百分比乘于360的来计算出占用的角度，使用先乘再除可以算出值
            sweepPercent = floatList.get(i) * 360 / 100;
            Log.e("MyChatView", "=====计算比例值===" + sweepPercent);
            canvas.drawArc(new RectF(0, 0, mRadius, mRadius), startPercent, sweepPercent, false, cyclePaint);
        }
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyChatView);
        if (typedArray != null) {
            mRadius = typedArray.getDimension(R.styleable.MyChatView_MyChatView_radius, 0);
            mStrokeWidth = typedArray.getDimension(R.styleable.MyChatView_MyChatView_strokewidth, 0);
            typedArray.recycle();
        }
    }
}

