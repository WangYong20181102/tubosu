package com.tbs.tobosutype.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 货币格式化
 *
 * @author WangYong
 */
public class MoneyFormatUtil {
    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("#,##0.00");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留两位小数,格式化后没有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format2(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null") || moneyStr.equals("NaN")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0.00");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留一位小数,格式化后没有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format3(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.0";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0.0");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     *
     */
    public static <T> String formatW(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0";
        }
        NumberFormat nf = new DecimalFormat("0.#");
        String testStr = nf.format(Double.parseDouble(moneyStr));

        return testStr;
    }


    /**
     * 将金钱格式化成带有万字单位结尾
     *
     * @param t 金额 小于1万会以0.X万字单位结尾
     * @return
     */
    public static <T> String formatW2(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        double w = money / 10000;
        NumberFormat nf = new DecimalFormat("0.##");
        String testStr = nf.format(w);

        return testStr + "万";
    }


    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String formatZ(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String formatPersonNum(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("#,###");
        String testStr = nf.format(money);

        return testStr;
    }
}
