package com.tbs.tobosutype.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.DesignFreePopupWindow;
import com.tbs.tobosutype.customview.TouchImageView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.Util;

/**
 * 逛图库 全浏览页
 *
 * @author dec
 */
public class ImageDetailsFullScreenActivity extends Activity implements OnPageChangeListener {
    private static final String TAG = ImageDetailsFullScreenActivity.class.getSimpleName();
    private Context mContext;
    private ViewPager full_screen_view_pager;
    private TextView pageText;
    private TextView tv_image_name;

    /**
     * 收藏按钮的布局
     */
    private LinearLayout ll_store;

    /**
     * 收藏按钮
     */
    private ImageView iv_store;

    /**
     * 下载按钮的布局
     */
    private LinearLayout ll_downLoad;

    /**
     * 为我设计按钮的布局
     */
    private LinearLayout ll_designforme;

    private String id;

    /***效果图详情接口*/
    private String imageDetaiUrl = Constant.TOBOSU_URL + "tapp/impression/detail";

    /**
     * 添加取消收藏接口
     */
    private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";

    /**
     * 发单入口接口
     */
    private String pubOrderUrl = Constant.TOBOSU_URL + "tapi/order/pub_order";

    /**
     * 安卓url
     */
    private String androidUrl = "http://m.tobosu.com/?channel=seo&subchannel=android&chcode=" + AppInfoUtil.getChannType(MyApplication.getContext());;


    private RequestParams imageDetaiParams;
    private List<String> imageUrls;

    private ViewPagerAdapter adapter;

    private String id_next;
    private String id_prev;
    private String layout_id;
    private String area_id;
    private String plan_price;
    private boolean showImageLastOne = false;

    private int currentPage;

    /***图片缓存本地的路径地址*/
    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/tbs/";
    private Bitmap mBitmap;

    /**
     * 图片文件名称
     */
    private String mFileName;

    /**
     * 图片下载是否成功信息提示
     */
    private String mSaveMessage;

    /**
     * 免费设计popupwindow
     */
    private DesignFreePopupWindow designPopupWindow;

    private String userid;
    private String cityid;
    private String oper_type = "-1";
    private String token;
    private String fav_type = "showpic";

