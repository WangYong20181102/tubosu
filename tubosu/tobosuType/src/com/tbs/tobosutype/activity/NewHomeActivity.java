package com.tbs.tobosutype.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.NewHomeAdapter;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lie on 2017/10/23.
 */

public class NewHomeActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewHomeAdapter newHomeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewHomeActivity.this;
        TAG = NewHomeActivity.class.getSimpleName();
        initView();
        initDataFromNet();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.newhome_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newhome_swiprefreshlayout);

        //初始化swipeRreshLayout

    }

    private void initDataFromNet(){
        if(Util.isNetAvailable(mContext)){
            HashMap<String, Object> param = new HashMap<String, Object>();
//            param.put("", "");
            OKHttpUtil.post(Constant.NEWHOME_URL, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Util.setErrorLog(TAG, "---onFailure-->>首页请求网络失败--");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Util.setErrorLog(TAG, "---banner结果-->>" + result);

                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(newHomeAdapter!=null){
            newHomeAdapter.stopVerticalMarqueeView();
        }
    }
}
