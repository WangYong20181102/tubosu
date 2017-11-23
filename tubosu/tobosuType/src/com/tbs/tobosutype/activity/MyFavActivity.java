package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.MyViewPager;
import com.tbs.tobosutype.fragment.MyFavCompanyFragment;
import com.tbs.tobosutype.fragment.MyFavImageFragment;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;

/**
 * 业主 收藏页面
 *
 * @author dec
 */
public class MyFavActivity extends FragmentActivity {
    private static final String TAG = MyFavActivity.class.getSimpleName();
    public static final String DELETE_IMAGE_ACTION = "com.tobosu.delete_myfav_images";
    public static final String DELETE_COMPANY_ACTION = "com.tobosu.delete_myfav_companies";

    private Context mContext;
    private ImageView iv_myfav_back;

    /**
     * 右上角 -- 编辑和取消
     */
    private TextView tv_myfav_edit;

    private RelativeLayout rl_banner;
    /**
     * 装修图库 装修公司 的tab
     */
    private LinearLayout myfav_layout_tabtitle;

    /**
     * 装修图库 装修公司 下面的 tabline
     */
    private LinearLayout myfav_layout_tabline;

    /**
     * 收藏viewpager
     */
    private MyViewPager myfav_viewpager;

    /**
     * 底部删除布局
     */
    public FrameLayout delete_framelayout;

    /**
     * 底部删除数量
     */
    public TextView tv_delete_num;

    /**
     * 正在加载数据布局
     */
    private LinearLayout myfav_data_loading_layout;

    /**
     * 收藏页面下两个收藏类别的titleText
     */
    private TextView[] arr_titles = null;

    /**
     * 收藏页面下两个收藏类别下面的黄色标记横线
     */
    private ImageView[] arr_lines = null;

    /**
     * 是否在编辑状态<br/>
     * false -> 编辑状态<br/>
     * true  -> 取消状态
     */
    private boolean isEdit = false;

    private int fragmentPostion = 0;

    private DeleteBroadcastReceiver deleteReceiver;

    private Handler deleteHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x0000012:
                    if (isEdit) { //取消
                        tv_delete_num.setVisibility(View.VISIBLE);
//					Toast.makeText(mContext, "我真的来到了"+msg.arg1, Toast.LENGTH_SHORT).show();
                        tv_delete_num.setText("(" + msg.arg1 + ")  删 除");
                        tv_myfav_edit.setText("取消");
                        tv_myfav_edit.setVisibility(View.VISIBLE);
                    } else { //编辑
                        tv_delete_num.setVisibility(View.GONE);
                        tv_delete_num.setText("");
                        tv_myfav_edit.setText("编辑");
                        tv_myfav_edit.setVisibility(View.VISIBLE);
                    }
                    break;
                case 0x0000013:
                    if (isEdit) { //取消
                        tv_delete_num.setVisibility(View.VISIBLE);
//					Toast.makeText(mContext, "我真的来到了"+msg.arg1, Toast.LENGTH_SHORT).show();
                        tv_delete_num.setText("(" + msg.arg1 + ")  删 除");
                        tv_myfav_edit.setText("取消");
                        tv_myfav_edit.setVisibility(View.VISIBLE);
                    } else { //编辑
                        tv_delete_num.setVisibility(View.GONE);
                        tv_delete_num.setText("");
                        tv_myfav_edit.setText("编辑");
                        tv_myfav_edit.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_my_fav);

        mContext = MyFavActivity.this;

        initViews();
        initReceiver();
        initTabView();

