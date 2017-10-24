package com.tbs.tobosutype.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.BindingPhoneActivity;
import com.tbs.tobosutype.activity.FindPwdActivity2;
import com.tbs.tobosutype.activity.FreeDesignPrice;
import com.tbs.tobosutype.activity.LoginActivity;
import com.tbs.tobosutype.activity.RegisterActivity2;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.HintInput;
import com.tbs.tobosutype.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***
 * 获取验证码弹窗
 *
 * @author dec
 */
public class GetVerificationPopupwindow extends PopupWindow {
    private String TAG = "GetVerificationPopupwindow";
    private View mView;
    private ImageView iv_imageverif;
    private InputStream imgStream;
    private TextView tv_another;
    private TextView tv_cancel;
    private TextView tv_ensure;
    private String urlBase = Constant.TOBOSU_URL + "tapp/passport/get_pic_code?version=";
    private Context mContext;
    public String phone = null;
    private String imageVerif;
    public String version = null;
    public EditText et_input;

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 444:
                    Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
                    iv_imageverif.setImageBitmap(bitmap);
                    break;
            }
        }
    };


    public GetVerificationPopupwindow(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popupwindow_get_verification, null);
        iv_imageverif = (ImageView) mView.findViewById(R.id.iv_imageverif);
        tv_another = (TextView) mView.findViewById(R.id.tv_another);
        tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
        tv_ensure = (TextView) mView.findViewById(R.id.tv_ensure);
        et_input = (EditText) mView.findViewById(R.id.et_input);
        this.setContentView(mView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.custom_popupwindow_animstyle);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(Util.isNetAvailable(mContext)){
            OKHttpUtil.get(urlBase, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    imgStream = response.body().byteStream();
                    mhandler.sendEmptyMessage(444);
                }
            });
        }

        tv_another.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isNetAvailable(mContext)){
                    OKHttpUtil.get(urlBase, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            imgStream = response.body().byteStream();
                            mhandler.sendEmptyMessage(444);
                        }
                    });
                }
            }
        });

        tv_ensure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_input.getText() == null || et_input.getText().length() == 0) {
                    Toast.makeText(mContext, "验证码为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                imageVerif = et_input.getText().toString().trim();

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("mobile", phone);
                map.put("pic_code", imageVerif);
//                map.put("version", version);
//                map.put("device", "android");


                Util.setErrorLog(TAG, phone);
                Util.setErrorLog(TAG, imageVerif);
//                Util.setErrorLog(TAG, version);
//                Util.setErrorLog(TAG, imageVerif);


                OKHttpUtil.post(Constant.TOBOSU_URL + "tapp/passport/app_sms_code"/* + System.currentTimeMillis()*/, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mhandler.sendEmptyMessage(4);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String codeJson = response.body().string();
                        Util.setErrorLog(TAG, codeJson);


                        if (codeJson != null && !"".equals(codeJson)) {
                            int errorCode = -1;
                            try {
                                if (codeJson != null) {
                                    JSONObject object = new JSONObject(codeJson);
                                    errorCode = object.getInt("error_code");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Message msg = new Message();
                            msg.what = 0;
                            if (errorCode == 0) {
                                msg.obj = 0;
                            } else {
                                msg.obj = -1;
                            }
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        });
        new HintInput(4, et_input, context);
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int errorCode = (Integer) msg.obj;
                if (errorCode == 0) {
                    dismiss();
                    Activity activity = (Activity) mContext;
                    if (activity instanceof RegisterActivity2) {
                        ((RegisterActivity2) activity).startCount();
                    } else if (activity instanceof FindPwdActivity2) {
                        ((FindPwdActivity2) activity).startCount();

                    } else if (activity instanceof BindingPhoneActivity) {
                        ((BindingPhoneActivity) activity).startCount();
                    } else if (activity instanceof LoginActivity) {
                        ((LoginActivity) activity).startCount();
                    } else if (activity instanceof FreeDesignPrice) {
//						((ApplyforSuccessActivity) activity).startCount();
                        ((FreeDesignPrice) activity).startCount();
                    }
                } else {
                    Util.setToast(mContext, "验证码错误!");
                }
            }
        }
    };
}