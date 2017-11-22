package com.tbs.tobosutype.activity;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.PopAdapter;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.customview.TextSeekBar;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static com.tbs.tobosutype.utils.CacheManager.getOngoingStyle;

public class PopOrderActivity extends Activity {
    private static final String TAG = PopOrderActivity.class.getSimpleName();
    private static final String popOrderUrl = Constant.TOBOSU_URL + "tapi/order/pub_order";
    private static final String pipe_code = Constant.PIPE; // 渠道代码
    private Context mContext;

    private RelativeLayout relBar;
    private RelativeLayout relBack;
    private ImageView ivClose;


    // 选择类型
    private GridView gvStyle;
    private PopAdapter styleAdapter;
    private ArrayList<String> styleDatalist;
    private ArrayList<Boolean> isSelectedList;
    private TextView tv_style;

    // 选择面积
    private RelativeLayout relPrSquare;
    private GridView gvPrSquare;
    private PopAdapter squareAdapter;
    private ArrayList<String> squareDatalist;
    private TextView tv_prsquare;

    // 预算
    private GridView gvprBudge;
    private PopAdapter budgetAdapter;
    private ArrayList<String> budgetDatalist;
    private TextView tv_prbudget;


    // 准备装修最后
    private ImageView imageResult;
    private TextView tvPrepareFinalResult;
    private ImageView imagePrepareLeftUp;
    private TextView tvPrepreLeftUpText;
    private ImageView imagePrepareLeftDown;
    private TextView tvPrepareLeftDownText;
    private ImageView imagePrepareRight;
    private TextView tvPrepareRightText;
    private TextView tvCityLocation;
    private TextView tvSummitOrder;
    private EditText etCellphone;
    private RelativeLayout relDontNeed;


    //风格样式
    private PopAdapter ongoingStyleAdapter;
    private ArrayList<String> ongoingStyleDatalist;

    // 最后一页
    private LinearLayout llayout_going_final;
    private ImageView imageOngoningResult;
    private TextView tvOngoningFinalResult;
    private RelativeLayout relDontNeedOrder1;
    private TextView tvSummitOrder1;
    private EditText etCellphone1;
    private TextView tvCityLocation1;


    // 新增加
    private TextView tv_question_title;
    private RelativeLayout rel_left_gif;
    private RelativeLayout rel_right_gif;
    private ImageView gif_left;
    private ImageView gif_right;

    private TextView tv_prepare_title;
    private TextView tv_ongoing_title;

    private RelativeLayout rel_style;

    private RelativeLayout rel_prsquare;

    private RelativeLayout rel_mid_layout;
    private FrameLayout frameParepareFinal;
    private RelativeLayout rel_prbudget;
    private RelativeLayout rel_gif_layout;

    private RelativeLayout re_prepare_direction;
    private RelativeLayout re_ongoing_direction;



    // 装修阶段
    private RelativeLayout rl_going_stage;
    private TextSeekBar stage_seekbar;
    private String stage = "拆改";
    private TextView tvStage0, tvStage1, tvStage2, tvStage3, tvStage4, tvStage5;


    private RelativeLayout rl_going_style;
    private TextView tv_going_style;
    private GridView gv_going_style;


    private RelativeLayout rel_dont_care;
    private TextView tv_dont_care;

    private TextView tv_message;

    private LinearLayout ll_location;
    private LinearLayout ll_location1;

    /**先逛逛*/
    private TextView tv_xgg;


    private void initNewViews(){

        tv_question_title = (TextView) findViewById(R.id.tv_question_title);
        rel_left_gif = (RelativeLayout) findViewById(R.id.rel_left_gif);
        rel_right_gif = (RelativeLayout) findViewById(R.id.rel_right_gif);
        gif_left = (ImageView) findViewById(R.id.gif_left);

//        Glide.with(this).load(R.drawable.juse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(gif_left);

//        Glide.with(this).load(R.drawable.juse0225).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(gif_left);
        Glide.with(this).load(R.drawable.juse0225).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.juse0225).error(R.drawable.juse0225).into(gif_left);
//        gif_left.setBackgroundResource(R.drawable.juse022);

        gif_right = (ImageView) findViewById(R.id.gif_right);

//        Glide.with(this).load(R.drawable.lanse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(gif_right);
//        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(gif_right);
        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.lanse0225).error(R.drawable.lanse0225).into(gif_right);
//        gif_right.setBackgroundResource(R.drawable.lanse022);


        tv_prepare_title = (TextView) findViewById(R.id.tv_prepare_title);
        tv_ongoing_title = (TextView) findViewById(R.id.tv_ongoing_title);

        //进入 准备装修 风格
        gvStyle = (GridView) findViewById(R.id.gv_style);
        rel_style = (RelativeLayout) findViewById(R.id.rel_style);

        // 面积
        rel_prsquare = (RelativeLayout) findViewById(R.id.rel_prsquare);
        rel_mid_layout = (RelativeLayout) findViewById(R.id.rel_mid_layout);

        rel_gif_layout = (RelativeLayout) findViewById(R.id.rel_gif_layout);

        re_prepare_direction = (RelativeLayout) findViewById(R.id.re_prepare_direction);
        re_ongoing_direction = (RelativeLayout) findViewById(R.id.re_ongoing_direction);


        // 装修阶段
        rl_going_stage = (RelativeLayout) findViewById(R.id.rl_going_stage);
        stage_seekbar = (TextSeekBar) findViewById(R.id.stage_seekbar);
        tvStage0 = (TextView) findViewById(R.id.tv_stage_0);
