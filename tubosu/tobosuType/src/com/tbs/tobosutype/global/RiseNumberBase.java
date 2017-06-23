package com.tbs.tobosutype.global;

import com.tbs.tobosutype.customview.RiseNumberTextView;

/**
 * Created by Lie on 2017/6/20.
 */

public interface RiseNumberBase {
    public void start();

    public RiseNumberTextView withNumber(float number);

    public RiseNumberTextView withNumber(float number, boolean flag);

    public RiseNumberTextView withNumber(int number);

    public RiseNumberTextView setDuration(long duration);

//    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
