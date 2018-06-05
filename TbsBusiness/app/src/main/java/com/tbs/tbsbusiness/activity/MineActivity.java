package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.customview.CustomDialog;

public class MineActivity extends BaseActivity {
    private Context mContext;
    private String TAG = "MineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        mContext = this;
        Log.e(TAG, "==onCreate==");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "==onStart==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "==onStop==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "==onResume==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "==onRestart==");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause==");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==onDestroy==");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //退出前将数据上传
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
