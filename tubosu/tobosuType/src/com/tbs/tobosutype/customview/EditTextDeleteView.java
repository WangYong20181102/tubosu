package com.tbs.tobosutype.customview;


import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.PublicFunc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class EditTextDeleteView extends RelativeLayout {

    private ImageView img_del;
    private EditText ed_username;
    private RelativeLayout relativeLayout;
    private  CharSequence hint;
    public EditTextDeleteView(Context context) {
        super(context);
    }

    public EditTextDeleteView(Context context,AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.EditTextDeleteView);
        Drawable drawable = array.getDrawable(R.styleable.EditTextDeleteView_drawable);
         hint = array.getText(R.styleable.EditTextDeleteView_hint);
        relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.edittext_del_layout,this,true);
        img_del = (ImageView) relativeLayout.findViewById(R.id.img_del);
        ed_username = (EditText) relativeLayout.findViewById(R.id.ed_username);
        ed_username.setHint(hint);
        if(drawable != null)
        {
            img_del.setImageDrawable(drawable);
        }
        ed_username.setOnFocusChangeListener(PublicFunc.onFocusAutoClearHintListener);
        img_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	ed_username.setText("");
            }
        });
        ed_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            	
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 0)
                {
                    img_del.setVisibility(VISIBLE);
                }else
                {
                    img_del.setVisibility(GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        array.recycle();
    };



    public String getText()
    {
        return ed_username.getText().toString();
    }

    public void setText(String str)
    {
    	ed_username.setText(str);
    }

    public void setDeleteImageResources(int id)
    {
        img_del.setImageResource(id);
    }

    public void setHint(CharSequence hint)
    {
    	ed_username.setHint(hint);
    }

}
