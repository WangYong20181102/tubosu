package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.SpUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户第一次使用我们的App时的启动页
 * 主要放置一些滑动切片 利用viewpager 作为切片的载体
 */
public class FristActivity extends AppCompatActivity {
    @BindView(R.id.frist_viewpager)
    ViewPager fristViewpager; //
    private String TAG = "FristActivity";
    private Context mContext;
    private int[] ids = new int[]{R.mipmap.img01, R.mipmap.img02, R.mipmap.img03, R.mipmap.img04};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
        ButterKnife.bind(this);
        mContext = this;

        initViewPager();
    }

    private void initViewPager(){
        SpUtils.setUserIsFristLogin(mContext, "cao");
        final ArrayList<ImageView> imgList = new ArrayList<ImageView>();
        for(int i=0;i<ids.length;i++){
            ImageView iv = new ImageView(this);
//            iv.setImageResource(ids[i]);
            Glide.with(this).load(ids[i]).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
            imgList.add(iv);
        }

        ImgPageAdapter adapter = new ImgPageAdapter(mContext, imgList);
        fristViewpager.setAdapter(adapter);
        fristViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fristViewpager.setCurrentItem(position);
                if(position == ids.length - 1){
                    imgList.get(position).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(FristActivity.this, MainActivity.class));
                            FristActivity.this.finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    class ImgPageAdapter extends PagerAdapter{
        Context context;
        ArrayList<ImageView> dataList;

        public ImgPageAdapter(Context context, ArrayList<ImageView> dataList){
            this.context = context;
            this.dataList = dataList;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(dataList.get(position));
            return dataList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(dataList.get(position));
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
