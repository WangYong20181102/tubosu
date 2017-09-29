package com.tbs.tobosutype.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.SelectPersonalPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.MiPictureHelper;
import com.tbs.tobosutype.utils.WriteUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FeedbackActivity extends Activity {
    private static final String TAG = FeedbackActivity.class.getSimpleName();

    private static final int REQUESTCODE_PICK_PICTURE = 0x00013;
    private static final int REQUESTCODE_TAKE = 0x00015;

    private Context mContext;

    private TextView tv_number_words;
    private Button bt_ensure;
    //banner
    private RelativeLayout rl_banner;
    /**
     * 反馈内容输入框
     */
    private EditText et_content;
    private ImageView iv_back_up;
    private TextView send_text_button;

    /**
     * 选择图片上传的window
     */
    private SelectPersonalPopupWindow menuWindow;

    /**
     * 反馈接口
     */
    private String feedBackUrl = Constant.TOBOSU_URL + "tapp/util/feedback";

    private HashMap<String, String> feedbackParams;
    private String mode;
    private String mode_version;
    private String client_version;
    private String token;

    /**
     * false 展开 <br/>
     * true 关闭
     */
    private boolean hidePict = false;

    private ImageView iv_add_icon;

    /**
     * 添加图片上传布局
     */
    private LinearLayout ll_feedback_pictures_layout;

    /**
     * 添加图片上传按钮
     */
    private ImageView iv_add_picture, iv_add_picture1, iv_add_picture2, iv_add_picture3;

    private int add = 0;

    /**
     * 下拉选择电话 邮箱
     */
    private Button btn_select_contact;

    /**
     * [0] 电话 <br/>
     * [1] 邮箱
     */
    private String[] contactArray = new String[]{"手机号          ", "邮箱          "};

    /**
     * 显示 电话 邮箱
     */
    private EditText et_contact;

    private String contactString = "";

    private String userMark;

    private String contactType = "";

    private String phoneNum = "";

    private String emailAddr = "";

    /**
     * 请求网络进度
     */
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_feedback);

        mContext = FeedbackActivity.this;
        initView();
        initData();
        initEvetnt();
    }

    private void initView() {
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        tv_number_words = (TextView) findViewById(R.id.tv_number_words);
        bt_ensure = (Button) findViewById(R.id.bt_ensure);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.clearFocus();
        iv_back_up = (ImageView) findViewById(R.id.iv_back_up);
        send_text_button = (TextView) findViewById(R.id.send_text_button);

        iv_add_icon = (ImageView) findViewById(R.id.iv_add_icon);
        ll_feedback_pictures_layout = (LinearLayout) findViewById(R.id.ll_feedback_pictures_layout);
        iv_add_picture = (ImageView) findViewById(R.id.iv_add_picture);
        iv_add_picture1 = (ImageView) findViewById(R.id.iv_add_picture1);
        iv_add_picture2 = (ImageView) findViewById(R.id.iv_add_picture2);
        iv_add_picture3 = (ImageView) findViewById(R.id.iv_add_picture3);

        et_contact = (EditText) findViewById(R.id.et_contact_cell);
        btn_select_contact = (Button) findViewById(R.id.btn_select_contact);

        ll_feedback_pictures_layout.setVisibility(View.GONE);
        rl_banner.setBackgroundColor(Color.parseColor("#ff882e"));

    }


    private void initData() {
        mode = android.os.Build.MODEL;
        client_version = AppInfoUtil.getAppVersionName(getApplicationContext());
        mode_version = android.os.Build.VERSION.RELEASE;
        feedbackParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        token = AppInfoUtil.getToekn(getApplicationContext());

        // 获取用户存储信息
        SharedPreferences userInfoSP = getSharedPreferences("userInfo", 0);
        userMark = userInfoSP.getString("mark", "0");

        // 已经登录 可获取电话号码
        if (token != null && !"".equals(token)) {
            phoneNum = userInfoSP.getString("cellphone", "");
            if ("未绑定".equals(phoneNum)) {
                phoneNum = "";
            } else {
                if (!"".equals(phoneNum)) {
                    et_contact.setHint(phoneNum);
                } else {
                    et_contact.setHint("可输入电话号码");
                }
            }
            contactType = "phone";
        }

    }

    private void initEvetnt() {
        iv_back_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_number_words.setText(s.length() + "/500");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_select_contact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 出现下拉 window
                initPopWindow();
            }
        });


        iv_add_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!hidePict) {
                    //展开
                    iv_add_icon.setBackgroundResource(R.drawable.icon_pull_up);
                    ll_feedback_pictures_layout.setVisibility(View.VISIBLE);

                } else {
                    // 关闭
                    iv_add_icon.setBackgroundResource(R.drawable.icon_pull_down);
                    ll_feedback_pictures_layout.setVisibility(View.GONE);
                }
                hidePict = !hidePict;
            }
        });

        bt_ensure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        send_text_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        //添加图片
        iv_add_picture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initScreenShotImage();
            }
        });

    }

    private void initScreenShotImage() {
        menuWindow = new SelectPersonalPopupWindow(FeedbackActivity.this, itemsOnClick);
        menuWindow.showAtLocation(findViewById(R.id.layout_feedback), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /***
     * 发送反馈内容
     */
    private void sendFeedback() {
        if (et_content.getText().toString().length() == 0) {
            Toast.makeText(mContext, "您要反馈的内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }


        // FIXME 有图片则开启线程
        if (bitmapList != null && bitmapList.size() > 0) {
            // 有图片需要上传  开启线程上传
            for (int i = 0; i < pictPathLists.size(); i++) {
                System.out.println("log [" + pictPathLists.get(i) + "]");
            }
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            new Thread(uploadImageRunnable).start();
        } else {
            send("");
        }
    }

    private void send(String thumbpath) {

        //手机型号
        feedbackParams.put("mode", mode);
        // 安卓系统版本
        feedbackParams.put("mode_version", mode_version);
        // app版本
        feedbackParams.put("client_version", client_version);

        if (contactType.equals("email")) {
            feedbackParams.put("email", et_contact.getText() + "");
        } else {
            feedbackParams.put("mobile", et_contact.getText() + "");
        }
        //电话号码  邮箱地址  照片地址

        feedbackParams.put("thumbpath", thumbpath);
        feedbackParams.put("content", "【用户:" + AppInfoUtil.getUserid(mContext) + "】【反馈内容:" + et_content.getText().toString() + "】");
        if (token.length() != 0) {
            feedbackParams.put("token", token);
        }


        OKHttpUtil.post(feedBackUrl, feedbackParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("error_code") == 0) {
                                Toast.makeText(mContext, "反馈成功，感谢您的的宝贵意见！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);

                    break;
                case R.id.pickPhotoBtn:
                    // 选择本地文件夹中图片
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK_PICTURE);
                    break;
                default:
                    break;
            }
        }
    };

    private Bitmap pickBitmap;
    private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    private ArrayList<String> pictPathLists = new ArrayList<String>();

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_TAKE:
                // 拍照
                String path = "";
                // 判断是否有sd卡
                if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    // 若没有传文件的路径 则重新生成一个新路径
                    path = Environment.getExternalStorageDirectory() + "/uploadPicture/";
                }
                if (data != null) {
                    if (add == 0 && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            String pictureDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                            String fileName = pictureDate + ".jpg";
//						savePicture(bitmap, "", fileName);
//						Bitmap bm = compressionBigBitmap(bitmap, false); // 不压缩了
                            pictPathLists.add(saveBitmap(bitmap, path, fileName));
                            iv_add_picture1.setVisibility(View.VISIBLE);
                            iv_add_picture1.setImageBitmap(bitmap);
                            bitmapList.add(bitmap);
                            System.out.println("新增第" + add + "张图片");
                            add++;
                        }
                    } else if (add == 1 && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            String pictureDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                            String fileName = pictureDate + ".jpg";
//						Bitmap bm = compressionBigBitmap(bitmap, false); // 不压缩了
                            pictPathLists.add(saveBitmap(bitmap, path, fileName));
                            iv_add_picture2.setVisibility(View.VISIBLE);
                            iv_add_picture2.setImageBitmap(bitmap);
                            bitmapList.add(bitmap);
                            System.out.println("新增第" + add + "张图片");
                            add++;
                        }
                    } else if (add == 2 && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            String pictureDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                            String fileName = pictureDate + ".jpg";
//						Bitmap bm = compressionBigBitmap(bitmap, false); // 不压缩了
                            pictPathLists.add(saveBitmap(bitmap, path, fileName));
                            iv_add_picture3.setVisibility(View.VISIBLE);
                            iv_add_picture3.setImageBitmap(bitmap);
                            iv_add_picture.setVisibility(View.GONE);
                            bitmapList.add(bitmap);
                            System.out.println("新增第" + add + "张图片");
                        }
                    }
                }

                break;
            case REQUESTCODE_PICK_PICTURE:
                // 选取照片
                if (data != null) {
                    try {
                        Uri uri = data.getData();

                        String pickPath = MiPictureHelper.getPath(mContext, uri);
                        System.out.println("未做路径处理之前的路径 -->> " + pickPath);


//					Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//					cursor.moveToFirst();
//					
//					String imageFilePath = cursor.getString(1);
//					System.out.println("从相册中选取图片的地址【" + imageFilePath+"】<br/>");

                        pictPathLists.add(pickPath);

                        FileInputStream fis = new FileInputStream(pickPath);
                        pickBitmap = BitmapFactory.decodeStream(fis);

//					int width = pickBitmap.getWidth();
//					int height = pickBitmap.getHeight();
//					System.out.println("压缩前的宽高----> width: " + width + " height:" + height);

                        /***压缩获取的图像 */
                        Bitmap bitmap = compressionBigBitmap(pickBitmap, false);
//					showImgs(pickBitmap, false);

                        if (add == 0) {
                            if (bitmap != null) {
//							savePicture(bitmap, imageFilePath, imageFilePath.substring(imageFilePath.lastIndexOf("/")));
                                pickPath = pickPath.substring(0, pickPath.lastIndexOf("/"));
                                System.out.println("选取图片的路径[" + pickPath + "]");
                                pictPathLists.add(saveBitmap(bitmap, pickPath, pickPath.substring(pickPath.lastIndexOf("/"))));
                                iv_add_picture1.setVisibility(View.VISIBLE);
                                iv_add_picture1.setImageBitmap(bitmap);
                                bitmapList.add(bitmap);
                                System.out.println("新增第" + add + "张图片");
                                add++;
                            }
                        } else if (add == 1) {
                            if (bitmap != null) {
                                pickPath = pickPath.substring(0, pickPath.lastIndexOf("/"));
                                System.out.println("选取图片的路径[" + pickPath + "]");
                                pictPathLists.add(saveBitmap(bitmap, pickPath, pickPath.substring(pickPath.lastIndexOf("/"))));
                                iv_add_picture2.setVisibility(View.VISIBLE);
                                iv_add_picture2.setImageBitmap(bitmap);
                                System.out.println("新增第" + add + "张图片");
                                bitmapList.add(bitmap);
                                add++;
                            }
                        } else if (add == 2) {
                            if (bitmap != null) {
                                pickPath = pickPath.substring(0, pickPath.lastIndexOf("/"));
                                System.out.println("选取图片的路径[" + pickPath + "]");
                                pictPathLists.add(saveBitmap(bitmap, pickPath, pickPath.substring(pickPath.lastIndexOf("/"))));
                                iv_add_picture3.setVisibility(View.VISIBLE);
                                iv_add_picture3.setImageBitmap(bitmap);
                                iv_add_picture.setVisibility(View.GONE);
                                System.out.println("新增第" + add + "张图片");
                                bitmapList.add(bitmap);
                            }
                        }
                        fis.close();
//					cursor.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    ;


    /**
     * @param bitmap
     * @return 压缩后的bitmap
     */
    private Bitmap compressionBigBitmap(Bitmap bitmap, boolean isSysUp) {
        Bitmap destBitmap = null;
        /* 图片宽度调整为100，大于这个比例的，按一定比例缩放到宽度为100 */
        if (bitmap.getWidth() > 80) {
            float scaleValue = (float) (80f / bitmap.getWidth());
            System.out.println("缩放比例---->" + scaleValue);

            Matrix matrix = new Matrix();
            /* 针对系统拍照，旋转90° */
            if (isSysUp)
                matrix.setRotate(90);
            matrix.postScale(scaleValue, scaleValue);

            destBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            int widthTemp = destBitmap.getWidth();
            int heightTemp = destBitmap.getHeight();
            Log.d(TAG, "压缩后的宽高----> width: " + heightTemp + " height:" + widthTemp);
        } else {
            return bitmap;
        }
        return destBitmap;

    }


    private void initPopWindow() {

        View windowContentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow_listview, null);

        final PopupWindow popupWindow = new PopupWindow(windowContentView, LayoutParams.WRAP_CONTENT, DensityUtil.px2dip(mContext, 142));
        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        ListView listView = (ListView) windowContentView.findViewById(R.id.contact_listview);
        ContactWindowAdapter adapter = new ContactWindowAdapter(mContext, contactArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactString = parent.getItemAtPosition(position).toString();
                if ("未绑定".equals(contactString)) {
                    contactString = "";
                }
                if (userMark.equals("0")) {
                    // 未登陆
                    if (contactString.trim().equals("邮箱") && "".equals(et_contact.getText().toString().trim())) {
                        //邮箱
                        et_contact.setHint("可输入邮箱地址");
                        btn_select_contact.setText(contactArray[1]);
                        contactType = "email";
                    } else if (contactString.trim().equals("邮箱") && !"".equals(et_contact.getText().toString().trim())) {
                        //邮箱
                        et_contact.setText("");
                        btn_select_contact.setText(contactArray[1]);
                        contactType = "email";
                    } else if (contactString.trim().equals("手机号") && "".equals(et_contact.getText().toString().trim())) {
                        // 电话
                        et_contact.setHint("可输入手机号");
                        btn_select_contact.setText(contactArray[0]);
                        contactType = "phone";
                    } else if (contactString.trim().equals("手机号") && !"".equals(et_contact.getText().toString().trim())) {
                        // 电话
                        et_contact.setText("");
                        btn_select_contact.setText(contactArray[0]);
                        contactType = "phone";
                    }
                } else {
                    // 已登陆
                    if (contactString.trim().equals("邮箱")) {
                        //邮箱
                        et_contact.setHint("可输入邮箱地址");
                        btn_select_contact.setText(contactArray[1]);
                        contactType = "email";
                    } else {
                        // 电话
                        if (!"".equals(phoneNum)) {
                            et_contact.setHint(phoneNum);
                        } else {
                            et_contact.setHint("可输入手机号");
                        }
                        btn_select_contact.setText(contactArray[0]);
                        contactType = "phone";
                    }
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(btn_select_contact, 0, 0);
    }


    class ContactWindowAdapter extends BaseAdapter {
        private String[] array;
        private LayoutInflater inflater;

        public ContactWindowAdapter(Context context, String[] array) {
            this.array = array;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SpinnerHolder holder;
            if (convertView == null) {
                holder = new SpinnerHolder();
                convertView = inflater.inflate(R.layout.item_contact_spinner, null);
                holder.tvContact = (TextView) convertView.findViewById(R.id.tv_contact_textview);
                convertView.setTag(holder);
            } else {
                holder = (SpinnerHolder) convertView.getTag();
            }

            holder.tvContact.setText(array[position].trim());

            return convertView;
        }
    }

    class SpinnerHolder {
        TextView tvContact;
    }


    /*** 上传图片到此接口*/
    private String imgUrl = Constant.TOBOSU_URL + "cloud/upload/upload_for_ke?";

    /**
     * 上传图片成功与否得到的结果
     */
    private List<String> returnResultList = new ArrayList<String>();

    /**
     * 上传图片成功与否得到的结果拼出来的字符串
     */
    private String returnUrlString = "";


    /**
     * 保存方法
     *
     * @param bm   图片位图
     * @param path 图片路径
     * @param name 图片名
     */
    public String saveBitmap(Bitmap bm, String path, String name) {
        Log.e(TAG, "进入保存图片方法");
        File f = new File(path, name);
        if (!f.exists()) {
            try {
                f.createNewFile();

                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Log.d(TAG, "所拍的图片已保存到本地");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "所拍的图片在本地无法找到文件");
                return "";
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "保存图片出错");
                return "";
            }
        }
        return path + name;
    }


    private Runnable uploadImageRunnable = new Runnable() {

        @Override
        public void run() {
            // 去掉重复的路径
            HashSet<String> hashSet = new HashSet<String>();
            for (int i = 0; i < pictPathLists.size(); i++) {
                hashSet.add(pictPathLists.get(i));
            }
            pictPathLists.clear();
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                pictPathLists.add((String) it.next());
//				System.out.println("-->>"+it.next().toString());
            }


            Looper.prepare();
            if (pictPathLists != null && pictPathLists.size() > 0) {
                for (int i = 0; i < pictPathLists.size(); i++) {
                    System.out.println("打印需要上传的图片的地址[" + pictPathLists.get(i) + "]");
                    if (!"".equals(pictPathLists.get(i))) {
                        Map<String, String> textParams = null;
                        Map<String, File> fileparams = null;
                        try {
                            URL url = new URL(imgUrl);
                            textParams = new HashMap<String, String>();
                            fileparams = new HashMap<String, File>();
                            File file = new File(pictPathLists.get(i));
                            fileparams.put("filedata", file);
                            textParams.put("s_code", "head_file");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setUseCaches(false);
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestProperty("user-Agent", "Fiddler");
                            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + WriteUtil.BOUNDARY);
                            OutputStream os = conn.getOutputStream();
                            DataOutputStream ds = new DataOutputStream(os);
                            WriteUtil.writeStringParams(textParams, ds);
                            WriteUtil.writeFileParams(fileparams, ds);
                            WriteUtil.paramsEnd(ds);
                            // 对文件流操作完,要记得及时关闭
                            os.close();
                            // 服务器返回的响应吗
                            int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                            // 对响应码进行判断
                            if (code == 200) {// 返回的响应码200,是成功
                                // 得到网络返回的输入流
                                InputStream is = conn.getInputStream();
                                returnResultList.add(WriteUtil.readString(is));
                            } else {
                                Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            handlerUpload.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
        }
    };

    //上传成功与否的回调
    private Handler handlerUpload = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    System.out.println("返回【" + returnResultList.toString() + "】  【长度：" + returnResultList.size() + "】");

                    try {
                        JSONArray jsonArray = new JSONArray(returnResultList.toString());
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            if (len == 1) {
                                if ("0".equals(jsonArray.getJSONObject(i).getString("error"))) {
                                    returnUrlString = returnUrlString + jsonArray.getJSONObject(i).getString("url");
                                }
                            } else {
                                if (i == len - 1) {
                                    if ("0".equals(jsonArray.getJSONObject(i).getString("error"))) {
                                        returnUrlString = returnUrlString + jsonArray.getJSONObject(i).getString("url");
                                    }
                                } else {
                                    if ("0".equals(jsonArray.getJSONObject(i).getString("error"))) {
                                        returnUrlString = returnUrlString + jsonArray.getJSONObject(i).getString("url") + ",";
                                    }
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("解析出错！");
                    }


//				System.out.print("最后获取返回的字符串是"+returnUrlString);

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    Message msgs = new Message();
                    msgs.what = 12;
                    myHandler.sendMessage(msgs);

                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 12:
                    send(returnUrlString);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    protected void onDestroy() {
        super.onDestroy();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    ;

}
