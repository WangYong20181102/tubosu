package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.fragment.CurtainFragment;
import com.tobosu.mydecorate.fragment.FloorBrickFragment;
import com.tobosu.mydecorate.fragment.FloorFragment;
import com.tobosu.mydecorate.fragment.PaintFragment;
import com.tobosu.mydecorate.fragment.WallBrickFragment;
import com.tobosu.mydecorate.fragment.WallPicFragment;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class CalculaterActivity extends BaseActivity {

    private static final String TAG = CalculaterActivity.class.getSimpleName();
    private Context context;
    private ImageView iv_back_calculater;
    private ViewPagerIndicator indicator;
    private ViewPager vp_calculater;
    private FloorFragment floorFragment;
    private WallBrickFragment wallBrickFragment;
    private FloorBrickFragment floorBrickFragment;
    private WallPicFragment wallPicFragment;
    private CurtainFragment curtainFragment;
    private PaintFragment paintFragment;
    private ArrayList<Fragment> itemFragmentList;
    private List<String> fragmentTitle = new ArrayList<String>();
    private int[] mTextColors = {0xFFA0A0A0, 0xFF000000};
    private int mUnderlineColor = 0xFF168EFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calculater);
        context = CalculaterActivity.this;

        initView();
        initViewData();
        initViewPager();
    }


    private void initView() {
        iv_back_calculater = (ImageView) findViewById(R.id.iv_back_calculater);
        iv_back_calculater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        indicator = (ViewPagerIndicator) findViewById(R.id.calculater_indicator);
        vp_calculater = (ViewPager) findViewById(R.id.calculater_viewpager);
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
        floorFragment=new FloorFragment();
        wallBrickFragment=new WallBrickFragment();
        floorBrickFragment=new FloorBrickFragment();
        wallPicFragment=new WallPicFragment();
        curtainFragment=new CurtainFragment();
        paintFragment=new PaintFragment();

        itemFragmentList.add(floorFragment);
        itemFragmentList.add(wallBrickFragment);
        itemFragmentList.add(floorBrickFragment);
        itemFragmentList.add(wallPicFragment);
        itemFragmentList.add(curtainFragment);
        itemFragmentList.add(paintFragment);
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
