package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbs.tbs_mj.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifActivity extends AppCompatActivity {
    @BindView(R.id.gif_ac_img)
    ImageView gifAcImg;
    private Context mContext;
    private String mGifPath = "http://img5.duitang.com/uploads/item/201411/29/20141129013744_UJEuu.gif";
//    private String mGifPath = "https://isparta.github.io/compare-webp/image/gif_webp/webp/1.webp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);
        mContext = this;
        Glide.with(mContext).load(mGifPath).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifAcImg);
//        Glide.with(mContext).load(mGifPath).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifAcImg);
    }
}
