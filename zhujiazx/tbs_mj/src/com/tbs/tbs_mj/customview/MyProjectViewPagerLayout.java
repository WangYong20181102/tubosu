package com.tbs.tbs_mj.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.utils.DensityUtil;


public class MyProjectViewPagerLayout extends LinearLayout implements OnClickListener{
	private Context context;
	//导航栏
	private LinearLayout linear_navigation;
	//viewpager栏
	private LinearLayout linear_viewpager;
	//标题栏滚动条
	private HorizontalScrollView scrollview;
	
	private ViewPager viewpager;
	
	private ImageView navigation;
	
	//用于导航栏的滑动
	private Matrix matrix;
	//导航栏当前偏移量
	private float ffo;
	//屏幕宽度
	private int mWidth;
	//单个选项卡宽度
	private int width;
	//标题数组
	private String[] titles;
	//页面单次展示标题个数
	private int maxColumn = 4;
	
	private LinearLayout titleLinearlayout;
	
	private static final int COLOR_TEXT_NORMAL = 0xff74787b;
	private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xffffae00;

	private String fragment_type = "";
	
	
	public MyProjectViewPagerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init(){
		setDefaultWidth();
		setDefaultLinears();
		setViewPager();
	}

	//获得屏幕宽度
	private void setDefaultWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
	}
	
	//设置初始的3个行布局
	private void setDefaultLinears(){
		// 最外层布局 包裹indicator和viewpager的 horiziontalscrollview
		scrollview = new HorizontalScrollView(context);
		// 导航层布局 即indicator
		linear_navigation = new LinearLayout(context);
		// viewpager层布局
		linear_viewpager = new LinearLayout(context);
		
		LinearLayout.LayoutParams whole_scroll_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout.LayoutParams navigation_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout.LayoutParams viewpager_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

		scrollview.setLayoutParams(whole_scroll_params);
		//不显示滚动条
		scrollview.setHorizontalScrollBarEnabled(false);
		
		linear_navigation.setLayoutParams(navigation_params);
		linear_viewpager.setLayoutParams(viewpager_params);
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(scrollview);
		this.addView(linear_navigation);
		this.addView(linear_viewpager);
	}
	
	//设置一个viewpager
	private void setViewPager(){
		if(viewpager==null){
			viewpager = new ViewPager(context);
			viewpager.setId("project_viewpager".hashCode());
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewpager.setLayoutParams(params);
		viewpager.setOnPageChangeListener(new MyOnViewPagerChangeLintener());
		linear_viewpager.addView(viewpager);

	}

	private void setPage(){

		if("local".equals(fragment_type)){
			viewpager.setCurrentItem(2);
		}else if("style".equals(fragment_type)){
			// 风格欣赏
			viewpager.setCurrentItem(3);
			setSelectedPage(3);
		}else if("space".equals(fragment_type)){
			viewpager.setCurrentItem(1);
		}else if("special".equals(fragment_type)){
			viewpager.setCurrentItem(4);
			setSelectedPage(4);
		}else if("home_sytle_design".equals(fragment_type)){
			// 户型设计
			viewpager.setCurrentItem(0);
		}
	}


	/**
	 * 标题栏标题数必须与viewpager页数一致
	 * @param titles	想要使用的标题栏
	 */
	public void setTitles(String[] titles){
		this.titles = titles;
	}
	
	/**
	 * 标题栏标题数必须与viewpager页数一致
	 * @param titles	想要使用的标题栏
	 * @param maxColumn	一个页面中最多同时展示几个标题,
	 */
	public void setTitles(String[] titles,int maxColumn){
		this.titles = titles;
		this.maxColumn = maxColumn;
	}
	
	public void setAdapter(PagerAdapter adapter, String type){
		if(viewpager!=null && adapter!=null){
			viewpager.setAdapter(adapter);
		}
		this.fragment_type = type;
	}
	
	//属性设置完成，开始构建标题栏与导航栏
	public void ok(){
		setTitle();
		setNavigation();
		setPage();
	}
	
	private void setTitle(){
		titleLinearlayout = new LinearLayout(context);
		titleLinearlayout.setOrientation(LinearLayout.HORIZONTAL);
		FrameLayout.LayoutParams title_param = new FrameLayout.LayoutParams(width * titles.length, LayoutParams.WRAP_CONTENT);
		titleLinearlayout.setLayoutParams(title_param);
		scrollview.addView(titleLinearlayout);
		
		if(titles.length > maxColumn){
			width = mWidth/maxColumn;
		} else{
			width = mWidth/titles.length;
		}
		
		LinearLayout.LayoutParams text_param = new LinearLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
		text_param.setMargins(0, DensityUtil.px2dip(this.getContext(), 24), 0, 0);
		text_param.gravity = Gravity.CENTER;
		
		for(int i = 0; i < titles.length; i++){
			TextView text = new TextView(context);
			text.setText(titles[i]);
			text.setTag(i);
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			text.setTextColor(COLOR_TEXT_NORMAL);
			text.setGravity(Gravity.CENTER);
			text.setOnClickListener(this);
			text.setLayoutParams(text_param);
			titleLinearlayout.addView(text);
		}
		TextView tv = (TextView) titleLinearlayout.getChildAt(0);
		tv.setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
	}
	
	private void setNavigation(){
		navigation = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		navigation.setLayoutParams(params);
		navigation.setScaleType(ScaleType.MATRIX);
		matrix = new Matrix(); 
		matrix.postTranslate(0, 0);  
        //重绘bitmap至合适大小
        Bitmap d2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.navigation);
        Bitmap d3 = Bitmap.createBitmap(d2, 0, 0, (int)width, 30);
        //释放该bitmap所占内存
        d2.recycle();
        navigation.setImageBitmap(d3);
        navigation.setImageMatrix(matrix); 
        navigation.invalidate();
        linear_navigation.addView(navigation);
	}



	/**
	 * viewpager监听事件
	 * @author dec
	 */
	private class MyOnViewPagerChangeLintener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
//			viewpage选定后进行导航栏的偏移
			setSelectedPage(position);
		}
		
	}

	private void setSelectedPage(int num){
		load(num);
		changeTextStyle(num);
	}
	

	/**
	 * 导航栏
	 * @param position
	 */
	private void load(int position){

		if(matrix!=null){
			if(position > maxColumn/2 && position < titles.length - maxColumn/2){
				scrollview.scrollTo(width * (position -maxColumn/2), 0);
				if(position != titles.length-maxColumn/2 - 1){
					return;
				}
			}

			int i = 0;
			if(position <= maxColumn/2){
				i = position;
				if(position == maxColumn/2){
					scrollview.scrollTo(0, 0);
				}
			}else{
				int k = titles.length - maxColumn/2 - 1;
				i = maxColumn/2 + position - k;
			}
			float x = width * i;
			matrix.postTranslate(x - ffo, 0);
			ffo = i * width;
			navigation.setImageMatrix(matrix);
			navigation.invalidate();
		}

	}
	
	private void changeTextStyle(int position){
		if(titleLinearlayout!=null){
			for (int m = 0; m < titleLinearlayout.getChildCount(); m++) {
				View view = titleLinearlayout.getChildAt(m);
				if (view instanceof TextView) {
					((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
				}
			}

			View v = titleLinearlayout.getChildAt(position);
			if(v instanceof TextView) {
				((TextView) v).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
			}
		}

	}
	
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		viewpager.setCurrentItem(position);
	}
}