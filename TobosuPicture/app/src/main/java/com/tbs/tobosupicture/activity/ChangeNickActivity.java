package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用户修改自己的昵称
 */
public class ChangeNickActivity extends BaseActivity {

    @BindView(R.id.change_nick_back)
    LinearLayout changeNickBack;
    @BindView(R.id.change_nick_input)
    EditText changeNickInput;
    @BindView(R.id.change_nick_ok)
    TextView changeNickOk;

    private Context mContext;
    private String TAG = "ChangeNickActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.change_nick_back, R.id.change_nick_ok})
    public void onViewClickedInChangeNickActivity(View view) {
        switch (view.getId()) {
            case R.id.change_nick_back:
                finish();
                break;
            case R.id.change_nick_ok:
                //调用修改昵称的接口
                if (TextUtils.isEmpty(changeNickInput.getText().toString())) {
                    Toast.makeText(mContext, "您输入的昵称为空~", Toast.LENGTH_SHORT).show();
                } else {
                    HttpChangeNick(changeNickInput.getText().toString());
                }
                break;
        }
    }

    private void HttpChangeNick(String changeNick) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("nick", changeNick);
        HttpUtils.doPost(UrlConstans.MODIFY_NICK, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=========" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功==========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
