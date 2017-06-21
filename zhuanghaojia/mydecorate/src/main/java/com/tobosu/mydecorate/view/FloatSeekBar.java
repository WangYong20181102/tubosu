package com.tobosu.mydecorate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

public class FloatSeekBar extends View {
    private String TAG = "FloatProgressBar";

    private Context mContext;

    private int mMax = 100;
    private int mProgress = 0;

    private int mWidth;
    private int mHeight;
    private int radius;

    private RectF backRectF;
    private RectF fontRectF;

    private Paint paintBack;
    private Paint paintFont;

    private OnProgressChangeListener mListener;

    private WindowManager wm;
    private LayoutParams floatLP;

    private TextView floatView;
    private int floatViewWidth;
    private int floatViewHeight;

    private int mFloatVerticalSpacing;
    private int statusHeight;

    public FloatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        // 矩形初始化
        backRectF = new RectF();
        fontRectF = new RectF();

        // 画笔初始化
        paintBack = new Paint();
        paintBack.setAntiAlias(true);
        paintBack.setColor(0XFFDDDDDD);
        paintFont = new Paint(paintBack);
        paintFont.setColor(0XFFD54321);

        // 获得WindowManager
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // 创建FloatView
        floatView = new TextView(getContext());
        floatView.setGravity(Gravity.CENTER);
        floatView.setBackgroundResource(R.drawable.shape_circle_blue);
        floatView.setTextColor(0XFFFFFFFF);
        floatViewWidth = (int) dp2px(40);
        floatViewHeight = floatViewWidth;

        // FloatView添加到Window的参数初始化
        floatLP = new LayoutParams();
        floatLP.width = floatViewWidth;
        floatLP.height = floatViewHeight;
        floatLP.gravity = Gravity.LEFT | Gravity.TOP;
        floatLP.format = PixelFormat.RGBA_8888;
        floatLP.windowAnimations = R.style.pppanim;
        // 根据屏幕密度计算FloatView的垂直间隔
        mFloatVerticalSpacing = (int) dp2px(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (wMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                mWidth = wSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                mWidth = getDefaultWidth();
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                mHeight = hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                mHeight = getDefaultHeight();
                break;
        }
        setMeasuredDimension(mWidth, mHeight);
        onMeasureComplete();
    }

    private void onMeasureComplete() {
        backRectF.left = 0;
        backRectF.right = mWidth;
        backRectF.top = 0;
        backRectF.bottom = mHeight;
        fontRectF.left = 0;
        fontRectF.right = mProgress * mWidth / mMax;
        fontRectF.top = 0;
        fontRectF.bottom = mHeight;
        radius = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画背景
        canvas.drawRoundRect(backRectF, radius, radius, paintBack);
        // 需要的时候画前景
        if (fontRectF.right > 0) {
            canvas.drawRoundRect(fontRectF, radius, radius, paintFont);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获得状态栏高度
                statusHeight = getStatusHeight(mContext);
                // 修改刻度
                fontRectF.right = event.getX();
                changeProgress();
                // 修改FloatView显示文字
                floatView.setText(mProgress + "");
                // 修改FloatView的X和Y坐标
                // X坐标=当前触摸的X-FloatView的宽度/2+该ProgressBar的左边坐标
                floatLP.x = (int) event.getRawX() - floatViewWidth / 2;
                // Y坐标=相对屏幕触摸X坐标-前面根据屏幕密度计算出来的垂直间隔-状态栏高度-FloatView的高度
                floatLP.y = (int) event.getRawY() - mFloatVerticalSpacing - statusHeight - floatViewHeight;
                // 将FloatView添加进Window
                wm.addView(floatView, floatLP);
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                // 临界处理
                if (newX < 0) {
                    newX = 0;
                } else if (newX > mWidth) {
                    newX = mWidth;
                }
                // 修改刻度
                fontRectF.right = newX;
                changeProgress();
                // 修改FloatView显示文字
                floatView.setText(mProgress + "");
                // 修改FloatView的X坐标
                // 临界处理，只有在触摸在Bar范围内才去更新
                if (event.getRawX() >= getLeft() && event.getRawX() <= getRight()) {
                    floatLP.x = (int) event.getRawX() - floatViewWidth / 2;
                    // 更新FloatView在window中的位置
                    wm.updateViewLayout(floatView, floatLP);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 从window中移除FloatView
                // 在2.3版本模拟器中报IllegalArgumentException，尚未查明原因
                wm.removeView(floatView);
                break;
        }
        // 我们想处理触摸事件，所以这里要返回true，对触摸事件不清楚的，找我另一篇博文
        return true;
    }

    private void changeProgress() {
        if (fontRectF.right == 0) {
            mProgress = 0;
        } else if (fontRectF.right == mWidth) {
            mProgress = mMax;
        } else {
            mProgress = (int) (fontRectF.right * mMax / mWidth);
        }
        notifyListener();
        invalidate();
    }

    public void setProgress(int progress) {
        if (this.mProgress == progress || progress < 0) {
            return;
        }
        if (progress > mMax) {
            progress = mMax;
        }
        this.mProgress = progress;
        fontRectF.right = mProgress * mWidth / mMax;
        notifyListener();
        invalidate();
    }

    private void notifyListener() {
        if (mListener != null) {
            mListener.onProgressChange(this.mProgress);
        }
    }

    @SuppressWarnings("deprecation")
    private int getDefaultWidth() {
        return wm.getDefaultDisplay().getWidth() / 2;
    }

    private int getDefaultHeight() {
        return (int) dp2px(10);
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int newProgress);
    }

    private int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}

