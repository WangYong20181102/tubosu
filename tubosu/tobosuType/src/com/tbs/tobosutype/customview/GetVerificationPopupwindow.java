package com.tbs.tobosutype.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
import com.tbs.tobosutype.activity.ApplyforSuccessActivity;
import com.tbs.tobosutype.activity.BindingPhoneActivity;
import com.tbs.tobosutype.activity.FindPwdActivity2;
import com.tbs.tobosutype.activity.FreeDesignPrice;
import com.tbs.tobosutype.activity.LoginActivity;
import com.tbs.tobosutype.activity.RegisterActivity2;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.http.HttpClientHelper;
import com.tbs.tobosutype.utils.HintInput;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/***
 * 获取验证码弹窗
 *
 * @author dec
 */
public class GetVerificationPopupwindow extends PopupWindow {
    private View mView;
    private ImageView iv_imageverif;
    private TextView tv_another;
    private TextView tv_cancel;
    private TextView tv_ensure;
    public EditText et_input;
    private HashMap<String, Object> headerMap = new HashMap<String, Object>();
    private String urlBase = AllConstants.TOBOSU_URL + "tapp/passport/get_pic_code?version=";
    private String header;
    private Handler imageVerifHandler = new Handler();
    private Context mContext;
    public String phone = null;
    private String imageVerif;
    public String version = null;

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

        getVerifyCode();

        tv_another.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerifyCode();
//				RequestQueue queue = Volley.newRequestQueue(mContext);
//				ImageRequest imageRequest = new ImageRequest(urlBase, new Response.Listener<Bitmap>() {
//					@Override
//					public void onResponse(Bitmap response) {
//						iv_imageverif.setImageBitmap(response);
//					}
//				}, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError error) {
//
//					}
//				});
//				queue.add(imageRequest);
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mobile", phone);
                map.put("pic_code", imageVerif);
                map.put("version", version);
                map.put("device", "android");
                new SendMessageThread(map).start();
            }
        });
        new HintInput(4, et_input, context);
    }

    private void getVerifyCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                headerMap = HttpClientHelper.loadTextFromURL(urlBase);
                System.out.println("urlBase-->>>" + urlBase + "<<<" + headerMap);
                if (headerMap != null) {
                    header = (String) headerMap.get("header");
                    final byte[] result = (byte[]) headerMap.get("body");
                    imageVerifHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                            iv_imageverif.setImageBitmap(bitmap);
                        }
                    });
                } else {
                }

            }
        }).start();
    }

    class SendMessageThread extends Thread {
        HashMap<String, String> map;

        public SendMessageThread(HashMap<String, String> map) {
            this.map = map;
        }

        public void run() {
            Looper.prepare();
            if (header != null) {
                String[] split = header.split(";");
                String result = HttpClientHelper.doPostSubmit(AllConstants.TOBOSU_URL + "tapp/passport/app_sms_code?" + System.currentTimeMillis(), map, split[0]);
                int errorCode = -1;
                try {
                    if (result != null) {
                        JSONObject object = new JSONObject(result);
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
            } else {
                Util.setLog("*********", "**+null*");
            }
        }
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
                    Toast.makeText(mContext, "验证码错误!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
