package com.tbs.tbs_mj.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/11/29 18:16.
 * 文本轮播器
 */

public class VerticalTextview extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;
    private float mTextSize;
    private int mPadding;
    private int textColor;
    private VerticalTextview.OnItemClickListener itemClickListener;
    private Context mContext;
    private int currentId;
    private ArrayList<String> textList;
    private Handler handler;


    public void setText(float textSize, int padding, int textColor) {
        this.mTextSize = textSize;
        this.mPadding = padding;
        this.textColor = textColor;
    }

    public VerticalTextview(Context context) {
        super(context);
        this.mContext = context;
    }

    public VerticalTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTextSize = 16.0F;
        this.mPadding = 5;
        this.textColor = -16777216;
        this.currentId = -1;
        this.mContext = context;
        this.textList = new ArrayList();
    }
    public void setAnimTime(long animDuration) {
        this.setFactory(this);
        Animation in = new TranslateAnimation(0.0F, 0.0F, (float)animDuration, 0.0F);
        in.setDuration(animDuration);
        in.setInterpolator(new AccelerateInterpolator());
        Animation out = new TranslateAnimation(0.0F, 0.0F, 0.0F, (float)(-animDuration));
        out.setDuration(animDuration);
        out.setInterpolator(new AccelerateInterpolator());
        this.setInAnimation(in);
        this.setOutAnimation(out);
    }

    public void setTextStillTime(final long time) {
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 0:
                        if(VerticalTextview.this.textList.size() > 0) {
                            VerticalTextview.this.currentId++;
                            VerticalTextview.this.setText((CharSequence)VerticalTextview.this.textList.get(VerticalTextview.this.currentId % VerticalTextview.this.textList.size()));
                        }

                        VerticalTextview.this.handler.sendEmptyMessageDelayed(0, time);
                        break;
                    case 1:
                        VerticalTextview.this.handler.removeMessages(0);
                }

            }
        };
    }

    public void setTextList(ArrayList<String> titles) {
        this.textList.clear();
        this.textList.addAll(titles);
        this.currentId = -1;
    }
    public void startAutoScroll() {
        this.handler.sendEmptyMessage(0);
    }

    public void stopAutoScroll() {
        this.handler.sendEmptyMessage(1);
    }

    public View makeView() {
        TextView t = new TextView(this.mContext);
        t.setGravity(19);
        t.setMaxLines(2);
        t.setPadding(this.mPadding, this.mPadding, this.mPadding, this.mPadding);
        t.setTextColor(this.textColor);
        t.setTextSize(this.mTextSize);
        t.setClickable(true);
        t.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(VerticalTextview.this.itemClickListener != null && VerticalTextview.this.textList.size() > 0 && VerticalTextview.this.currentId != -1) {
                    VerticalTextview.this.itemClickListener.onItemClick(VerticalTextview.this.currentId % VerticalTextview.this.textList.size());
                }

            }
        });
        return t;
    }


    public void setOnItemClickListener(VerticalTextview.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int var1);
    }
}
