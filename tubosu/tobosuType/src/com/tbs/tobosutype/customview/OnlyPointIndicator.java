package com.tbs.tobosutype.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/4 17:46.
 */
public class OnlyPointIndicator extends View implements IPagerIndicator {
    private String TAG = "OnlyPointIndicator";
    public static final int MODE_MATCH_EDGE = 0;   // 直线宽度 == title宽度 - 2 * mXOffset
    public static final int MODE_WRAP_CONTENT = 1;    // 直线宽度 == title内容宽度 - 2 * mXOffset
    public static final int MODE_EXACTLY = 2;  // 直线宽度 == mLineWidth

    private int mMode;  // 默认为MODE_MATCH_EDGE模式

    // 控制动画
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    private float mYOffset;   // 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
    private float mLineHeight;
    private float mXOffset;
    private float mLineWidth;
    private float mRoundRadius;

    private Paint mPaint;
    private Path mPath1 = new Path();
    private Path mPath2 = new Path();
    private Path mPath3 = new Path();
    private List<PositionData> mPositionDataList;
    private List<Integer> mColors;


    private Paint myPaint;
    private Path myPath = new Path();

    private float mBezierStartPointX1;//第一条运动轨迹的起点的X坐标
    private float mBezierStartPointY1;//第一条运动轨迹起点的Y坐标

    private float mBezierEndPointX1;//第一条运动轨迹终点的X坐标
    private float mBezierEndPointY1;//第一条运动轨迹终点的Y坐标

    private float mBezierContentX1;//第一条贝塞尔控制点的X1坐标
    private float mBezierContentY1;//第一条贝塞尔控制点的Y1坐标

    private float mBezierStartPointX2;//第2条运动轨迹的起点的X坐标
    private float mBezierStartPointY2;//第2条运动轨迹起点的Y坐标

    private float mBezierEndPointX2;//第2条运动轨迹终点的X坐标
    private float mBezierEndPointY2;//第2条运动轨迹终点的Y坐标

    private float mBezierContentX2;//第2条贝塞尔控制点的X2坐标
    private float mBezierContentY2;//第2条贝塞尔控制点的Y1坐标

    private float mBezierStartPointX3;//第3条运动轨迹的起点的X坐标
    private float mBezierStartPointY3;//第2条运动轨迹起点的Y坐标

    private float mBezierEndPointX3;//第3条运动轨迹终点的X坐标
    private float mBezierEndPointY3;//第3条运动轨迹终点的Y坐标

    private float mBezierContentX3;//第3条贝塞尔控制点的X2坐标
    private float mBezierContentY3;//第3条贝塞尔控制点的Y1坐标

    private float mViewPagerOffset;//viewpager偏移量
    private float mPointT;//点所处的周期
    //自定义绘制贝塞尔曲线所用的参数↑↑
    //运动的点的起始坐标
    private float mStartPointX;
    private float mStartPointY;
    //运动点的坐标
    private float mChangePointX;
    private float mChangePointY;

    private float XD_T = 0.005f;

