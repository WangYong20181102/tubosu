package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity._MinePage;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/7 14:28.
 */

public class MyRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<_MinePage.Follow> followList;
    private HashMap<Integer, Boolean> checkMap;

    public static interface OnMyRecommendItemClick {
        void onItemClick(View view);
    }

    public void setOnMyRecommendItemClick(OnMyRecommendItemClick onMyRecommendItemClick) {
        this.onMyRecommendItemClick = onMyRecommendItemClick;
    }

    private OnMyRecommendItemClick onMyRecommendItemClick;

    public MyRecommendAdapter(Context context, List<_MinePage.Follow> followList) {
        this.mContext = context;
        this.followList = followList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend, parent, false);
        MyRecommendViewHolder recommendViewHolder = new MyRecommendViewHolder(view);
        return recommendViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyRecommendViewHolder) {
            GlideUtils.glideLoader(mContext, followList.get(position).getIcon(), 0, R.mipmap.jiazai_loading,
                    ((MyRecommendViewHolder) holder).recommendIcon, GlideUtils.CIRCLE_IMAGE);
            ((MyRecommendViewHolder) holder).recommendName.setText("" + followList.get(position).getNick());
            ((MyRecommendViewHolder) holder).recommendAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进行关注
                    Log.e("MyRecommendAdapter", "点击了子项关注" + position);
                    HttpGetFollw(position);
                    ((MyRecommendViewHolder) holder).recommendAdd.setImageResource(R.mipmap.is_recommend);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    @Override
    public void onClick(View v) {
        if (onMyRecommendItemClick != null) {
            onMyRecommendItemClick.onItemClick(v);
        }
    }

    class MyRecommendViewHolder extends RecyclerView.ViewHolder {
        private ImageView recommendIcon;//推荐用户的头像
        private TextView recommendName;//推荐用户的名称
        private ImageView recommendAdd;//添加推荐用户

        public MyRecommendViewHolder(View itemView) {
            super(itemView);
            recommendIcon = (ImageView) itemView.findViewById(R.id.recommend_icon);
            recommendName = (TextView) itemView.findViewById(R.id.recommend_name);
            recommendAdd = (ImageView) itemView.findViewById(R.id.recommend_add);
        }
    }

    //推荐关注中的请求关注
    private void HttpGetFollw(int position) {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", followList.get(position).getUid());
        param.put("system_type", "1");
        okHttpUtil.post(Constant.FOLLOW_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    Log.e("MyRecommendAdapter", "请求回来的数据" + json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //关注成功
                        Toast.makeText(mContext, "关注成功！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }
}
