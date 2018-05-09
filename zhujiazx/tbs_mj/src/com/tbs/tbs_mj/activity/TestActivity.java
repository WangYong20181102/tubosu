package com.tbs.tbs_mj.activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.tbs.tbs_mj.R;

public class TestActivity extends BaseActivity {
    private Context mContext;
    private ImageView test_imageview;
    private String imageUrl = "https://opic.tbscache.com/manage/case/2015/07-07/small/32856fb1-b7a3-eb64-c1b6-756a010ba703.jpg";
    private ImageView testimageview;
    private android.widget.TextView tvNum0;
    private android.widget.TextView tvNum1;
    private android.widget.TextView tvNum2;
    private android.widget.TextView tvNum3;
    private android.widget.TextView tvNum4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.tvNum4 = (TextView) findViewById(R.id.tvNum4);
        this.tvNum3 = (TextView) findViewById(R.id.tvNum3);
        this.tvNum2 = (TextView) findViewById(R.id.tvNum2);
        this.tvNum1 = (TextView) findViewById(R.id.tvNum1);
        this.tvNum0 = (TextView) findViewById(R.id.tvNum0);
        this.testimageview = (ImageView) findViewById(R.id.test_imageview);

        mContext = this;
//        test_imageview = (ImageView) findViewById(R.id.test_imageview);
//        initView();
    }

//    private void initView() {
//        ImageLoaderUtil.loadImage(mContext, test_imageview, imageUrl);




//    }
}
