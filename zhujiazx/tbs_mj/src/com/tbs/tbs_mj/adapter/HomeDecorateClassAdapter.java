package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.tbs.tbs_mj.R;
/**
 * 首页装修课堂adapter
 * @author dec
 *
 */
public class HomeDecorateClassAdapter extends BaseAdapter {
	private Context context;
	private int selected;
	private int last;

	public HomeDecorateClassAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setData(int selected, int last) {
		this.last = last;
		this.selected = selected;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.item_home_decorate_class, null);
		ImageView iv_decorate_gallery = (ImageView) convertView.findViewById(R.id.iv_decorate_class_gallery);
//		iv_decorate_gallery.setBackground(context.getResources().getDrawable(R.drawable.first_gallery01 + position));
		setBackgroundOfVersion(iv_decorate_gallery, context.getResources().getDrawable(R.drawable.first_gallery01 + position));
		iv_decorate_gallery.setScaleType(ScaleType.CENTER_INSIDE);
		return convertView;
	}

	
	/** 
     * 在API16以前使用setBackgroundDrawable，在API16以后使用setBackground 
     * API16<---->4.1 
     * @param view 
     * @param drawable 
     */  
    private void setBackgroundOfVersion(View view, Drawable drawable) {  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {  
            //Android系统大于等于API 16，使用setBackground  
            view.setBackground(drawable);  
        } else {  
            //Android系统小于API 16，使用setBackground  
            view.setBackgroundDrawable(drawable);  
        }  
    }
}
