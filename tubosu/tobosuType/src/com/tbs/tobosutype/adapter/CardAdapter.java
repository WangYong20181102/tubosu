package com.tbs.tobosutype.adapter;

import android.support.v7.widget.CardView;

/**
 * Created by Mr.Lin on 2018/9/4 15:09.
 */
public interface CardAdapter {
    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
