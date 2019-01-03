package com.tbs.tobosutype.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;

/**
 * Created by Mr.Wang on 2019/1/2 13:35.
 */
public class DecorationToolItem extends RelativeLayout {

    /**
     * 图片
     */
    private ImageView imageIcon;
    /**
     * 标题
     */
    private TextView tvTittle;

    public DecorationToolItem(Context context, AttributeSet attrs) {
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
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DecorationToolItem);
        //图片资源
        Drawable drawable = array.getDrawable(R.styleable.DecorationToolItem_decoration_tool_image);
        imageIcon.setImageDrawable(drawable);
        //图片底部文字
        CharSequence text = array.getText(R.styleable.DecorationToolItem_decoration_tool_text);
        tvTittle.setText(text);
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
        View view = LayoutInflater.from(context).inflate(R.layout.widget_decoration_tool, this, true);
        imageIcon = view.findViewById(R.id.image_icon);
        tvTittle = view.findViewById(R.id.tv_tittle);

    }
}
