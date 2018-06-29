package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.DensityUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.tobosu.mydecorate.view.ObservableScrollView;
import com.tobosu.mydecorate.view.TipDialog1;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class GetPriceActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = GetPriceActivity.class.getSimpleName();

    private final int[] imgIdList = {R.mipmap.vp1, R.mipmap.vp2, R.mipmap.vp3};


    private ImageView[] tips;
    private ImageView[] mImageViews;

    private Context mContext;
    private RelativeLayout relBack;
    private ObservableScrollView scrollview;
    private ImageView ivGotop;
    private EditText etName;
    private EditText etPhone;
    private RelativeLayout relChooseCity;
    private TextView tvCity;
    private String city;

    private TextView tvSummit;

    private TextView tvCheatTextView;

    private ViewPager vpGetpriceImg;
    private ViewGroup group;


    private String phone;
    private String cityid;
    private String userid;
    private String budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getprice);
        mContext = GetPriceActivity.this;

        initView();
        initEvent();
        initViewpagerAdapter();
    }

    private void initView() {
        relBack = (RelativeLayout) findViewById(R.id.rel_getprice_back);
        ivGotop = (ImageView) findViewById(R.id.iv_gotop);
        scrollview = (ObservableScrollView) findViewById(R.id.scrollview_getprice);
        vpGetpriceImg = (ViewPager) findViewById(R.id.vp_getprice_img);
        group = (ViewGroup) findViewById(R.id.dot_view_group);
        etName = (EditText) findViewById(R.id.getprice_nam1e);
        etPhone = (EditText) findViewById(R.id.getprice_phon1e);
        tvCity = (TextView) findViewById(R.id.tv_getprice_city);
        relChooseCity = (RelativeLayout) findViewById(R.id.rel_choose_city);
        tvSummit = (TextView) findViewById(R.id.getprice_submit);
        tvCheatTextView = (TextView) findViewById(R.id.cheat_textview);

        scrollview.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y <= DensityUtil.dip2px(mContext, 371)) {
                    ivGotop.setVisibility(View.GONE);
                } else {
                    ivGotop.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void cheatTextAnimation() {
        Animation animation = AnimationUtils.loadAnimation(GetPriceActivity.this, R.anim.textview_left_out);
        tvCheatTextView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvCheatTextView.setText(getCheatText());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cheatTextAnimation();
                tvCheatTextView.setText(getCheatText());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private String getCheatText() {
        String text = "";
        int po = (int) (1 + Math.random() * (14));
        int name = (int) (1 + Math.random() * (170));
        text = cityText[po] + "的" + cheat[name] + cheatText[po % 2] + getSecondes() + "秒前";
        return text;
    }

    private String[] cheatText = {"先生已经获取1份报价 ", "女士已经获取1份报价 "};

    private int getSecondes() {
        return (int) (1 + Math.random() * (58));
    }

    private String[] cityText = {"武汉", "上海", "北京", "深圳", "南昌", "厦门", "湖南", "武汉", "广州", "合肥", "上海", "南京", "南宁", "石家", "天津", "乌鲁木齐"};
    private String[] cheat =
            {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                    "何", "吕", "施", "张", "孔", "曹", "严", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章",
                    "云", "苏", "潘", "葛", "奚", "范", "彭", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
                    "酆", "鲍", "史", "唐", "费", "廉", "岑", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
                    "乐", "于", "时", "傅", "皮", "卞", "齐", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹",
                    "姚", "邵", "湛", "汪", "祁", "毛", "禹", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞",
                    "熊", "纪", "舒", "屈", "项", "祝", "董", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危",
                    "江", "童", "颜", "郭", "梅", "盛", "林", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
                    "虞", "万", "支", "柯", "昝", "管", "卢", "经", "房", "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓"};

    @Override
    protected void onResume() {
        super.onResume();
        cheatTextAnimation();
    }


    private void initEvent() {
        tvSummit.setOnClickListener(this);
        relBack.setOnClickListener(this);
        relChooseCity.setOnClickListener(this);
        ivGotop.setOnClickListener(this);

//        hideEdittext(etName);
//        hideEdittext(etPhone);
//        etName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etName.setFocusable(true);
//                etName.requestFocus();
//                return true;
//            }
//        });
//        etPhone.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etPhone.setFocusable(true);
//                etPhone.requestFocus();
//                return true;
//            }
//        });

    }

    private void initViewpagerAdapter() {
        //将点点加入到ViewGroup中
        tips = new ImageView[imgIdList.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.dot_on);
            } else {
                tips[i].setBackgroundResource(R.mipmap.dot_off);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }


        //将图片装载到数组中
        mImageViews = new ImageView[imgIdList.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdList[i]);
        }


        ImgAdapter adapter = new ImgAdapter(mImageViews);
        vpGetpriceImg.setAdapter(adapter);

        vpGetpriceImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position % imgIdList.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.mipmap.dot_on);
            } else {
                tips[i].setBackgroundResource(R.mipmap.dot_off);
            }
        }
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


    private void hideEdittext(EditText myEditText){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if(isOpen){
            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_getprice_back:
                finish();
                break;
            case R.id.iv_gotop:
                scrollview.smoothScrollTo(0, DensityUtil.dip2px(mContext, 160));
                break;
            case R.id.rel_choose_city:
                Intent selectCityIntent = new Intent(mContext, SelectCityActivity.class);
                Bundle b = new Bundle();
                b.putString("from_home_getcity", "67");
                selectCityIntent.putExtra("choose_city_bundle", b);
                startActivityForResult(selectCityIntent, 0);

                break;
            case R.id.getprice_submit:
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Util.setToast(mContext, "请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Util.setToast(mContext, "请输入手机号码");
                    return;
                }

                if (Util.isNetAvailable(mContext)) {
                    showLoadingView();
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("cellphone", phone);
                    hashMap.put("city", city);
                    hashMap.put("ownername", name);
                    hashMap.put("urlhistory", Constant.PIPE_CODE);
                    hashMap.put("comeurl", Constant.PIPE_CODE);
                    hashMap.put("source", "1111");
                    if (Util.isLogin(mContext)) {
                        hashMap.put("userid", Util.getUserId(mContext));
                    } else {
                        hashMap.put("userid", "0");
                    }

                    OKHttpUtil okHttpUtil = new OKHttpUtil();
                    okHttpUtil.post(Constant.PUB_ORDER_URL, hashMap, new OKHttpUtil.BaseCallBack() {
                        @Override
                        public void onSuccess(Response response, String json) {
                            Util.setErrorLog(TAG, json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);

                                if(jsonObject.getInt("error_code") == 0){
//                                    Util.setToast(mContext, "发单成功,稍后给你电话");

                                    TipDialog1.Builder builder = new TipDialog1.Builder(mContext);
                                    builder.setTitle("温馨提醒")
                                            .setPositiveButton("确定",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            //退出当前账户
                                                            finish();
                                                        }
                                                    });
                                    builder.create().show();
                                }else {
                                    Util.setToast(mContext, "系统繁忙，稍后再试~");
                                }
                                hideLoadingView();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(Request request, IOException e) {
                            Util.setErrorLog(TAG, e.getMessage());
                            hideLoadingView();
                        }

                        @Override
                        public void onError(Response response, int code) {
                            Util.setErrorLog(TAG, response.body().toString());
                            hideLoadingView();
                        }
                    });
                }
                break;
        }

    }

    /***
     * 发单接口请求
     */
    private void requestPubOrder() {
        showLoadingView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 70:
                Bundle cityBundle = data.getBundleExtra("city_bundle");
                city = cityBundle.getString("ci");
                tvCity.setText(city);
                break;
        }
    }



    private class ImgAdapter extends PagerAdapter {
        private ImageView[] array;

        public ImgAdapter(ImageView[] array) {
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(array[position % array.length]);
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(array[position % array.length], 0);
            return array[position % array.length];
        }

    }

}