package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
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

public class ChangeSiginActivity extends AppCompatActivity {

    @BindView(R.id.change_sigin_back)
    LinearLayout changeSiginBack;
    @BindView(R.id.change_sigin_input)
    EditText changeSiginInput;
    @BindView(R.id.change_sigin_ok)
    TextView changeSiginOk;

    private Context mContext;
    private String TAG = "ChangeSiginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sigin);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.change_sigin_back, R.id.change_sigin_ok})
    public void onViewClickedInChangeSiginActivity(View view) {
        switch (view.getId()) {
            case R.id.change_sigin_back:
                finish();
                break;
            case R.id.change_sigin_ok:
                HttpChangeSigin(changeSiginInput.getText().toString());
                break;
        }
    }

    private void HttpChangeSigin(String sigin) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("personal_signature", sigin);
        HttpUtils.doPost(UrlConstans.MODIFY_PERSONAL_SIGNATURE, param, new Callback() {
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
