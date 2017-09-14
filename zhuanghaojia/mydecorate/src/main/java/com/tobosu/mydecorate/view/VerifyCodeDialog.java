package com.tobosu.mydecorate.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.HttpClientHelper;
import com.tobosu.mydecorate.util.Util;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by dec on 2017/8/17.
 */
public class VerifyCodeDialog extends Dialog {
    private static String TAG = "VerifyCodeDialog";
    private static String header;
    private static HashMap<String, Object> headerMap = new HashMap<String, Object>();
    private static String urlBase = Constant.ZHJ + "tapp/passport/get_pic_code";

    public VerifyCodeDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    public VerifyCodeDialog(Context context, int theme) {
        super(context, theme);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private String imageVerif;
        private String phone;
        private TimeCount timeCount;
        private Handler imageVerifHandler = new Handler();
        private String version;
        private OnClickListener cancelButtonClickListener, okButtonClickListener;
        private TextView timeNmu;
        private ImageView iv_imageverif;
        public Builder(Context context, TextView timeNmu, String phone) {
            this.context = context;
            this.phone = phone;
            this.timeNmu = timeNmu;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setCancelButton( OnClickListener listener) {
            this.cancelButtonClickListener = listener;
            return this;
        }

        public VerifyCodeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final VerifyCodeDialog dialog = new VerifyCodeDialog(context, R.style.warn_dialog_style);

            timeCount = new TimeCount(60000, 1000, timeNmu);

            View mView = inflater.inflate(R.layout.popupwindow_get_verification, null);
            LinearLayout windowLayout = (LinearLayout) mView.findViewById(R.id.pop_linearlayout);
            windowLayout.setBackgroundResource(R.drawable.custom_dialog_bg);
            iv_imageverif = (ImageView) mView.findViewById(R.id.iv_imageverif);
            TextView tv_another = (TextView) mView.findViewById(R.id.tv_another);
            TextView tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
            TextView tv_ensure = (TextView) mView.findViewById(R.id.tv_ensure);
            final EditText et_input = (EditText) mView.findViewById(R.id.et_input);

            dialog.addContentView(mView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    timeCount.reset();
                }
            });

            tv_ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_input.getText() == null || et_input.getText().length() == 0) {
                        Toast.makeText(context, "验证码为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    timeCount.start();
                    imageVerif = et_input.getText().toString().trim();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("mobile", phone);
                    map.put("pic_code", imageVerif);

                    String[] split = header.split(";");

                    Util.setErrorLog("=========>>", "返回揭开锅phone是" + phone + "    pic_code验证码：" +imageVerif +"==split[0]=" + split[0]);
                    new SendMessageThread(context, dialog, map).start();

                }
            });

            tv_another.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            }

                        }
                    }).start();
                }
            });
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
                    }

                }
            }).start();

            dialog.setContentView(mView);
            Window window = dialog.getWindow();
            return dialog;
        }


        class TimeCount extends CountDownTimer {

            private TextView btn_count;

            public TimeCount(long millisInFuture, long countDownInterval,TextView btn_count) {
                super(millisInFuture, countDownInterval);
                this.btn_count = btn_count;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                btn_count.setEnabled(false);
                btn_count.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                btn_count.setEnabled(true);
                btn_count.setText("获取验证码");
                timeCount = null;
            }

            public void reset(){
                this.cancel();
                onFinish();
            }

        }

        class SendMessageThread extends Thread {
            HashMap<String, String> map;
            Dialog mDialog;
            Context context;
            public SendMessageThread(Context context, Dialog mDialog, HashMap<String, String> map) {
                this.map = map;
                this.context = context;
                this.mDialog = mDialog;
            }

            public void run() {
                Looper.prepare();
                String[] split = header.split(";");
                String result = HttpClientHelper.doPostSubmit(Constant.ZHJ + "tapp/passport/app_sms_code?" + System.currentTimeMillis(), map, split[0]);
                Util.setErrorLog("=====>>", "返回码result是什么=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getInt("error_code")==0){
                        mDialog.dismiss();
                    }else {
                        Util.setErrorLog(TAG, "验证码错误!====================");
                        Activity activity = (Activity) context;
                        Intent it = new Intent(Constant.SEND_STARTCOUNT_ACTION);
                        activity.sendBroadcast(it);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}