        iv_myfav_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initReceiver() {
        deleteReceiver = new DeleteBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DELETE_IMAGE_ACTION);
        filter.addAction(DELETE_COMPANY_ACTION);
        registerReceiver(deleteReceiver, filter);
    }

    private void initViews() {
        iv_myfav_back = (ImageView) findViewById(R.id.iv_myfav_back);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        myfav_layout_tabtitle = (LinearLayout) findViewById(R.id.myfav_layout_tabtitle);
        tv_myfav_edit = (TextView) findViewById(R.id.tv_myfav_edit); // FIXME
        myfav_layout_tabline = (LinearLayout) findViewById(R.id.myfav_layout_tabline);
        myfav_viewpager = (MyViewPager) findViewById(R.id.myfav_viewpager);
        delete_framelayout = (FrameLayout) findViewById(R.id.delete_framelayout);
        tv_delete_num = (TextView) findViewById(R.id.tv_delete_num);
        myfav_data_loading_layout = (LinearLayout) findViewById(R.id.myfav_data_loading_layout);
        myfav_data_loading_layout.setVisibility(View.VISIBLE);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));

    }

    private void initTabView() {
        myfav_viewpager.setNoScrollView(false);
        arr_titles = new TextView[2];
        arr_lines = new ImageView[2];

        for (int i = 0; i < 2; i++) {
            TextView textView = (TextView) myfav_layout_tabtitle.getChildAt(i);
            arr_titles[i] = textView;
            arr_titles[i].setEnabled(true);
            arr_titles[i].setBackgroundColor(getResources().getColor(R.color.bg_title));
            arr_titles[i].setTextColor(getResources().getColor(R.color.color_black));
            arr_titles[i].setTag(i);
            arr_titles[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEdit == false) {
                        myfav_viewpager.setCurrentItem((Integer) v.getTag());
                        fragmentPostion = myfav_viewpager.getCurrentItem();
                    }
                }
            });

            ImageView imageView = (ImageView) myfav_layout_tabline.getChildAt(i);
            arr_lines[i] = imageView;
            arr_lines[i].setEnabled(true);
            arr_lines[i].setBackgroundResource(R.drawable.store_off);
            arr_lines[i].setTag(i);
            arr_lines[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEdit == false) {
                        myfav_viewpager.setCurrentItem((Integer) v.getTag());
                        fragmentPostion = myfav_viewpager.getCurrentItem();
                    }
                }
            });

        }

        //默认状态
        arr_titles[0].setEnabled(false);
        arr_titles[0].setBackgroundColor(getResources().getColor(R.color.white));
        arr_titles[0].setTextColor(getResources().getColor(R.color.color_icon));
        arr_lines[0].setEnabled(false);
        arr_lines[0].setBackgroundResource(R.drawable.store_on);

        myfav_data_loading_layout.setVisibility(View.GONE);

        initViewPager();
    }


    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        /**图册收藏fragment*/
        final MyFavImageFragment myFavImageFragment = new MyFavImageFragment();
        fragments.add(myFavImageFragment);

        /**公司收藏fragment*/
        final MyFavCompanyFragment myFavCompanyFragment = new MyFavCompanyFragment();
        fragments.add(myFavCompanyFragment);

        myfav_viewpager.setAdapter(new MyViewPagerAdapter(this.getSupportFragmentManager(), fragments));

        myfav_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for (int j = 0; j < arr_titles.length; j++) {
                    arr_titles[j].setEnabled(true);
                    arr_titles[j].setBackgroundColor(getResources().getColor(R.color.bg_title)); //灰色
                    arr_titles[j].setTextColor(getResources().getColor(R.color.color_black));
                    arr_lines[j].setBackgroundColor(getResources().getColor(R.color.bg_title)); //灰色

                    arr_lines[j].setEnabled(true);
                    arr_lines[j].setBackgroundResource(R.drawable.store_off);
                }

                arr_titles[position].setEnabled(false);
                arr_titles[position].setBackgroundColor(getResources().getColor(R.color.color_icon_bg_normal)); //白色
                arr_titles[position].setTextColor(getResources().getColor(R.color.color_icon)); //黄色

                arr_lines[position].setEnabled(false);
                arr_lines[position].setBackgroundResource(R.drawable.store_on);
                MyApplication.urrentItemFragment = position;
                fragmentPostion = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        tv_myfav_edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fragmentPostion == 0) {
                    // 图库收藏
//					Toast.makeText(mContext, isEdit?"111当前是-取消状态":"111当前是-编辑状态", Toast.LENGTH_SHORT).show();
                    if (isEdit == false) {
                        isEdit = true; //   true取消    <--   false编辑
                        //取消状态
                        tv_myfav_edit.setText("取消");
                        myfav_viewpager.setNoScrollView(isEdit);
                        delete_framelayout.setVisibility(View.VISIBLE);

                        // 显示勾选
                        myFavImageFragment.showImagesSelectedView();

//						Toast.makeText(mContext, "222显示勾选   当前是-取消状态"+isEdit, Toast.LENGTH_SHORT).show();
                    } else {
                        isEdit = false; //  false编辑    <--   true取消
                        // 编辑状态
                        tv_myfav_edit.setText("编辑");
                        myfav_viewpager.setNoScrollView(isEdit);
                        delete_framelayout.setVisibility(View.GONE);

                        //FIXME 让图库打钩消失
                        myFavImageFragment.imageSelectedGoDie();
//						Toast.makeText(mContext, "222隐藏勾选   当前是-编辑状态"+isEdit, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 公司收藏
                    if (isEdit == false) {
                        isEdit = true; //   true取消    <--   false编辑
                        //取消状态
                        tv_myfav_edit.setText("取消");
                        myfav_viewpager.setNoScrollView(isEdit);
                        delete_framelayout.setVisibility(View.VISIBLE);

                        // 显示勾选
                        myFavCompanyFragment.showCompanySelectedView();

                    } else {
                        isEdit = false; //  false编辑    <--   true取消
                        // 编辑状态
                        tv_myfav_edit.setText("编辑");
                        myfav_viewpager.setNoScrollView(isEdit);
                        delete_framelayout.setVisibility(View.GONE);

                        //FIXME 让公司打钩消失
                        myFavCompanyFragment.hideCompanySelectedView();
                    }


                }
            }
        });


        //删除按钮
        tv_delete_num.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fragmentPostion == 0) {
                    myFavImageFragment.deleteFavImages();
                } else if (fragmentPostion == 1) {
                    myFavCompanyFragment.deleteFavCompanies();
                }
                //按下之后恢复编辑状态
                isEdit = false;
                tv_myfav_edit.setVisibility(View.VISIBLE);
                tv_myfav_edit.setText("编辑");
                myfav_viewpager.setNoScrollView(isEdit);
                tv_delete_num.setVisibility(View.GONE);
                tv_delete_num.setText("");
            }

        });

    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }


    class DeleteBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DELETE_IMAGE_ACTION)) {
                Message msg = new Message();
                msg.what = 0x0000012;
                Bundle b = intent.getBundleExtra("delete_image_num_bundle");
                if (Integer.parseInt(b.getString("delete_image_num")) >= 0) {
                    msg.arg1 = Integer.parseInt(b.getString("delete_image_num"));
                } else {
                    msg.arg1 = 0;
                }
                deleteHandler.sendMessage(msg);
            } else if (intent.getAction().equals(DELETE_COMPANY_ACTION)) {
                Message msge = new Message();
                msge.what = 0x0000013;
                Bundle b = intent.getBundleExtra("delete_company_num_bundle");
                if (Integer.parseInt(b.getString("delete_company_num")) >= 0) {
                    msge.arg1 = Integer.parseInt(b.getString("delete_company_num"));
                } else {
                    msge.arg1 = 0;
                }
//				Toast.makeText(mContext, ""+msge.arg1, Toast.LENGTH_SHORT).show();
                deleteHandler.sendMessage(msge);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deleteReceiver != null) {
            unregisterReceiver(deleteReceiver);
            deleteReceiver = null;
        }
    }

}
