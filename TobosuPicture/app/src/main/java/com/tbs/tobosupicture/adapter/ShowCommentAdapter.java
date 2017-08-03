package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.LoginActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.activity.ReplyActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2017/7/27 18:54.
 */

public class ShowCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private Activity mActivity;
    private List<_DynamicDetail.Comment> commentArrayList;
    private String TAG = "ShowCommentAdapter";
    private String dynamic_id;

    public ShowCommentAdapter(Context context, Activity activity, String dynamic_id, List<_DynamicDetail.Comment> commentList) {
        this.mContext = context;
        this.mActivity = activity;
        this.commentArrayList = commentList;
        this.dynamic_id = dynamic_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommentViewHolder) {
            //头像
            GlideUtils.glideLoader(mContext, commentArrayList.get(position).getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((CommentViewHolder) holder).commentIcon, 0);
            //头像点击事件
            ((CommentViewHolder) holder).commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", commentArrayList.get(position).getUid());
                    intent.putExtra("is_virtual_user", commentArrayList.get(position).getIs_virtual_user());
                    mContext.startActivity(intent);
                }
            });
            //点赞事件
            ((CommentViewHolder) holder).dynamic_detail_comment_ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.userIsLogin(mContext)) {
                        HttpCommentZan(commentArrayList.get(position).getId(),
                                commentArrayList.get(position).getUid(),
                                ((CommentViewHolder) holder).commentZan,
                                ((CommentViewHolder) holder).dynamic_detail_comment_zan_add,
                                ((CommentViewHolder) holder).commentZanNum);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });
            //昵称
            ((CommentViewHolder) holder).commentNick.setText("" + commentArrayList.get(position).getNick());
            //时间
            ((CommentViewHolder) holder).commentTime.setText("" + commentArrayList.get(position).getAdd_time());
            //点赞数量
            ((CommentViewHolder) holder).commentZanNum.setText("" + commentArrayList.get(position).getPraise_count());
            //回复数量
            ((CommentViewHolder) holder).commentRevert.setText("" + commentArrayList.get(position).getReply_count() + "回复");
            //回复的点击事件
            ((CommentViewHolder) holder).commentRevert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReplyActivity.class);
                    Log.e(TAG, "检测回复的id====" + commentArrayList.get(position).getId());
                    intent.putExtra("comment_id", commentArrayList.get(position).getId());
                    intent.putExtra("dynamic_id", dynamic_id);
                    mContext.startActivity(intent);
                }
            });
            //回复的内容
            ((CommentViewHolder) holder).commentTitle.setText("" + commentArrayList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList != null ? commentArrayList.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView commentIcon;//头像
        private TextView commentNick;//昵称
        private TextView commentTitle;//内容
        private TextView commentTime;//回复时间
        private TextView commentRevert;//回复按钮
        private ImageView commentZan;//点赞图标
        private TextView commentZanNum;//点赞数
        private TextView dynamic_detail_comment_zan_add;//点赞数

        private LinearLayout dynamic_detail_comment_ll_zan;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentIcon = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_icon);
            commentZan = (ImageView) itemView.findViewById(R.id.dynamic_detail_comment_zan);
            commentNick = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_nick);
            commentTitle = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_title);
            commentTime = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_time);
            commentRevert = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_revert);
            commentZanNum = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_zannum);
            dynamic_detail_comment_zan_add = (TextView) itemView.findViewById(R.id.dynamic_detail_comment_zan_add);
            dynamic_detail_comment_ll_zan = (LinearLayout) itemView.findViewById(R.id.dynamic_detail_comment_ll_zan);

        }
    }

    //TODO 对用户得回复进行点赞 当前用户的id暂时固定
    private void HttpCommentZan(String comment_id, String praised_uid, final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("comment_id", comment_id);
        param.put("praised_uid", praised_uid);
        HttpUtils.doPost(UrlConstans.COMMENT_PRAISE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "链接成功===" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //点赞成功
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (msg.equals("点赞成功")) {
                                    zan.setImageResource(R.mipmap.zan_after);
                                    zanAddAnimation(tvAdd, tvShowNum);
                                } else {
                                    int num = Integer.parseInt(tvShowNum.getText().toString());
                                    int numAddone = num - 1;
                                    tvShowNum.setText("" + numAddone);
                                    zan.setImageResource(R.mipmap.zan2);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点赞数加一的动画效果
    private void zanAddAnimation(final TextView tvAdd, final TextView showNum) {
        tvAdd.setVisibility(View.VISIBLE);
        tvAdd.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAdd.setVisibility(View.GONE);
                int num = Integer.parseInt(showNum.getText().toString());
                int numAddone = num + 1;
                showNum.setText("" + numAddone);
            }
        }, 1000);
    }
}