    private String fav_conid;
    private String hav_fav;
    private int index;
    private boolean flag = true;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_image_detail_viewpager);
        mContext = ImageDetailsFullScreenActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        pageText = (TextView) findViewById(R.id.tv_image_num);
        ll_store = (LinearLayout) findViewById(R.id.ll_store);
        ll_designforme = (LinearLayout) findViewById(R.id.ll_designforme);
        ll_downLoad = (LinearLayout) findViewById(R.id.ll_downLoad);
        tv_image_name = (TextView) findViewById(R.id.tv_image_name);
        iv_store = (ImageView) findViewById(R.id.iv_store);
        full_screen_view_pager = (ViewPager) findViewById(R.id.full_screen_view_pager);
        adapter = new ViewPagerAdapter();
        full_screen_view_pager.setAdapter(adapter);
    }

    private void initData() {
        SharedPreferences settings = getSharedPreferences("userInfo", 0);
        userid = settings.getString("userid", "");
//        token = AppInfoUtil.getAppVersionName(getApplicationContext());
        index = getIntent().getExtras().getInt("index", 0);
        fav_conid = getIntent().getExtras().getString("fav_conid");

        id = getIntent().getExtras().getString("id");
        imageUrl = getIntent().getExtras().getString("url");
        token = AppInfoUtil.getToekn(getApplicationContext());
        imageDetaiParams = AppInfoUtil.getPublicParams(getApplicationContext());
        imageDetaiParams.put("token", token);

//        imageDetaiParams.put("id", id);
        imageDetaiParams.put("url", imageUrl);
        imageUrls = new ArrayList<String>();
        full_screen_view_pager.setOnPageChangeListener(this);
        full_screen_view_pager.setEnabled(false);
        Util.setLog(TAG, "imgurl[" + imageUrl + "]   id[" + id + "]   index[" + index + "]");
        requestImages();
    }

    private void initEvent() {

        //下载
        ll_downLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Constant.checkNetwork(mContext)) {
                    // 效果图下载线程
                    new Thread(saveFileRunnable).start();
                } else {
                    Toast.makeText(mContext, "请检查网络~", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ll_designforme.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                designPopupWindow = new DesignFreePopupWindow(mContext, itemsClick);
                designPopupWindow.showAtLocation(findViewById(R.id.ll_designforme), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        ll_store.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
				token = AppInfoUtil.getToekn(getApplicationContext());
                if (token == null || token.length() == 0) {
                    Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("isFav", true);
                    startActivityForResult(intent, 0);
                    return;
                }
                RequestParams favParams = AppInfoUtil.getPublicParams(getApplicationContext());
                favParams.put("fav_conid", fav_conid);
                if (oper_type.equals("1") && !TextUtils.isEmpty(oper_type)) {
                    favParams.put("token", token);
                    favParams.put("fav_type", fav_type);
                    favParams.put("oper_type", "1");
                    hav_fav = "1";
                } else {
                    favParams.put("token", token);
                    favParams.put("oper_type", "0");
                    hav_fav = "0";
                }
                if (hav_fav.equals("1") && !TextUtils.isEmpty(hav_fav)) {
                    iv_store.setImageResource(R.drawable.image_love_sel);
                    oper_type = "0";
                } else {
                    iv_store.setImageResource(R.drawable.image_love_nor);
                    oper_type = "1";
                }
                // 添加和取消收藏接口方法
                HttpServer.getInstance().requestPOST(favUrl, favParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                        String result = new String(body);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("操作成功")) {
                                if (oper_type.equals("0")) {
                                    Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		token = data.getStringExtra("token");
		token = AppInfoUtil.getToekn(getApplicationContext());
    }

    /***
     * 效果图显示接口请求
     */
    private void requestImages() {

        HttpServer.getInstance().requestPOST(imageDetaiUrl, imageDetaiParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                String result = new String(body);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("error_code") == 0) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                        id_next = jsonObject2.getString("id_next");
                        id_prev = jsonObject2.getString("id_prev");
                        layout_id = jsonObject2.getString("layout_id");
                        area_id = jsonObject2.getString("area_id");
                        hav_fav = jsonObject2.getString("hav_fav");
                        if (hav_fav.equals("1")) {
                            iv_store.setImageResource(R.drawable.image_love_sel);
                            oper_type = "0";
                        } else {
                            iv_store.setImageResource(R.drawable.image_love_nor);
                            oper_type = "1";
                        }

                        plan_price = jsonObject2.getString("plan_price");

                        JSONArray jsonArray = jsonObject2.getJSONArray("single_map");
                        imageUrls.clear();
                        imageUrls.add("");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            imageUrls.add(jsonArray.getString(i));
                        }
                        imageUrls.add("");
                        adapter.notifyDataSetChanged();
                        tv_image_name.setText(layout_id + " - " + area_id + " - " + plan_price + "万");
                        if (flag) {
                            full_screen_view_pager.setCurrentItem(index + 1);
                            flag = false;
                        } else {
                            if (showImageLastOne) {
                                full_screen_view_pager.setCurrentItem(imageUrls.size() - 2);
                            } else {
                                full_screen_view_pager.setCurrentItem(1);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }

    /***
     * FIXME
     * @author dec
     *
     */
    class ViewPagerAdapter extends PagerAdapter {

        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView view = new TouchImageView(mContext);
            ImageLoaderUtil.loadImages(mContext, view, imageUrls.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            if (imageUrls != null) {
                return imageUrls.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    @Override
    public void onPageScrollStateChanged(int postion) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int postion) {
        currentPage = postion;
        if (postion == 0) {
            Toast.makeText(mContext, "即将进入上一套图！", Toast.LENGTH_SHORT).show();
            imageDetaiParams = AppInfoUtil.getPublicParams(getApplicationContext());
            imageDetaiParams.put("id", id_prev);
            showImageLastOne = true;
            id = id_prev;
            requestImages();

        } else if (postion == imageUrls.size() - 1) {
            Toast.makeText(mContext, "即将进入下一套图！", Toast.LENGTH_SHORT).show();
            imageDetaiParams = AppInfoUtil.getPublicParams(getApplicationContext());
            imageDetaiParams.put("id", id_next);
            showImageLastOne = false;
            id = id_next;
            requestImages();
        } else {
            pageText.setText((currentPage) + "/" + (imageUrls.size() - 2));
            new Thread(connectNet).start();
        }
    }


    public byte[] getImage(String path){
        if(path!=null && !"".equals(path)){
            try{
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                InputStream inStream = conn.getInputStream();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return readStream(inStream);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /***
     * 下载照片到本地文件夹
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName){
        File dirFile = new File(IMG_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(IMG_PATH + fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            if (bm != null) {
                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }

    }

    /***
     * 下载线程
     */
    private Runnable saveFileRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                saveFile(mBitmap, mFileName);
                mSaveMessage = "效果图下载成功！";
            } catch (Exception e) {
                mSaveMessage = "效果图下载失败！";
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }

    };

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mContext, mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     *
     */
    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = imageUrls.get(currentPage);
                mFileName = System.currentTimeMillis() + ".jpg";
                byte[] data = getImage(filePath);
                if (data != null) {
                    mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                } else {
//                    Toast.makeText(mContext, "Image error!", Toast.LENGTH_SHORT).show();
                }


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                mBitmap = BitmapFactory.decodeStream(getImageStream(filePath), null, options);
                connectHanlder.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };

    // FIXME 为什么没有做？
    private Handler connectHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };


    private OnClickListener itemsClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_freedesign_submit:
                    phone = ((DesignFreePopupWindow) designPopupWindow).getPhone();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(mContext, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (phone.length() != 11) {
                        Toast.makeText(mContext, "手机号码必须是11位数字！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        RequestParams params = AppInfoUtil.getPublicParams(getApplicationContext());
                        params.put("cellphone", phone);
                        params.put("city", cityid);
                        params.put("urlhistory", Constant.PIPE); // 渠道代码
                        params.put("comeurl", Constant.PIPE); //订单发布页面
                        params.put("comeurl", androidUrl);
                        if (!TextUtils.isEmpty(userid)) {
                            params.put("userid", userid);
                        } else {
                            params.put("userid", "0");
                        }
                        params.put("source", "898");
                        requestPubOrder(params);
                    }

                    break;
                default:
                    break;
            }
        }

        /***
         * 发单接口请求
         * @param params
         */
        private void requestPubOrder(RequestParams params) {
            HttpServer.getInstance().requestPOST(pubOrderUrl, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                    String result = new String(body);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getInt("error_code") == 0) {
                            Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
                            intent.putExtra("phone", phone);
                            //跳转申请成功的页面
                            startActivity(intent);
                            designPopupWindow.dismiss();
                        } else {
                            Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println("--全屏页没有发单？ --");
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                }
            });
        }

        ;
    };
    private String imageUrl;

}