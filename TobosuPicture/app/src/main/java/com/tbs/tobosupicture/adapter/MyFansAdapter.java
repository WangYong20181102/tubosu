package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._MyFans;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/7/12 16:46.
 * 我的图谜适配器
 */

public class MyFansAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "MyFansAdapter";
    private ArrayList<_MyFans> myFansList;
    private int adapterLoadState = 1;//1.加载更多  2.恢复正常状态
    private Activity mActivity;

    public MyFansAdapter(Context context, Activity activity, ArrayList<_MyFans> myFansList) {
        this.mContext = context;
        this.myFansList = myFansList;
        this.mActivity = activity;
    }

    //图层变换 加载更多图层
    public void changLoadState(int state) {
        this.adapterLoadState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//普通图层
        }
    }

    public static interface OnMyFansItemClickLister {
        void onItemClick(View view);
    }

    private OnMyFansItemClickLister onMyFansItemClickLister;

    public void setOnMyFansItemClickLister(OnMyFansItemClickLister onMyFansItemClickLister) {
        this.onMyFansItemClickLister = onMyFansItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //正常图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_fans, parent, false);
            MyFansViewHolder holder = new MyFansViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else {
            //加载更多图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyFansLoadMore loadMoreHolder = new MyFansLoadMore(view);
            return loadMoreHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyFansViewHolder) {
            //TODO 数据的适配 当个性签名为空时要判断 将个性签名的栏目隐藏 添加加为图友的按钮点击事件 思考一下点击头像事件以及图友添加事件写在adapter中
            //设置头像
            GlideUtils.glideLoader(mContext, myFansList.get(position).getIcon(), 0, 0, ((MyFansViewHolder) holder).myfansIcon, 0);
            //设置头像点击事件
            ((MyFansViewHolder) holder).myfansIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", myFansList.get(position).getUid());
                    intent.putExtra("is_virtual_user", myFansList.get(position).getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //设置名称
            ((MyFansViewHolder) holder).myfansName.setText(myFansList.get(position).getNick());
            //设置个性签名
            if (TextUtils.isEmpty(myFansList.get(position).getPersonal_signature())) {
                ((MyFansViewHolder) holder).myfansSign.setVisibility(View.GONE);
            } else {
                ((MyFansViewHolder) holder).myfansSign.setText(myFansList.get(position).getPersonal_signature());
            }
            //设置和“我”的关系
            if (myFansList.get(position).getIs_friends().equals("1")) {
                //互为好友关系
                ((MyFansViewHolder) holder).myfansAdd.setText("⇆ 互为图友");
                ((MyFansViewHolder) holder).myfansAdd.setBackgroundResource(R.drawable.shape_btn_gray);
                ((MyFansViewHolder) holder).myfansAdd.setTextColor(Color.parseColor("#8a8f99"));
                ((MyFansViewHolder) holder).myfansAdd.setClickable(false);
            } else {
                //加为图友
                ((MyFansViewHolder) holder).myfansAdd.setText("+ 加为图友");
                ((MyFansViewHolder) holder).myfansAdd.setTextColor(Color.parseColor("#ff882e"));
                ((MyFansViewHolder) holder).myfansAdd.setBackgroundResource(R.drawable.shape_btn_yellow);
                ((MyFansViewHolder) holder).myfansAdd.setClickable(true);
                ((MyFansViewHolder) holder).myfansAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进行加为图友请求
                        if (Utils.userIsLogin(mContext)) {
                            HttpAddFrend(myFansList.get(position).getUid(), myFansList.get(position).getUser_type(), ((MyFansViewHolder) holder).myfansAdd, position);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        } else if (holder instanceof MyFansLoadMore) {
            if (position == 0) {
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                //显示加载更多
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
            } else {
                ((MyFansLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFansLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myFansList != null ? myFansList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onMyFansItemClickLister != null && !myFansList.isEmpty()) {
            onMyFansItemClickLister.onItemClick(v);
        }
    }

    //显示当前粉丝的列表子项
    class MyFansViewHolder extends RecyclerView.ViewHolder {
        private ImageView myfansIcon;//我的粉丝头像
        private TextView myfansName;//我的粉丝名字
        private TextView myfansSign;//我的粉丝签名
        private TextView myfansAdd;//添加图谜或者显示是否已为图友

        public MyFansViewHolder(View itemView) {
            super(itemView);
            myfansIcon = (ImageView) itemView.findViewById(R.id.item_myfans_icon);
            myfansName = (TextView) itemView.findViewById(R.id.item_myfans_name);
            myfansSign = (TextView) itemView.findViewById(R.id.item_myfans_sign);
            myfansAdd = (TextView) itemView.findViewById(R.id.item_myfans_add);
        }
    }

    //加载更多
    class MyFansLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyFansLoadMore(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }

    //请求加为图友的接口
    private void HttpAddFrend(String followed_id, String followed_user_type, final TextView addTv, final int position) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("user_type", "2");
        param.put("followed_id", followed_id);
        param.put("followed_user_type", followed_user_type);
        param.put("type", "1");
        HttpUtils.doPost(UrlConstans.USER_FOLLOW, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        //添加图友成功 将状态变成“已互为图友”
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addTv.setText("⇆ 互为图友");
                                addTv.setBackgroundResource(R.drawable.shape_btn_gray);
                                addTv.setTextColor(Color.parseColor("#8a8f99"));
                                addTv.setClickable(false);
                                Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                                myFansList.get(position).setIs_friends("1");
                                Log.e(TAG, "修改列表的数据===位置===" + position + "==是否互为好友==" + myFansList.get(position).getIs_friends());
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
