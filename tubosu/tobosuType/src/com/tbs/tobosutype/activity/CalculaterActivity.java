package com.tbs.tobosutype.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.ViewPagerIndicator;
import com.tbs.tobosutype.fragment.CurtainFragment;
import com.tbs.tobosutype.fragment.FloorBrickFragment;
import com.tbs.tobosutype.fragment.FloorFragment;
import com.tbs.tobosutype.fragment.PaintFragment;
import com.tbs.tobosutype.fragment.WallBrickFragment;
import com.tbs.tobosutype.fragment.WallPicFragment;
import com.tbs.tobosutype.utils.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class CalculaterActivity extends FragmentActivity {
    private static final String TAG = CalculaterActivity.class.getSimpleName();
    private Context context;
    private ImageView iv_back_calculater;
    private ViewPagerIndicator indicator;
    private ViewPager vp_calculater;
    private ArrayList<Fragment> itemFragmentList;
    private List<String> fragmentTitle = new ArrayList<String>();
    private int[] mTextColors = {0xFFA0A0A0, 0xFF000000};
    private int mUnderlineColor = 0xFF168EFF;
    private RelativeLayout rl_banner;//顶部栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setActivityTheme(CalculaterActivity.this);
        setContentView(R.layout.activity_calculater);
        context = CalculaterActivity.this;

        initView();
        initViewData();
        initViewPager();
    }


    private void initView() {
        iv_back_calculater = (ImageView) findViewById(R.id.iv_back_calculater);
        iv_back_calculater.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        indicator = (ViewPagerIndicator) findViewById(R.id.calculater_indicator);
        indicator.setBackgroundColor(Color.parseColor("#ff882e"));
        vp_calculater = (ViewPager) findViewById(R.id.calculater_viewpager);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }


    private void initViewData() {

        fragmentTitle.add("地板");
        fragmentTitle.add("墙砖");
        fragmentTitle.add("地砖");
        fragmentTitle.add("壁纸");
        fragmentTitle.add("窗帘");
        fragmentTitle.add("涂料");
        indicator.setTabItemTitles(context, fragmentTitle);

        itemFragmentList = new ArrayList<Fragment>();
        itemFragmentList.add(new FloorFragment());
        itemFragmentList.add(new WallBrickFragment());
        itemFragmentList.add(new FloorBrickFragment());
        itemFragmentList.add(new WallPicFragment());
        itemFragmentList.add(new CurtainFragment());
        itemFragmentList.add(new PaintFragment());
    }

    private void initViewPager() {
        ItemFragmentPagerAdapter adapter = new ItemFragmentPagerAdapter(getSupportFragmentManager(), itemFragmentList);
        vp_calculater.setAdapter(adapter);
        vp_calculater.setCurrentItem(0);
        indicator.setViewPager(vp_calculater, 0);
    }


    class ItemFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentList;

        public ItemFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    /**
     * 重新计算
     *
     * @param editTextList
     */
    public static void clearEditText(ArrayList<EditText> editTextList) {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).setFocusableInTouchMode(true);
            editTextList.get(i).requestFocus();
            editTextList.get(i).setText("");
        }
        editTextList.get(0).requestFocus();
    }

    public static void clearEditTextFocus(ArrayList<EditText> editTextList) {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).clearFocus();
//			editTextList.get(i).setFocusableInTouchMode(false);
        }
    }

//	public static String clearDot(String floatNum){
//		if(floatNum.length()==2 && "0.".equals(floatNum)){
//			return "0.";
//		}else{
//			if(".".equals(floatNum.substring(floatNum.length()-1))){
//				return floatNum.substring(0,floatNum.length()-1);
//			}else{
//				return floatNum;
//			}
//		}

//		if(floatNum.length()==2){
//			if("0.".equals(floatNum)){
//				System.out.println("********0.****"+floatNum);
//				return floatNum;
//			}else if(".".equals(floatNum.substring(1))){
//				System.out.println("********.****"+floatNum);
//				return floatNum.substring(0);
//			}else{
//				System.out.println("************"+floatNum);
//				return floatNum;
//			}
//		}else{
//			System.out.println("********final****"+floatNum);
//			return floatNum;
//		}
//	}


}
