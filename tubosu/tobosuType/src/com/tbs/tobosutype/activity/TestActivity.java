package com.tbs.tobosutype.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends BaseActivity {


    @BindView(R.id.meizu_banner)
    MZBannerView meizuBanner;
    @BindView(R.id.meizu_view)
    View meizuView;

    private Context mContext;
    private ArrayList<String> mImgList = new ArrayList<>();
    private String TAG = "CardViewActivity";
    private List<Color> colorList = new ArrayList<>();
    private int mColor1;
    private int mColor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        meizuBanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        meizuBanner.pause();
    }

    private void initView() {
        mColor1 = Color.parseColor("#ff882e");
        mColor2 = Color.BLUE;
        meizuView.setBackgroundColor(mColor1);
        mImgList.add("http://a.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd90df1ede83401213fb80e9127.jpg");
        mImgList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1094986098,2549356376&fm=26&gp=0.jpg");
        mImgList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=752453405,3701374730&fm=26&gp=0.jpg");
        mImgList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=686574148,3013055798&fm=26&gp=0.jpg");
        mImgList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4049201283,433568428&fm=26&gp=0.jpg");
//        colorList.add(Color.argb())
//        Glide.with(mContext)
//                .load(mImgList.get(0))
//                .transform(new FastBlur(mContext, 100))
//                .into(meizuImage);
        initBanner();
    }

    private void initBanner() {
        meizuBanner.setBannerPageClickListener(bannerPageClickListener);
        meizuBanner.addPageChangeListener(onPageChangeListener);
        meizuBanner.setPages(mImgList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });


        meizuBanner.setIndicatorVisible(false);
    }


    private MZBannerView.BannerPageClickListener bannerPageClickListener = new MZBannerView.BannerPageClickListener() {
        @Override
        public void onPageClick(View view, int i) {
            Toast.makeText(mContext, "点击了第" + i + "个图片", Toast.LENGTH_SHORT).show();
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "banner轮播==========" + position);
//            Glide.with(mContext)
//                    .load(mImgList.get(position))
//                    .transform(new FastBlur(mContext, 100))
//                    .into(meizuImage);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static class BannerViewHolder implements MZViewHolder<String> {


        private ImageView testBannerItemImage;
        private TextView test_banner_item_text;


        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.a_test_banner_item, null);
            testBannerItemImage = view.findViewById(R.id.test_banner_item_image);
            test_banner_item_text = view.findViewById(R.id.test_banner_item_text);
            return view;
        }

        @Override
        public void onBind(Context context, int i, String s) {
            Glide.with(context).load(s).into(testBannerItemImage);
        }
    }

}
