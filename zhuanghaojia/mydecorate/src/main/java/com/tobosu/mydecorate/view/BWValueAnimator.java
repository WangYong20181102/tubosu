package com.tobosu.mydecorate.view;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by dec on 2016/10/27.
 */

public class BWValueAnimator {
    public static ValueAnimator getValueAnimator(float param, int time){
        ValueAnimator valueX = ValueAnimator.ofFloat(0,param);
        valueX.setInterpolator(new LinearInterpolator());
        valueX.setDuration(time);
        return valueX;
    }

    public static ValueAnimator getValueAnimator(float start,float end, int time){
        ValueAnimator valueX = ValueAnimator.ofFloat(start,end);
        valueX.setInterpolator(new LinearInterpolator());
        valueX.setDuration(time);
        return valueX;
    }
}