//        tvStage0.setTextColor(Color.parseColor("#FE905F"));
        tvStage1 = (TextView) findViewById(R.id.tv_stage_1);
        tvStage2 = (TextView) findViewById(R.id.tv_stage_2);
        tvStage3 = (TextView) findViewById(R.id.tv_stage_3);
        tvStage4 = (TextView) findViewById(R.id.tv_stage_4);
        tvStage5 = (TextView) findViewById(R.id.tv_stage_5);

        rl_going_style = (RelativeLayout) findViewById(R.id.rl_going_style);

        gv_going_style = (GridView) findViewById(R.id.gv_going_style);
        tv_going_style = (TextView) findViewById(R.id.tv_going_style);

        rel_dont_care = (RelativeLayout) findViewById(R.id.rel_dont_care);
        tv_dont_care = (TextView) findViewById(R.id.tv_dont_care);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_message.bringToFront();
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_location1 = (LinearLayout) findViewById(R.id.ll_location1);
        tv_xgg = (TextView) findViewById(R.id.tv_xgg);
        tv_xgg.bringToFront();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_poporder);
        mContext = PopOrderActivity.this;

        initView();
        initNewViews();
        initNetData();
        setClickListener();

    }


    private void leftToMid(View view){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, 149.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", 0.0f, -130.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(813).start();
    }

    private void midToLeft(View view){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 149.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", -130.0f, 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(813).start();
    }

    private void rightToMid(View view){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, -149.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", 0.0f, -130.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(813).start();
    }

    private void midToRight(View view){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", -149.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", -130.0f, 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(813).start();
    }

    private CustomWaitDialog waitDialog;

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    private String setTextColor(int postion) {

        switch (postion) {
            case 0:
                tvStage0.setTextColor(Color.parseColor("#FE8F5B"));
                tvStage1.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage2.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage3.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage4.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage5.setTextColor(Color.parseColor("#CDAFA1"));
                stage = "拆改";
                return stage;
            case 1:
                tvStage0.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage1.setTextColor(Color.parseColor("#FE8F5B"));
                tvStage2.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage3.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage4.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage5.setTextColor(Color.parseColor("#CDAFA1"));
                stage = "水电";
                return stage;
            case 2:
                tvStage0.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage1.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage2.setTextColor(Color.parseColor("#FE8F5B"));
                tvStage3.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage4.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage5.setTextColor(Color.parseColor("#CDAFA1"));
                stage = "泥木";
                return stage;
            case 3:
                tvStage0.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage1.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage2.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage3.setTextColor(Color.parseColor("#FE8F5B"));
                tvStage4.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage5.setTextColor(Color.parseColor("#CDAFA1"));
                stage = "油漆";
                return stage;
            case 4:
                tvStage0.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage1.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage2.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage3.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage4.setTextColor(Color.parseColor("#FE8F5B"));
                tvStage5.setTextColor(Color.parseColor("#CDAFA1"));
                stage = "竣工";
                return stage;
            case 5:
                tvStage0.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage1.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage2.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage3.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage4.setTextColor(Color.parseColor("#CDAFA1"));
                tvStage5.setTextColor(Color.parseColor("#FE8F5B"));
                stage = "软装";
                return stage;
        }
//        System.out.println("---------------------->>" + stage);
//        return stage;
        return "";
    }

    private void initView() {

        CacheManager.setFistPopOrder(mContext, "not_first"); // 已经进入过发单页面了

        relBar = (RelativeLayout) findViewById(R.id.rel_bar);
        relBar.setVisibility(View.INVISIBLE);
        relBar.bringToFront();


        //----------------
        relBack = (RelativeLayout) findViewById(R.id.rel_poporder_back);
        ivClose = (ImageView) findViewById(R.id.iv_poporder_close);

        //面积
        relPrSquare = (RelativeLayout) findViewById(R.id.rel_prsquare);
        gvPrSquare = (GridView) findViewById(R.id.gv_prsquare);

        tv_prsquare = (TextView) findViewById(R.id.tv_prsquare);
        tv_prsquare.setVisibility(View.INVISIBLE);
        tv_style = (TextView) findViewById(R.id.tv_style);
        tv_style.setVisibility(View.INVISIBLE);
        tv_prbudget = (TextView)findViewById(R.id.tv_prbudget);
        tv_prbudget.setVisibility(View.INVISIBLE);

        // 预算
        rel_prbudget = (RelativeLayout) findViewById(R.id.rel_prbudget);
        gvprBudge = (GridView) findViewById(R.id.gv_prbudge);

        // prepare最后页面
        frameParepareFinal = (FrameLayout) findViewById(R.id.framelayout_prepare_final);
        frameParepareFinal.setVisibility(View.INVISIBLE);
//        relFinal = (RelativeLayout) findViewById(R.id.rel_final);
        imageResult = (ImageView) findViewById(R.id.image_result);

//        Glide.with(this).load(R.drawable.juse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(imageResult);
        Glide.with(this).load(R.drawable.juse0225).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageResult);


        tvPrepareFinalResult = (TextView) findViewById(R.id.tv_prepare_result);
//        tvPrepareFinalResult.setText("结果如何?");

        imagePrepareLeftUp = (ImageView) findViewById(R.id.image_prepare_left_up);
//        Glide.with(this).load(R.drawable.lanse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(imagePrepareLeftUp);
        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imagePrepareLeftUp);

        tvPrepreLeftUpText = (TextView) findViewById(R.id.tv_prepare_left_up_text);
        imagePrepareLeftDown = (ImageView) findViewById(R.id.image_prepare_left_down);
//        Glide.with(this).load(R.drawable.lanse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(imagePrepareLeftDown);
        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imagePrepareLeftDown);

        tvPrepareLeftDownText = (TextView) findViewById(R.id.tv_prepare_left_down_text);
        imagePrepareRight = (ImageView) findViewById(R.id.image_parepare_right);
//        Glide.with(this).load(R.drawable.lanse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(imagePrepareRight);
        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imagePrepareRight);

        tvPrepareRightText = (TextView) findViewById(R.id.tv_prepare_right_text);
        tvCityLocation = (TextView) findViewById(R.id.tv_city_location);
        if (!"".equals(CacheManager.getCity(mContext))) {
            tvCityLocation.setText(" 当前位置 " + CacheManager.getCity(mContext));
        }else {
            tvCityLocation.setText("定位失败 点击选择城市");
        }
        tvSummitOrder = (TextView) findViewById(R.id.tv_summit_order);
        relDontNeed = (RelativeLayout) findViewById(R.id.rel_dont_need_order);
        etCellphone = (EditText) findViewById(R.id.et_cellphone);
        etCellphone.clearFocus();


        // 最后一页
        llayout_going_final = (LinearLayout) findViewById(R.id.llayout_going_final);
        imageOngoningResult = (ImageView) findViewById(R.id.image_ongoning_result);