    public OnlyPointIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);

        // TODO: 2018/8/22 另一种思路

        myPaint = new Paint();
        myPaint.setColor(Color.parseColor("#ff882e"));
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setAntiAlias(true);//抗锯齿
        myPaint.setStrokeWidth(4);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: 绘制二阶曲线的方式 公式B(t)=(1-t)²p0+2t(1-t)p1+t²p2
        myPath.reset();
        for (int i = 0; i < 100; i++) {
            mPointT = (mViewPagerOffset + (XD_T * i)) * 2;
            if (0 <= mPointT && mPointT <= 1) {
                //点所在第一条曲线上
                if (i == 0) {
                    //获取初始值
                    mStartPointX = (1 - mPointT) * (1 - mPointT) * mBezierStartPointX1 + 2 * mPointT * (1 - mPointT) * mBezierContentX1 + mPointT * mPointT * mBezierEndPointX1;
                    mStartPointY = (1 - mPointT) * (1 - mPointT) * mBezierStartPointY1 + 2 * mPointT * (1 - mPointT) * mBezierContentY1 + mPointT * mPointT * mBezierEndPointY1;
                } else {
                    mChangePointX = (1 - mPointT) * (1 - mPointT) * mBezierStartPointX1 + 2 * mPointT * (1 - mPointT) * mBezierContentX1 + mPointT * mPointT * mBezierEndPointX1;
                    mChangePointY = (1 - mPointT) * (1 - mPointT) * mBezierStartPointY1 + 2 * mPointT * (1 - mPointT) * mBezierContentY1 + mPointT * mPointT * mBezierEndPointY1;
                }
            } else if (1 < mPointT && mPointT <= 2) {
                //在第二条曲线上
                if (i == 0) {
                    //获取初始值
                    mStartPointX = (1 - (mPointT - 1)) * (1 - (mPointT - 1)) * mBezierStartPointX2 + 2 * (mPointT - 1) * (1 - (mPointT - 1)) * mBezierContentX2 + (mPointT - 1) * (mPointT - 1) * mBezierEndPointX2;
                    mStartPointY = (1 - (mPointT - 1)) * (1 - (mPointT - 1)) * mBezierStartPointY2 + 2 * (mPointT - 1) * (1 - (mPointT - 1)) * mBezierContentY2 + (mPointT - 1) * (mPointT - 1) * mBezierEndPointY2;
                } else {
                    mChangePointX = (1 - (mPointT - 1)) * (1 - (mPointT - 1)) * mBezierStartPointX2 + 2 * (mPointT - 1) * (1 - (mPointT - 1)) * mBezierContentX2 + (mPointT - 1) * (mPointT - 1) * mBezierEndPointX2;
                    mChangePointY = (1 - (mPointT - 1)) * (1 - (mPointT - 1)) * mBezierStartPointY2 + 2 * (mPointT - 1) * (1 - (mPointT - 1)) * mBezierContentY2 + (mPointT - 1) * (mPointT - 1) * mBezierEndPointY2;
                }
            } else if (2 < mPointT && mPointT <= 3) {
                //运动到第三条曲线上
                if (i == 0) {
                    mStartPointX = (1 - (mPointT - 2)) * (1 - (mPointT - 2)) * mBezierStartPointX3 + 2 * (mPointT - 2) * (1 - (mPointT - 2)) * mBezierContentX3 + (mPointT - 2) * (mPointT - 2) * mBezierEndPointX3;
                    mStartPointY = (1 - (mPointT - 2)) * (1 - (mPointT - 2)) * mBezierStartPointY3 + 2 * (mPointT - 2) * (1 - (mPointT - 2)) * mBezierContentY3 + (mPointT - 2) * (mPointT - 2) * mBezierEndPointY3;
                } else {
                    mChangePointX = (1 - (mPointT - 2)) * (1 - (mPointT - 2)) * mBezierStartPointX3 + 2 * (mPointT - 2) * (1 - (mPointT - 2)) * mBezierContentX3 + (mPointT - 2) * (mPointT - 2) * mBezierEndPointX3;
                    mChangePointY = (1 - (mPointT - 2)) * (1 - (mPointT - 2)) * mBezierStartPointY3 + 2 * (mPointT - 2) * (1 - (mPointT - 2)) * mBezierContentY3 + (mPointT - 2) * (mPointT - 2) * mBezierEndPointY3;
                }
            }

            canvas.drawPoint(mChangePointX, mChangePointY, myPaint);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (mColors != null && mColors.size() > 0) {
            int currentColor = mColors.get(Math.abs(position) % mColors.size());
            int nextColor = mColors.get(Math.abs(position + 1) % mColors.size());
            int color = ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor);
            mPaint.setColor(color);
        }


        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        mViewPagerOffset = positionOffset;

        mBezierStartPointX1 = current.mLeft + current.width() / 5f;
        mBezierStartPointY1 = 4 * current.height() / 5f;

        mBezierContentX1 = current.mLeft + current.width() / 2f;
        mBezierContentY1 = current.height();

        mBezierEndPointX1 = current.mLeft + 4 * current.width() / 5f;
        mBezierEndPointY1 = 4 * current.height() / 5f;

        mBezierStartPointX2 = current.mLeft + 4 * current.width() / 5f;

        mBezierStartPointY2 = 4 * current.height() / 5f;

        mBezierContentX2 = ((current.mLeft + 2 * current.width() / 3f) + (next.mLeft + next.width() / 3f)) / 2f;

        mBezierContentY2 = 2 * current.height() / 3f;

        mBezierEndPointX2 = next.mLeft + next.width() / 5f;

        mBezierEndPointY2 = 4 * next.height() / 5f;

        mBezierStartPointX3 = next.mLeft + next.width() / 5f;

        mBezierStartPointY3 = 4 * next.height() / 5f;
        mBezierContentX3 = next.mLeft + next.width() / 2;
        mBezierContentY3 = next.height();

        mBezierEndPointX3 = next.mLeft + 4 * next.width() / 5f;

        mBezierEndPointY3 = 4 * next.height() / 5f;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        mXOffset = xOffset;
    }

    public float getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(float lineHeight) {
        mLineHeight = lineHeight;
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        if (mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT) {
            mMode = mode;
        } else {
            throw new IllegalArgumentException("mode " + mode + " not supported.");
        }
    }

    public Paint getPaint() {
        return mPaint;
    }

    public List<Integer> getColors() {
        return mColors;
    }

    public void setColors(Integer... colors) {
        mColors = Arrays.asList(colors);
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}