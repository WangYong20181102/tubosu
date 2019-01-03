package com.tbs.tobosutype.widget;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by Mr.Wang on 2019/1/3 14:03.
 */
public class DecimalInputFilter implements InputFilter {
    private String mRegularExpression;

    public DecimalInputFilter(int firstLength, int lastLength) {
        mRegularExpression = String.format("(\\d{0,%d}(\\.\\d{0,%d})?)", firstLength, lastLength);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        boolean delete = false;
        StringBuilder builder = new StringBuilder(dest);

        if (TextUtils.isEmpty(source)) {
            delete = true;
            builder.delete(dstart, dend);
        } else {
            builder.insert(dstart, source);
        }

        String value = builder.toString();

        return value.matches(mRegularExpression) ? null : delete ? "." : "";
    }
}
