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
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._MyFriend;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/7/15 17:00.
 * 我的图友 适配器  涉及到点击头像进入个人主页 所以在适配器中完成点击事件的逻辑
 */

public class MyFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private String TAG = "MyFriendAdapter";
    private ArrayList<_MyFriend> myFriendList;
    private int adapterLoadState = 1;//1.加载更多  2.恢复正常状态
    private Activity mActivity;

    public MyFriendAdapter(Context context, Activity activity, ArrayList<_MyFriend> myFriends) {
        this.mContext = context;
        this.myFriendList = myFriends;
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
            MyFriendsViewHolder holder = new MyFriendsViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else {
            //加载更多图层
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            MyFriendLoadMore loadMoreHolder = new MyFriendLoadMore(view);
            return loadMoreHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyFriendsViewHolder) {
            //TODO 数据的适配 当个性签名为空时要判断 将个性签名的栏目隐藏 添加加为图友的按钮点击事件 思考一下点击头像事件以及图友添加事件写在adapter中
            //设置头像
            GlideUtils.glideLoader(mContext, myFriendList.get(position).getIcon(), 0, 0, ((MyFriendsViewHolder) holder).myfriendIcon, 0);
            //设置头像点击事件
            ((MyFriendsViewHolder) holder).myfriendIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", myFriendList.get(position).getUid());
                    intent.putExtra("is_virtual_user", myFriendList.get(position).getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //设置名称
            ((MyFriendsViewHolder) holder).myfriendName.setText(myFriendList.get(position).getNick());
            //设置个性签名
            if (TextUtils.isEmpty(myFriendList.get(position).getPersonal_signature())) {
                ((MyFriendsViewHolder) holder).myfriendSign.setVisibility(View.GONE);
            } else {
                ((MyFriendsViewHolder) holder).myfriendSign.setVisibility(View.VISIBLE);
                ((MyFriendsViewHolder) holder).myfriendSign.setText(myFriendList.get(position).getPersonal_signature());
            }
            //设置和“我”的关系
            if (myFriendList.get(position).getIs_friends().equals("1")) {
                //互为好友关系
                ((MyFriendsViewHolder) holder).myfriendAdd.setVisibility(View.VISIBLE);
                ((MyFriendsViewHolder) holder).myfriendAdd.setText("⇆ 互为图友");
                ((MyFriendsViewHolder) holder).myfriendAdd.setBackgroundResource(R.drawable.shape_btn_gray);
                ((MyFriendsViewHolder) holder).myfriendAdd.setTextColor(Color.parseColor("#8a8f99"));
                ((MyFriendsViewHolder) holder).myfriendAdd.setClickable(false);
            } else {
                ((MyFriendsViewHolder) holder).myfriendAdd.setVisibility(View.GONE);
            }
        } else if (holder instanceof MyFriendLoadMore) {
            if (position == 0) {
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
            if (adapterLoadState == 1) {
                //显示加载更多
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.VISIBLE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.VISIBLE);
            } else {
                ((MyFriendLoadMore) holder).mTextView.setVisibility(View.GONE);
                ((MyFriendLoadMore) holder).mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myFriendList != null ? myFriendList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onMyFansItemClickLister != null && !myFriendList.isEmpty()) {
            onMyFansItemClickLister.onItemClick(v);
        }
    }

    //显示当前粉丝的列表子项
    class MyFriendsViewHolder extends RecyclerView.ViewHolder {
        private ImageView myfriendIcon;//头像
        private TextView myfriendName;//名字
        private TextView myfriendSign;//签名
        private TextView myfriendAdd;//已为图友

        public MyFriendsViewHolder(View itemView) {
            super(itemView);
            myfriendIcon = (ImageView) itemView.findViewById(R.id.item_myfans_icon);
            myfriendName = (TextView) itemView.findViewById(R.id.item_myfans_name);
            myfriendSign = (TextView) itemView.findViewById(R.id.item_myfans_sign);
            myfriendAdd = (TextView) itemView.findViewById(R.id.item_myfans_add);
        }
    }

    //加载更多
    class MyFriendLoadMore extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public MyFriendLoadMore(View itemView) {
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
                                myFriendList.get(position).setIs_friends("1");
                                Log.e(TAG, "修改列表的数据===位置===" + position + "==是否互为好友==" + myFriendList.get(position).getIs_friends());
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
