package com.tbs.tobosutype.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Lie on 2017/6/20.
 */

public class Utils {
    private static DecimalFormat dfs = null;

    public static DecimalFormat format(String pattern) {
        if (dfs == null) {
            dfs = new DecimalFormat();
        }
        dfs.setRoundingMode(RoundingMode.FLOOR);
        dfs.applyPattern(pattern);
        return dfs;
    }
}