//        Glide.with(this).load(R.drawable.lanse0225)
//                .bitmapTransform(new BlurTransformation(PopOrderActivity.this, 25), new CropCircleTransformation(PopOrderActivity.this))
//                .into(imageOngoningResult);
        Glide.with(this).load(R.drawable.lanse0225).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageOngoningResult);

        tvOngoningFinalResult = (TextView) findViewById(R.id.tv_ongoning_final_result);
        relDontNeedOrder1 = (RelativeLayout) findViewById(R.id.rel_dont_need_order1);
        tvSummitOrder1 = (TextView) findViewById(R.id.tv_summit_order1);
        etCellphone1 = (EditText) findViewById(R.id.et_cellphone1);
        etCellphone1.clearFocus();
        tvCityLocation1 = (TextView) findViewById(R.id.tv_city_location1);
        if (!"".equals(CacheManager.getCity(mContext))) {
            tvCityLocation1.setText(" 当前位置 " + CacheManager.getCity(mContext));
        }else{
            tvCityLocation1.setText("定位失败 点击选择城市");
        }

    }


    private int gopoporder_code = -1;
    private void initNetData() {
        if(getIntent()!=null && getIntent().getBundleExtra("home_go_pop_bundle")!=null){
            gopoporder_code = getIntent().getBundleExtra("home_go_pop_bundle").getInt("gopoporder_code");
            CacheManager.setBecon(mContext, "0");
        }

    }

    private void setClickListener() {

        relBack.setOnClickListener(new View.OnClickListener() {
            // relBack1
            @Override
            public void onClick(View v) {
                if(pressBack()){
                    int position = Integer.parseInt(CacheManager.getBecon(mContext));
//                Util.setToast(mContext, "relBack1> " + position);
                    switch (position){
                        case 4:
                            backShow(3);
                            break;
                        case 3:
                            // 预算布局 往下走 离开
                            budgetAnimationOut(rel_prbudget);
                            break;
                        case 2:
                            prepareSquareAnimationOut(relPrSquare);// 面积往下走 离开
                            break;
                        case 1:
                            // 退出动画
                            prepareStyleAnimation_Out(rel_style);
                            rel_dont_care.setVisibility(View.INVISIBLE);
                            relBar.setVisibility(View.INVISIBLE);
                            tv_message.setVisibility(View.VISIBLE);
                            tv_question_title.setVisibility(View.VISIBLE);
                            tv_prepare_title.setText("准备装修");
                            tv_xgg.setVisibility(View.VISIBLE);
                            break;
                        case -1:
                            // 阶段退出
                            rel_dont_care.setVisibility(View.INVISIBLE);
                            stageAnimationOut1(rl_going_stage);
                            tv_message.setVisibility(View.VISIBLE);
                            tv_question_title.setVisibility(View.VISIBLE);
                            tv_xgg.setVisibility(View.VISIBLE);
                            break;
                        case -2:
                            //  风格出  阶段入
                            ongoingStyleOut(rl_going_style);
                            tv_dont_care.setText("不透露");
                            rel_dont_care.setVisibility(View.VISIBLE);

                            if(!"".equals(CacheManager.getStage(mContext))){
                                tv_ongoing_title.setText(CacheManager.getStage(mContext));
                            }
                            rel_dont_care.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    ongoingStageAnimationOut(rl_going_stage); // 风格离开
                                    initOngoingStyle(-2);
                                    tv_ongoing_title.setText("不限阶段");
                                }
                            });



//                        if(CacheManager.getStage(mContext).contains("不限")){
//                            setTextColor(0);
//                        }else{
//                            String s = CacheManager.getStage(mContext);
//                            System.out.print("s" +s);
//                        }

                            break;
                        case -3:
                            re_prepare_direction.setVisibility(View.INVISIBLE);
                            re_ongoing_direction.setVisibility(View.VISIBLE);
                            rel_mid_layout.setVisibility(View.VISIBLE);
                            rel_gif_layout.setVisibility(View.VISIBLE);
                            llayout_going_final.setVisibility(View.INVISIBLE);
                            tv_dont_care.setText("无所谓");
                            rel_dont_care.setVisibility(View.VISIBLE);
                            CacheManager.setBecon(mContext, "-2");

                            if(CacheManager.getOngoingStylePosition(mContext)==-1){
                                if(ongoingStyleAdapter!=null){
                                    ongoingStyleAdapter.setSelection(-1);
                                    ongoingStyleAdapter.notifyDataSetChanged();
                                    tv_ongoing_title.setText("不限风格");
                                    CacheManager.setOngoingStyle(mContext,"不限风格",-1);
                                }
                            }else{
                                tv_ongoing_title.setText(getOngoingStyle(mContext));
                            }

                            break;
                    }
                }else{
                    return;
                }

            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                if (!Util.isLogin(mContext)) {
//                    startActivity(new Intent(mContext, TransitActivity.class));
//                    Log.d(TAG, "沒登录进入提醒登录页");
//                } else {
//                    startActivity(new Intent(mContext, MainActivity.class));
//                    Log.d(TAG, "直接进入主页");
//                }
//                startActivity(new Intent(mContext, MainActivity.class));
                skip();
            }
        });


        tv_xgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        });

        rel_left_gif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setAnimation(rel_left_gif,rel_right_gif,"left_mid");
            }
        });

        rel_right_gif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setAnimation(rel_left_gif,rel_right_gif,"right_mid");
            }
        });


        tvSummitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCellPhone(etCellphone.getText().toString().trim())) {

                } else {
                    popOrder(etCellphone.getText().toString().trim(), CacheManager.getSquare(mContext), CacheManager.getBudget(mContext), CacheManager.getStyle(mContext));
                }
            }
        });

        tvSummitOrder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCellPhone(etCellphone1.getText().toString().trim())) {

                } else {
                    popOrder(etCellphone1.getText().toString().trim(), "", "", CacheManager.getOngoingStyle(mContext));
                }
            }
        });

        relDontNeed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (!Util.isLogin(mContext)) {
//                    startActivity(new Intent(mContext, TransitActivity.class));
//                    Log.d(TAG, "沒登录进入提醒登录页");
//                } else {
//                    startActivity(new Intent(mContext, MainActivity.class));
//                    Log.d(TAG, "直接进入主页");
//                }
//                startActivity(new Intent(mContext, MainActivity.class));
                notNow();
            }
        });

        relDontNeedOrder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!Util.isLogin(mContext)) {
//                    startActivity(new Intent(mContext, TransitActivity.class));
//                    Log.d(TAG, "沒登录进入提醒登录页");
//                } else {
//                    startActivity(new Intent(mContext, MainActivity.class));
//                    Log.d(TAG, "直接进入主页");
//                }
//                startActivity(new Intent(mContext, MainActivity.class));
//                finish();
                notNow();
            }
        });

        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheManager.setPageFlag(mContext, "pop_order");
                Intent cityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle b = new Bundle();
                b.putInt("frompop", 31);
                cityIntent.putExtra("pop_bundle", b);
                startActivityForResult(cityIntent, 123);
            }
        });
        ll_location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheManager.setPageFlag(mContext, "pop_order");
                Intent cityIntent = new Intent(mContext, SelectCtiyActivity.class);
                Bundle b = new Bundle();
                b.putInt("frompop", 31);
                cityIntent.putExtra("pop_bundle", b);
                startActivityForResult(cityIntent, 124);
            }
        });

    }


    private void hide(){

    }

    /***
     * 跳过
     */
    private void skip(){
        if("5".equals(getSharedPreferences("Go_PopOrderActivity_SP", Context.MODE_PRIVATE).getString("go_poporder_string", "0"))){
            startActivity(new Intent(PopOrderActivity.this, MainActivity.class));
//            getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).getInt("cancel_String", 10)==1
            getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit(); // 1代表是skip
        }
        CacheManager.setBecon(mContext, "0");
        notNow();
        finish();
    }

    /***
     * 暂时不领取
     */
    private void notNow(){
        //现在无视获取，则标记为 1 ，即在首再次显示让用户获取
        getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
        MobclickAgent.onEvent(mContext, "click_receive immediately_fail");
        CacheManager.setBecon(mContext, "0");
        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("first_install_string", "10");
        intent.putExtra("first_install_bundle", b);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 77:
                String city = data.getBundleExtra("city_bundle").getString("ci");
                CacheManager.setCity(mContext, city);
                tvCityLocation.setText(" 当前位置 " + city);
                tvCityLocation1.setText(" 当前位置 " + city);
                break;
        }
        if(Constant.HOMEFRAGMENT_REQUESTCODE==requestCode){
            fromActivity = 1;
            setResult(Constant.FINISH_MAINACTIVITY);
        }
    }

    private int fromActivity = -1;

    private boolean isCellPhone(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum) || "".equals(phoneNum)) {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
            Pattern pattern = Pattern.compile(MOBILE);
            Matcher matcher = pattern.matcher(phoneNum);
            boolean flag = matcher.matches();
            if (flag == false) {
                Toast.makeText(mContext, "请输入合法手机号码", Toast.LENGTH_SHORT).show();
            }
            return matcher.matches();
        }
    }

    private void initOngoingStage(int becon){
        rel_left_gif.setVisibility(View.INVISIBLE);
        relBar.setVisibility(View.VISIBLE);
        re_prepare_direction.setVisibility(View.INVISIBLE);
        re_ongoing_direction.setVisibility(View.VISIBLE);

        if (CacheManager.getStagePosition(mContext) == 0) {
            // 没选择过
//            CacheManager.setStage(mContext, "拆改", 0);
//            tv_ongoing_title.setText("拆改");
            CacheManager.setStage(mContext, "不限阶段", 0); // 还没选
        }
        tv_ongoing_title.setText("正在装修");

        stage_seekbar.setOnDragPositionListener(new TextSeekBar.OnDragPositionListener() {

            @Override
            public void onDragPositionListener(int position) {
                tv_ongoing_title.setText(setTextColor(position));
                stage_seekbar.setConnectingLineColor(Color.parseColor("#FF915D")); // 连接线颜色
                stage_seekbar.setConnectingLineWeight(21);
                // 保存下来 stage position
                CacheManager.setStage(mContext, stage, position); // 选择了
                // 退出动画
                ongoingStageAnimationOut(rl_going_stage);
                // 进入风格
                initOngoingStyle(-2);
            }
        });
        // 进入动画
        ongoingStageAnimation(rl_going_stage);
        CacheManager.setBecon(mContext, becon + "");
//        tv_ongoing_title.setText("正在装修");

        tv_dont_care.setText("不透露");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() { //OK
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(CacheManager.getBecon(mContext))==-1){
                    // 退出动画
                    ongoingStageAnimationOut(rl_going_stage);
                    // 进入风格
                    initOngoingStyle(-2);
                    // -2页面 还没选择风格 因此显示不限阶段
                    CacheManager.setStage(mContext, "不限阶段", 0);
                    tv_ongoing_title.setText("不限阶段"); // 还没选

                }else{
                    // 进入 -3
                    ongoingFinal(-3);
                    rel_dont_care.setVisibility(View.GONE);
                    CacheManager.setOngoingStyle(mContext, "不限风格", -1);
                    tvOngoningFinalResult.setText("不限风格");
                }

            }
        });

    }

    private void initDecorateStyle(int prepareStylePosition){
        rel_right_gif.setVisibility(View.INVISIBLE);
        relBar.setVisibility(View.VISIBLE);
        re_prepare_direction.setVisibility(View.VISIBLE);
        re_ongoing_direction.setVisibility(View.INVISIBLE);
        tv_style.setVisibility(View.VISIBLE);

        styleDatalist = new ArrayList<String>();
        styleDatalist.add("现代简欧");
        styleDatalist.add("田园");
        styleDatalist.add("地中海");
        styleDatalist.add("中式");
        styleDatalist.add("美式");
        styleDatalist.add("欧式");

        styleAdapter = new PopAdapter(mContext, styleDatalist);
        gvStyle.setAdapter(styleAdapter);

        if (CacheManager.getStylePosition(mContext) == 10) {
            styleAdapter.setSelection(-1); //没有选
            styleAdapter.notifyDataSetChanged();
        } else {
            styleAdapter.setSelection(CacheManager.getStylePosition(mContext));
        }

        styleAdapter.setSelectStyleListener(new PopAdapter.SelectStyleListener() {

            @Override
            public void onSelectItemListener(String style, int po) {
                if(CacheManager.getStylePosition(mContext)==-1){
                    tv_prepare_title.setText(style);

                    CacheManager.setStylePosition(mContext,style , po); // 同时保存style
                    styleAdapter.clearSelection(po);
                    styleAdapter.notifyDataSetChanged();

                    // 显示 面积布局
                    initDecorateSquare(2);
                }else{

                    if(style.equals(CacheManager.getStyle(mContext))){
                        // 已经选择过的 取消选择
                        CacheManager.setStylePosition(mContext, style , -1);  // 取消选择
                        styleAdapter.setSelection(-1);
                        styleAdapter.notifyDataSetChanged();
                        tv_prepare_title.setText("不限风格");
                    }else{
                        tv_prepare_title.setText(style);
                        CacheManager.setStylePosition(mContext, style, po);  // 选择了   sdf
                        styleAdapter.clearSelection(po);
                        styleAdapter.notifyDataSetChanged();
                        initDecorateSquare(2);
                    }

                }

            }
        });

        // 进入动画
        prepareStyleAnimation(rel_style);

        CacheManager.setPopWay(mContext, "prepare");
        CacheManager.setBecon(mContext, prepareStylePosition + "");

        //底部
        tv_dont_care.setText("无所谓");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入 2
                initDecorateSquare(2);
                tv_prepare_title.setVisibility(View.GONE);
                tv_prepare_title.setVisibility(View.VISIBLE);
                tv_prepare_title.setText("不限风格");
                CacheManager.setStylePosition(mContext,"不限风格",-1);
                if(styleAdapter!=null){
                    styleAdapter.setSelection(-1);
                    styleAdapter.notifyDataSetChanged();
                }

//                if("".equals(CacheManager.getStyle(mContext))){
//
//                }else{
//                    tv_prepare_title.setText(CacheManager.getStyle(mContext));
//                }
            }
        });

    }

    // 显示面积
    private void initDecorateSquare(int prepareSquarePosition){
        re_prepare_direction.setVisibility(View.VISIBLE);
        re_ongoing_direction.setVisibility(View.INVISIBLE);
        tv_prsquare.setVisibility(View.VISIBLE);
        // 面积
        squareDatalist = new ArrayList<String>();
        squareDatalist.add("60㎡");
        squareDatalist.add("90㎡");
        squareDatalist.add("120㎡");
        squareDatalist.add("150㎡");
        squareDatalist.add("180㎡");
        squareDatalist.add("210㎡");
        squareDatalist.add("300㎡");
        squareAdapter = new PopAdapter(mContext, squareDatalist/*, isSelectedList*/);
        gvPrSquare.setAdapter(squareAdapter);
        if (CacheManager.getSquarePosition(mContext) == 10) {
            squareAdapter.setSelection(-1);
            squareAdapter.notifyDataSetChanged();
        } else {
            squareAdapter.setSelection(CacheManager.getSquarePosition(mContext));
        }

        squareAdapter.setSelectStyleListener(new PopAdapter.SelectStyleListener() {
            @Override
            public void onSelectItemListener(String square, int po) {
                if(CacheManager.getSquarePosition(mContext)==-1){
                    tv_prepare_title.setText("不小于" + square);
                    CacheManager.setSquarePosition(mContext, "不小于"+square, po);
                    squareAdapter.clearSelection(po);
                    squareAdapter.notifyDataSetChanged();

                    // 装修预算动画
                    initBudge(3,true);
                }else{

                    if(("不小于"+square).equals(CacheManager.getSquare(mContext))){
                        // 已经选择过的 取消选择
                        CacheManager.setSquarePosition(mContext, "不限面积", -1);  // 取消选择
                        squareAdapter.setSelection(-1);
                        squareAdapter.notifyDataSetChanged();
                        tv_prepare_title.setText("不限面积");
                    }else{
                        tv_prepare_title.setText("不小于"+square);
                        CacheManager.setSquarePosition(mContext, "不小于"+square, po);  // 选择了   sdf
                        squareAdapter.clearSelection(po);
                        squareAdapter.notifyDataSetChanged();
                        initBudge(3, true);
                    }



                }
            }
        });

        CacheManager.setPopWay(mContext, "prepare");
        CacheManager.setBecon(mContext, prepareSquarePosition + "");

        prepareStyleAnimationOut(rel_style); // 风格离开

        // 底部
        tv_dont_care.setText("不透露");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBudge(3,true);
                if(squareAdapter!=null){
                    squareAdapter.setSelection(-1);
                    squareAdapter.notifyDataSetChanged();
                    tv_prepare_title.setText("不限面积");
                    CacheManager.setSquarePosition(mContext,"不限面积",-1);
                }
            }
        });
    }


    private void initBudge(final int prepareBudgetPosition, boolean isShowBudge){
        re_prepare_direction.setVisibility(View.VISIBLE);
        re_ongoing_direction.setVisibility(View.INVISIBLE);

        tv_prbudget.setVisibility(View.VISIBLE);
        if(isShowBudge){
            prepareBudgeAnimation(rel_prbudget);
        }

        budgetDatalist = new ArrayList<String>();
        budgetDatalist.add("3-5万");
        budgetDatalist.add("5-8万");
        budgetDatalist.add("8-15万");
        budgetDatalist.add("15万以上");
        budgetAdapter = new PopAdapter(mContext, budgetDatalist/*, isSelectedList*/);
        gvprBudge.setAdapter(budgetAdapter);
        if (CacheManager.getBudgetPosition(mContext) == 10) {
            budgetAdapter.setSelection(-1);
            budgetAdapter.notifyDataSetChanged();
        } else {
            budgetAdapter.setSelection(CacheManager.getBudgetPosition(mContext));
        }
        budgetAdapter.setSelectStyleListener(new PopAdapter.SelectStyleListener() {

            @Override
            public void onSelectItemListener(String budget, int po) {
                if(CacheManager.getBudgetPosition(mContext)==-1){
                    tv_prepare_title.setText(budget + "预算");
                    CacheManager.setBudgetPosition(mContext, budget + "预算",po);
                    budgetAdapter.clearSelection(po);
                    budgetAdapter.notifyDataSetChanged();

                    goFinal(4);
                }else{
                    if((budget + "预算").equals(CacheManager.getBudget(mContext))){
                        // 已经选择过的 取消选择
                        CacheManager.setBudgetPosition(mContext, "不限预算", -1);  // 取消选择
                        budgetAdapter.setSelection(-1);
                        budgetAdapter.notifyDataSetChanged();
                        tv_prepare_title.setText("不限预算");
                    }else{
                        tv_prepare_title.setText(budget + "预算");
                        CacheManager.setBudgetPosition(mContext, budget + "预算",po);
                        budgetAdapter.clearSelection(po);
                        budgetAdapter.notifyDataSetChanged();

                        goFinal(4);
                    }
                }
            }
        });

        CacheManager.setPopWay(mContext, "prepare");
        CacheManager.setBecon(mContext, prepareBudgetPosition + "");


        // 底部
        tv_dont_care.setText("有钱任性");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                rel_dont_care.setVisibility(View.GONE);
                CacheManager.setBudgetPosition(mContext, "不限预算", -1);
                tv_prepare_title.setText("不限预算");
                if(budgetAdapter!=null){
                    budgetAdapter.setSelection(-1);
                    budgetAdapter.notifyDataSetChanged();
                }
                goFinal(4);// 进入 4
            }
        });
    }


    private void goFinal(int prepareFinal){
        rel_dont_care.setVisibility(View.GONE);
        rel_mid_layout.setVisibility(View.INVISIBLE);
        frameParepareFinal.setVisibility(View.VISIBLE);
        rel_gif_layout.setVisibility(View.INVISIBLE);

        tvPrepreLeftUpText.setText(CacheManager.getStyle(mContext));
        tvPrepareLeftDownText.setText(CacheManager.getSquare(mContext));
        tvPrepareRightText.setText(CacheManager.getBudget(mContext));

        CacheManager.setPopWay(mContext, "prepare");
        CacheManager.setBecon(mContext, "4");

//        System.out.println("-----result-->>>" + CacheManager.getStyle(mContext) + "---" + CacheManager.getSquare(mContext) + "--"
//                + CacheManager.getBudget(mContext) + "--" + etCellphone.getText().toString().trim());
    }

    private void ongoingFinal(int becon){
        rel_dont_care.setVisibility(View.GONE);
        rel_mid_layout.setVisibility(View.INVISIBLE);
        llayout_going_final.setVisibility(View.VISIBLE);
        rel_gif_layout.setVisibility(View.INVISIBLE);

        if(!"".equals(getOngoingStyle(mContext))){
            tvOngoningFinalResult.setText(getOngoingStyle(mContext));
        }else{
            tvOngoningFinalResult.setText("不限风格");
        }

        CacheManager.setBecon(mContext, becon + "");
    }


    // 显示-2
    private void initOngoingStyle(int becon){
        re_prepare_direction.setVisibility(View.INVISIBLE);
        re_ongoing_direction.setVisibility(View.VISIBLE);

        ongoingStyleDatalist = new ArrayList<String>();
        if (ongoingStyleDatalist.size() == 0) {
            ongoingStyleDatalist.add("现代简欧");
            ongoingStyleDatalist.add("田园");
            ongoingStyleDatalist.add("地中海");
            ongoingStyleDatalist.add("中式");
            ongoingStyleDatalist.add("美式");
            ongoingStyleDatalist.add("欧式");
        }
        ongoingStyleAdapter = new PopAdapter(mContext, ongoingStyleDatalist);
        gv_going_style.setAdapter(ongoingStyleAdapter);
        if ("".equals(getOngoingStyle(mContext))) {
            ongoingStyleAdapter.setSelection(-1);
            ongoingStyleAdapter.notifyDataSetChanged();
        } else {
            ongoingStyleAdapter.setSelection(CacheManager.getOngoingStylePosition(mContext));
        }


        ongoingStyleAdapter.setSelectStyleListener(new PopAdapter.SelectStyleListener() {
            @Override
            public void onSelectItemListener(String style, int po) {
                if(CacheManager.getOngoingStylePosition(mContext)==-1){
                    // 还没选择过的
                    tv_ongoing_title.setText(style);
                    CacheManager.setOngoingStyle(mContext, style, po);  // 选择了
                    ongoingStyleAdapter.clearSelection(po);
                    ongoingStyleAdapter.notifyDataSetChanged();

                    ongoingFinal(-3);

                }else{

                    if(style.equals(getOngoingStyle(mContext))){
                        // 已经选择过的 取消选择
                        CacheManager.setOngoingStyle(mContext, style, -1);  // 取消选择
                        ongoingStyleAdapter.setSelection(-1);
                        ongoingStyleAdapter.notifyDataSetChanged();
                        tv_ongoing_title.setText("不限风格");
                    }else{
                        tv_ongoing_title.setText(style);
                        CacheManager.setOngoingStyle(mContext, style, po);  // 选择了
                        ongoingStyleAdapter.clearSelection(po);
                        ongoingStyleAdapter.notifyDataSetChanged();
                        ongoingFinal(-3);
                    }


                }


            }
        });

        CacheManager.setBecon(mContext, becon + "");

        // 底部
        tv_dont_care.setText("无所谓");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Integer.parseInt(CacheManager.getBecon(mContext))==-1){
                    initOngoingStyle(-2);
                    CacheManager.setStage(mContext, "不限阶段", 0);
                    tv_ongoing_title.setText("不限阶段"); // 还没选
                }else{
                    // 进入 -3
                    ongoingFinal(-3);
                    rel_dont_care.setVisibility(View.GONE);
                    CacheManager.setOngoingStyle(mContext, "不限风格", -1);
                    tvOngoningFinalResult.setText("不限风格");
                }

            }
        });

    }

    private long exitTime = 0;
    public boolean pressBack() {
        if(System.currentTimeMillis() - exitTime > 1000) {
            exitTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /*if(keyCode==KeyEvent.KEYCODE_BACK && getSharedPreferences("From_Home_Activity", Context.MODE_PRIVATE).getInt("home_activity_int", 0)==3){
            // 从首页过来
            finish();
            return true;
        }else */

        if(pressBack()){
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                int position = 0;
                if("".equals(CacheManager.getBecon(mContext))){
                    position = 0;
                }else{
                    position = Integer.parseInt(CacheManager.getBecon(mContext));
                }

//            Util.setToast(mContext, "onKeyDown> " + position);
                if(position==4){
                    // 隐藏 最后一页
                    backShow(3);
                    relBar.setVisibility(View.VISIBLE);
                    return true;
                }else if(position==3){
                    // 预算布局 往下走 离开
                    budgetAnimationOut(rel_prbudget);
                    relBar.setVisibility(View.VISIBLE);

                    if(CacheManager.getSquare(mContext).contains("不限")){
                        if(squareAdapter!=null){
                            squareAdapter.setSelection(-1);
                            squareAdapter.notifyDataSetChanged();
                        }
                    }
                    return true;
                }else if(position==2){
                    prepareSquareAnimationOut(relPrSquare);// 面积往下走 离开  风格进入
                    relBar.setVisibility(View.VISIBLE);

                    if(CacheManager.getStyle(mContext).contains("不限")){
                        if(styleAdapter!=null){
                            styleAdapter.setSelection(-1);
                            styleAdapter.notifyDataSetChanged();
                        }
                    }
                    return true;
                }else if(position==1){
                    // 退出动画
                    prepareStyleAnimation_Out(rel_style);
                    rel_dont_care.setVisibility(View.INVISIBLE);
                    relBar.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_question_title.setVisibility(View.VISIBLE);
                    tv_xgg.setVisibility(View.VISIBLE);
                }else if(position==0){
                    if(gopoporder_code==34){
                        setResult(Constant.POP_RESULTCODE);
                        startActivity(new Intent(mContext, MainActivity.class));
                    }

                    if(16==CacheManager.getPopFlag(mContext)){
                        // 已经发过单
                    }else{
                        // 默认==19 未发过单
                        // 保证首页能够出现发单按钮
                        getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
                    }
                    notNow();
                    finish();
                    return true;
                }else if(position==-1){
                    // 阶段退出
                    rel_dont_care.setVisibility(View.INVISIBLE);
                    stageAnimationOut1(rl_going_stage);
                    relBar.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_question_title.setVisibility(View.VISIBLE);
                    tv_xgg.setVisibility(View.VISIBLE);
                }else if(position==-2){
                    //  风格出  阶段入
                    ongoingStyleOut(rl_going_style);
                    tv_dont_care.setText("不透露");
                    rel_dont_care.setVisibility(View.VISIBLE);
                    if(!"".equals(CacheManager.getStage(mContext))){
                        tv_ongoing_title.setText(CacheManager.getStage(mContext));
                    }
//                tv_ongoing_title.setText(setTextColor(0));
                    rel_dont_care.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 风格入
//                        initOngoingStage(-2);
                            ongoingStageAnimationOut(rl_going_stage); // 风格离开
                            initOngoingStyle(-2);
                        }
                    });
                    relBar.setVisibility(View.VISIBLE);
                    return true;
                }else if(position==-3){
                    relBar.setVisibility(View.VISIBLE);
                    re_prepare_direction.setVisibility(View.INVISIBLE);
                    re_ongoing_direction.setVisibility(View.VISIBLE);
                    rel_mid_layout.setVisibility(View.VISIBLE);
                    rel_gif_layout.setVisibility(View.VISIBLE);
                    llayout_going_final.setVisibility(View.INVISIBLE);

                    tv_dont_care.setText("无所谓");
                    rel_dont_care.setVisibility(View.VISIBLE);


                    CacheManager.setBecon(mContext, "-2");

                    if(CacheManager.getOngoingStylePosition(mContext)==-1){
                        if(ongoingStyleAdapter!=null){
                            ongoingStyleAdapter.setSelection(-1);
                            ongoingStyleAdapter.notifyDataSetChanged();
                            tv_ongoing_title.setText("不限风格");
                            CacheManager.setOngoingStyle(mContext,"不限风格",-1);
                        }
                    }else{
                        tv_ongoing_title.setText(getOngoingStyle(mContext));
                    }

                    return true;
                }
                return true;
            }
        }else{
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void backShow(int po){
        switch (po){
            case 3:
                frameParepareFinal.setVisibility(View.INVISIBLE);
                rel_gif_layout.setVisibility(View.VISIBLE);
                rel_mid_layout.setVisibility(View.VISIBLE);
                re_prepare_direction.setVisibility(View.VISIBLE);

                // 显示title, 面积布局, 预算布局
                tv_prepare_title.setVisibility(View.VISIBLE);
                re_prepare_direction.setVisibility(View.VISIBLE);
                tv_prsquare.setVisibility(View.VISIBLE);
                tv_style.setVisibility(View.VISIBLE);
                tv_prbudget.setVisibility(View.VISIBLE);

                CacheManager.setBecon(mContext, po+"");
                tv_dont_care.setText("有钱任性");
                rel_dont_care.setVisibility(View.VISIBLE);

                tv_prepare_title.setText(CacheManager.getBudget(mContext));
                if(CacheManager.getBudget(mContext).contains("不限")){
//                    tv_prepare_title.setText(CacheManager.getBudget(mContext));
                    if(budgetAdapter!=null){
                        budgetAdapter.setSelection(-1);
                        budgetAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }


    private void popOrder(final String cellphone, final String housearea, final String orderprice, final String style) {
        showLoadingView();
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("cellphone", cellphone); // 电话号码
            hashMap.put("housearea", housearea); // 面积
            hashMap.put("orderprice", orderprice); // 预算
            hashMap.put("style", style); // 风格
            hashMap.put("device", "android");
            hashMap.put("source", "942");
            hashMap.put("city", CacheManager.getCity(mContext));
            hashMap.put("version", AppInfoUtil.getAppVersionName(mContext));
            hashMap.put("urlhistory", Constant.PIPE); // 渠道代码
            hashMap.put("comeurl", Constant.PIPE); //订单发布页面
            OKHttpUtil.post(popOrderUrl, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(mContext, "领取失败 请稍后再试");
                            hideLoadingView();
                            notNow();
                            Util.setErrorLog(TAG, "===引导发单结果失败了");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    Util.setErrorLog(TAG, "===引导发单结果=>>>"+s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject orderObject = new JSONObject(s);
                                if (orderObject.getInt("error_code") == 0) {
                                    hideLoadingView();
                                    Util.setToast(mContext, "领取成功 我们会尽快以0574开头座机联系您");


                                    if("5".equals(getSharedPreferences("Go_ChooseActivity_SP", Context.MODE_PRIVATE).getString("go_chooseStyle_string", "0"))){
                                        Intent intent = new Intent();
                                        intent.setClass(mContext, MainActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("first_install_string", "11");
                                        intent.putExtra("first_install_bundle", b);
                                        startActivity(intent);
                                    }
                                    //现在获取了，则标记为 0 ，即在首不再显示让用户获取
                                    getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 0).commit();


                        /*这里标记是为了控制 是否执行过下面这句
                          getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
                        */
                                    CacheManager.setPopFlag(mContext, 16);
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    finish();
                                } else if (orderObject.getInt("error_code") == 250) {
                                    hideLoadingView();
                                    notNow();
                                    Util.setToast(mContext, "您操作太频繁 请稍后再试!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });
        }
    }


    private void setAnimation(final View left, final View right, String flag){

        if("left_mid".equals(flag)){
            right.setAlpha(0);
            tv_message.setVisibility(View.INVISIBLE);
            rel_dont_care.setVisibility(View.VISIBLE);
            tv_xgg.setVisibility(View.INVISIBLE);
            leftToMid(left);
            right.setVisibility(View.GONE);
            right.setAlpha(0.0f);
            tv_question_title.setVisibility(View.INVISIBLE);
            left.setClickable(false);
            initDecorateStyle(1);


        }else if("right_mid".equals(flag)){
            left.setAlpha(0);
            tv_message.setVisibility(View.INVISIBLE);
//            tv_message.setAlpha(0.0f);
            rel_dont_care.setVisibility(View.VISIBLE);
            tv_xgg.setVisibility(View.INVISIBLE);
            rightToMid(right);
            left.setVisibility(View.GONE);
            left.setAlpha(0.0f);
            tv_question_title.setVisibility(View.INVISIBLE);
            right.setClickable(false);
            initOngoingStage(-1);

        }else if("left_back".equals(flag)){
            relBar.setVisibility(View.INVISIBLE);
            rel_dont_care.setVisibility(View.INVISIBLE);
//            tv_message.setAlpha(1.0f);
            tv_prepare_title.setText("准备装修");
            midToLeft(left);
            right.setVisibility(View.VISIBLE);
            right.setAlpha(1.0f);
            left.setClickable(true);
            tv_question_title.setVisibility(View.VISIBLE);


        }else if("right_back".equals(flag)){
            relBar.setVisibility(View.INVISIBLE);
            rel_dont_care.setVisibility(View.INVISIBLE);
//            tv_message.setAlpha(1.0f);
            tv_ongoing_title.setText("正在装修");
            midToRight(right);
            left.setVisibility(View.VISIBLE);
            left.setAlpha(1.0f);
            right.setClickable(true);
            tv_question_title.setVisibility(View.VISIBLE);
        }
    }

    //装修类型进入动画
    private void prepareStyleAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                gif_left.setBackgroundResource(R.drawable.juse022);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
    }



    //装修类型离开动画
    private void prepareStyleAnimationOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                prepareSquareAnimation(rel_prsquare);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);
    }




    //装修面积进入动画
    private void prepareSquareAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
    }

    //装修预算进入动画
    private void prepareBudgeAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
    }


    //装修阶段进入动画
    private void ongoingStageAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
    }

    //装修类型离开动画
    private void ongoingStageAnimationOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                prepareSquareAnimation(rl_going_style);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);
    }


    //装修预算离开动画
    private void budgetAnimationOut(final View view){ //keydown 3
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);

        // 标题也要变化
        tv_prepare_title.setText(CacheManager.getSquare(mContext));
        CacheManager.setBecon(mContext, "2");
        tv_dont_care.setText("不透露");
        rel_dont_care.setVisibility(View.VISIBLE);
        rel_dont_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 预算出现
                initBudge(3,true);
            }
        });
    }


    private void prepareSquareAnimationOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
                // 显示 类型
                goBackDecorateStyle();
            }
        });
        view.startAnimation(animation);
        tv_prepare_title.setText(CacheManager.getStyle(mContext));
        tv_dont_care.setText("无所谓");
        CacheManager.setBecon(mContext, "1");
        rel_dont_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入面积
                initDecorateSquare(2);
            }
        });
    }


    //装修面积离开动画
    private void squareAnimationOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);

                // 显示风格
                rel_mid_layout.setVisibility(View.VISIBLE);
                rel_style.setVisibility(View.VISIBLE);
                re_prepare_direction.setVisibility(View.VISIBLE);
                re_ongoing_direction.setVisibility(View.INVISIBLE);
                tv_style.setVisibility(View.VISIBLE);


            }
        });
        view.startAnimation(animation);
        goBackDecorateStyle();
    }

    private void goBackDecorateStyle(){
        styleDatalist = new ArrayList<String>();
        styleDatalist.add("现代简欧");
        styleDatalist.add("田园");
        styleDatalist.add("地中海");
        styleDatalist.add("中式");
        styleDatalist.add("美式");
        styleDatalist.add("欧式");

//        isSelectedList = new ArrayList<Boolean>();
//        isSelectedList.clear();
//        for (int i = 0; i < styleDatalist.size(); i++) {
//            this.isSelectedList.add(false);
//        } 雷区 FIXME

        styleAdapter = new PopAdapter(mContext, styleDatalist);
        gvStyle.setAdapter(styleAdapter);

        if (CacheManager.getStylePosition(mContext) == 10) {
            styleAdapter.setSelection(-1);
            styleAdapter.notifyDataSetChanged();
        } else {
            styleAdapter.setSelection(CacheManager.getStylePosition(mContext));
        }

        styleAdapter.setSelectStyleListener(new PopAdapter.SelectStyleListener() {

            @Override
            public void onSelectItemListener(String style, int po) {
                if(CacheManager.getStylePosition(mContext)==-1){
                    tv_prepare_title.setText(style);
//                CacheManager.setStyle(mContext, style+"风");
                    CacheManager.setStylePosition(mContext, style, po);
                    styleAdapter.clearSelection(po);
                    styleAdapter.notifyDataSetChanged();
                    // 显示面积
                    initDecorateSquare(2);
                }else{
                    if(style.equals(CacheManager.getStyle(mContext))){
                        // 已经选择过的 取消选择
                        CacheManager.setStylePosition(mContext, "不限风格", -1);  // 取消选择
                        styleAdapter.setSelection(-1);
                        styleAdapter.notifyDataSetChanged();
                        tv_prepare_title.setText("不限风格");
                    }else{
                        tv_prepare_title.setText(style);
                        CacheManager.setStylePosition(mContext, style, po);  // 选择了   sdf
                        styleAdapter.clearSelection(po);
                        styleAdapter.notifyDataSetChanged();
                        initDecorateSquare(2);
                    }
                }
            }
        });

        prepareStyleAnimationIn(rel_style);
    }

    private void prepareStyleAnimation_Out(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 动态图恢复 FIXME
                setAnimation(rel_left_gif,rel_right_gif,"left_back");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);

                CacheManager.setBecon(mContext, "0");
            }
        });
        view.startAnimation(animation);
    }

    private void prepareStyleAnimationIn(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
        CacheManager.setBecon(mContext, "1");
    }


    private void stageAnimationOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimation(rel_left_gif,rel_right_gif,"right_back");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);

                rel_gif_layout.setVisibility(View.VISIBLE);
                rel_mid_layout.setVisibility(View.VISIBLE);

                // 底部
                tv_dont_care.setText("不透露");
                rel_dont_care.setVisibility(View.VISIBLE);
                rel_dont_care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 进入 -2
                        initOngoingStyle(-2);
                    }
                });

            }
        });

        view.startAnimation(animation);


        CacheManager.setBecon(mContext, "0");
    }

    private void stageAnimationOut1(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimation(rel_left_gif,rel_right_gif,"right_back");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
                rel_gif_layout.setVisibility(View.VISIBLE);
                rel_mid_layout.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(animation);
        CacheManager.setBecon(mContext, "0");
    }


    private void ongoingStyleOut(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);

                // 显示stage
                goingStageIn(rl_going_stage);
            }
        });
        view.startAnimation(animation);

        CacheManager.setBecon(mContext, "-1");
    }

    private void ongoingStyleIn(final View view){
        rl_going_stage.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
        CacheManager.setBecon(mContext, "-2");

        rel_right_gif.setVisibility(View.VISIBLE);
        tv_ongoing_title.setVisibility(View.VISIBLE);
        tv_ongoing_title.setText("-trfhyfhdfhtdf");
    }


    private void goingStageIn(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
//                rel_right_gif

            }
        });
        view.startAnimation(animation);
        CacheManager.setBecon(mContext, "-1");
    }

    private void ongoingStyleAnimationIn(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);

                // 显示阶段 上到下
                ongoingStageAnimationIn(rl_going_stage);
            }
        });
        view.startAnimation(animation);
    }

    private void ongoingStageAnimationIn(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_up_to_down);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.startAnimation(animation);
        CacheManager.setBecon(mContext, "0");
    }


}
