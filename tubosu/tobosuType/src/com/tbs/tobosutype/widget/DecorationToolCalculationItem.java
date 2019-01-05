package com.tbs.tobosutype.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;

/**
 * Created by Mr.Wang on 2019/1/3 10:03.
 */
public class DecorationToolCalculationItem extends RelativeLayout {
    /**
     * 左边文字
     */
    private TextView textLeft;
    private TextView textNoInput;
    /**
     * 右边文字
     */
    private TextView textRight;
    /**
     * 输入内容
     */
    private EditText etInput;

    public DecorationToolCalculationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context, attrs);
    }

    /**
     * 初始化数据
     *
     * @param context
     * @param attrs
     */
    private void initData(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DecorationToolCalculationItem);
        //左边文字
        CharSequence strLeft = array.getText(R.styleable.DecorationToolCalculationItem_decoration_tool_text_left);
        CharSequence strNoInput = array.getText(R.styleable.DecorationToolCalculationItem_decoration_tool_text_no_input);
        textLeft.setText(strLeft);
        textNoInput.setText(strNoInput);
        //右边文字
        CharSequence strRight = array.getText(R.styleable.DecorationToolCalculationItem_decoration_tool_text_right);
        textRight.setText(strRight);
        //文本输入框默认文字
        CharSequence strHint = array.getText(R.styleable.DecorationToolCalculationItem_decoration_tool_text_hint);
        etInput.setHint(strHint);
        //设置文本输入框默认输入两位小数
        setPointNum(4, 2);
        array.recycle();

    }

    /**
     * 初始化视图
     *
     * @param context
     */
    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.decoration_tool_calculation_item, this, true);
        textLeft = view.findViewById(R.id.text_left);
        textNoInput = view.findViewById(R.id.text_no_input);
        textRight = view.findViewById(R.id.text_right);
        etInput = view.findViewById(R.id.et_input);

    }

    /**
     * 获取edit输入内容
     */
    public String getEditContent() {
        return etInput.getText().toString().trim();
    }

    /**
     * 设置edit输入内容
     */
    public void setEditContent(String content) {
        etInput.setText(content);
    }

    /**
     * 设置数字类型(小数)
     */
    public void setNumInputTypeFloat() {
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * 设置数字类型(小数限位数)
     */
    public void setNumInputTypeFloat(int left, int right) {
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etInput.setFilters(new InputFilter[]{new DecimalInputFilter(left, right)});
    }

    /**
     * 设置数字类型(整数)
     */
    public void setNumInputTypeInt() {
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 设置数字类型(整数限位数)
     */
    public void setNumInputTypeInt(int max) {
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    /**
     * 设置小数点位数
     */
    public void setPointNum(int left, int right) {
        etInput.setFilters(new InputFilter[]{new DecimalInputFilter(left, right)});
    }
}
