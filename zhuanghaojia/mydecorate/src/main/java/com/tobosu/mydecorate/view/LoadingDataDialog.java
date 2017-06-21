package com.tobosu.mydecorate.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

/**
 * Created by dec on 2016/11/10.
 */

public class LoadingDataDialog extends ProgressDialog {
//    private AnimationDrawable mAnimation;
    private Context mContext;
    private ProgressBar mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
//    private int count = 0;
//    private String oldLoadingTip;
//    private int mResid;

    public LoadingDataDialog(Context context/*, String content, int id*/) {
        super(context);
        this.mContext = context;
//        this.mLoadingTip = content;
//        this.mResid = id;
        setCanceledOnTouchOutside(true);
        initView();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
////        initData();
//    }

//    private void initData() {
//
//        mImageView.setBackgroundResource(mResid);
//        // 通过ImageView对象拿到背景显示的AnimationDrawable
//        mAnimation = (AnimationDrawable) mImageView.getBackground();
//        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
//        mImageView.post(new Runnable() {
//            @Override
//            public void run() {
//                mAnimation.start();
//            }
//        });
//        mLoadingTv.setText(mLoadingTip);
//
//    }
//
//    public void setContent(String str) {
//        mLoadingTv.setText(str);
//    }

    private void initView() {
        setContentView(R.layout.progress_dialog);
//        mLoadingTv.setText(mLoadingTip);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ProgressBar) findViewById(R.id.loadingIv);
        mImageView.setVisibility(View.VISIBLE);
    }
}
