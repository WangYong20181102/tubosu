package com.tbs.tbs_mj.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.fragment.ImageDetailFragment;
import com.tbs.tbs_mj.utils.AppInfoUtil;

public class ImageDetailNewActivity extends FragmentActivity {
    private Intent intent;
    private String TAG = "ImageDetailNewActivity";
    private Context mContext;
    private ImageDetailFragment imageDetailFragment;

    private String id = "";//上一个界面（imageNewActivity）传来的id
    private String url = "";//上一个界面（imageNewActivity）传来的url
    private String userid="";//本地获取id
    private String fav_conid = ""; //  图册id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_image_detail_new);
        mContext = this;
        init();
        initFragMent();
        initImageLoader();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.img_detail_fragment, imageDetailFragment)
                .commitAllowingStateLoss();

    }

    private void init() {
        intent = getIntent();
        id = intent.getStringExtra("id");
        url = intent.getStringExtra("url");
        fav_conid = intent.getStringExtra("fav_conid");
        userid=getSharedPreferences("userInfo", 0).getString("userid", "");
        Log.e(TAG, "上个界面传来的信息==" + id + "==" + url);
    }

    private void initFragMent() {
        if (imageDetailFragment == null) {
            imageDetailFragment = new ImageDetailFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("url", url);
        bundle.putString("userid", userid);
        bundle.putString("fav_conid", fav_conid);
        imageDetailFragment.setArguments(bundle);
    }

    //重新配置图片加载框架
    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                .discCacheFileCount(200) // 缓冲文件数目
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.单例ImageLoader类的初始化
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }
}
