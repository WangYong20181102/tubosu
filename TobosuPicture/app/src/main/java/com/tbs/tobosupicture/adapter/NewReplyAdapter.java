package com.tbs.tobosupicture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DynamicDetailActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._NewReply;
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
 * Created by Mr.Lin on 2017/10/14 09:49.
 */

public class NewReplyAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private String TAG = "NewReplyAdapter";
    private Context mContext;
    private List<_NewReply.ChildCommentBean> childCommentBeanList;
    private _NewReply.CommentBean mCommentBean;
    private boolean isZaning = false;//是否正在点赞处理
    private int adapterState = 1;//适配器的状态
    private Activity mActivity;

    public static interface OnItemClickLister {
        void onItemClick(View view, int position);
    }

    private OnItemClickLister onItemClickLister = null;

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public NewReplyAdapter(Context context, Activity activity, _NewReply.CommentBean commentBean, List<_NewReply.ChildCommentBean> childCommentBeanList) {
        this.mContext = context;
        this.mCommentBean = commentBean;
        this.childCommentBeanList = childCommentBeanList;
        this.mActivity = activity;
    }

    public NewReplyAdapter(Context context, Activity activity, List<_NewReply.ChildCommentBean> childCommentBeanList) {
        this.mContext = context;
        this.mActivity = activity;
        this.childCommentBeanList = childCommentBeanList;
    }

    public void changeAdapterState(int adapterState) {
        this.adapterState = adapterState;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //显示头部
            return 0;
        } else if (position + 1 == getItemCount()) {
            //显示加载更多
            return 1;
        } else {
            return 2;//显示正常的层级
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_reply_head, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(view);
            return headViewHolder;
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_reply_comment_item, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            itemViewHolder.item_new_reply_comment_ll.setOnClickListener(this);
            return itemViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            LoadMoreHolder loadMoreHolder = new LoadMoreHolder(view);
            return loadMoreHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            //设置头像
            GlideUtils.glideLoader(mContext, mCommentBean.getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon,
                    ((HeadViewHolder) holder).item_new_reply_head_icon, 0);
            //头像的点击事件
            ((HeadViewHolder) holder).item_new_reply_head_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", mCommentBean.getUid());
                    intent.putExtra("is_virtual_user", mCommentBean.getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //设置昵称
            ((HeadViewHolder) holder).item_new_reply_head_nick.setText(mCommentBean.getNick());
            //设置内容
            ((HeadViewHolder) holder).item_new_reply_head_comment.setText(mCommentBean.getContent());
            //设置点赞数
            ((HeadViewHolder) holder).item_new_reply_head_zan_num.setText(mCommentBean.getPraise_count());
            //设置回复数
            ((HeadViewHolder) holder).item_new_reply_head_reply_num.setText("回复" + mCommentBean.getReply_count());
            //设置时间
            ((HeadViewHolder) holder).item_new_reply_head_time.setText(mCommentBean.getAdd_time());
            //查看原动态点击事件
            ((HeadViewHolder) holder).item_new_reply_head_goto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", mCommentBean.getDynamic_id());
                    intent.putExtra("commented_uid", mCommentBean.getDynamic_uid());
                    intent.putExtra("is_virtual_user", mCommentBean.getDynamic_user_type());
                    mContext.startActivity(intent);
                }
            });
            //是否点过赞
            if (mCommentBean.getIs_praise().equals("1")) {
                ((HeadViewHolder) holder).item_new_reply_head_zan.setImageResource(R.mipmap.zan_after);
            } else {
                ((HeadViewHolder) holder).item_new_reply_head_zan.setImageResource(R.mipmap.zan2);
            }
            //点赞事件
            ((HeadViewHolder) holder).item_new_reply_head_reply_zan_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isZaning) {
                        if (Utils.userIsLogin(mContext)) {
                            isZaning = true;
                            HttpCommnetZan(mCommentBean.getId(),
                                    ((HeadViewHolder) holder).item_new_reply_head_zan,
                                    ((HeadViewHolder) holder).item_reply_item_comment_zan_add,
                                    ((HeadViewHolder) holder).item_new_reply_head_zan_num);
                        } else {
                            Utils.gotoLogin(mContext);
                        }
                    }
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            //设置用户的回复
            ((ItemViewHolder) holder).item_new_reply_comment_ll.setTag(position);
            //头像
            GlideUtils.glideLoader(mContext,
                    childCommentBeanList.get(position - 1).getIcon(),
                    R.mipmap.default_icon, R.mipmap.default_icon,
                    ((ItemViewHolder) holder).item_new_reply_item_icon, 0);
            //头像点击事件
            ((ItemViewHolder) holder).item_new_reply_item_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", childCommentBeanList.get(position - 1).getUid());
                    intent.putExtra("is_virtual_user", childCommentBeanList.get(position - 1).getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //设置昵称
            ((ItemViewHolder) holder).item_new_reply_item_nick.setText(childCommentBeanList.get(position - 1).getNick());
            //设置回复内容
            if (!TextUtils.isEmpty(childCommentBeanList.get(position - 1).getC_nick())) {
                //有回复对象
                String str = "<font color='#FF882E'> 回复 " + childCommentBeanList.get(position - 1). getC_nick() + "</font>:" + childCommentBeanList.get(position - 1).getContent();
                ((ItemViewHolder) holder).item_new_reply_item_comment.setText(Html.fromHtml(str));
            } else {
                ((ItemViewHolder) holder).item_new_reply_item_comment.setText(childCommentBeanList.get(position - 1).getContent());
            }
            //设置时间
            ((ItemViewHolder) holder).item_new_reply_item_time.setText(childCommentBeanList.get(position - 1).getAdd_time());
        } else if (holder instanceof LoadMoreHolder) {
            if (position == 1) {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
            if (adapterState == 1) {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.VISIBLE);
            } else {
                ((LoadMoreHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((LoadMoreHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return childCommentBeanList != null ? childCommentBeanList.size() + 2 : 2;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    //头部Holder
    class HeadViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_reply_head_icon;//头像
        private ImageView item_new_reply_head_zan;//点赞图标
        private TextView item_new_reply_head_nick;//用户昵称
        private TextView item_reply_item_comment_zan_add;//点赞加一
        private TextView item_new_reply_head_zan_num;//点赞数量
        private TextView item_new_reply_head_reply_num;//回复+回复数量
        private TextView item_new_reply_head_comment;//内容
        private TextView item_new_reply_head_time;//时间
        private TextView item_new_reply_head_goto;//查看原动态
        private RelativeLayout item_new_reply_head_reply_zan_rl;//点赞的层级

        public HeadViewHolder(View itemView) {
            super(itemView);
            item_new_reply_head_icon = (ImageView) itemView.findViewById(R.id.item_new_reply_head_icon);
            item_new_reply_head_zan = (ImageView) itemView.findViewById(R.id.item_new_reply_head_zan);
            item_new_reply_head_nick = (TextView) itemView.findViewById(R.id.item_new_reply_head_nick);
            item_reply_item_comment_zan_add = (TextView) itemView.findViewById(R.id.item_reply_item_comment_zan_add);
            item_new_reply_head_zan_num = (TextView) itemView.findViewById(R.id.item_new_reply_head_zan_num);
            item_new_reply_head_reply_num = (TextView) itemView.findViewById(R.id.item_new_reply_head_reply_num);
            item_new_reply_head_comment = (TextView) itemView.findViewById(R.id.item_new_reply_head_comment);
            item_new_reply_head_time = (TextView) itemView.findViewById(R.id.item_new_reply_head_time);
            item_new_reply_head_goto = (TextView) itemView.findViewById(R.id.item_new_reply_head_goto);
            item_new_reply_head_reply_zan_rl = (RelativeLayout) itemView.findViewById(R.id.item_new_reply_head_reply_zan_rl);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_new_reply_item_icon;//头像
        private TextView item_new_reply_item_nick;//昵称
        private TextView item_new_reply_item_time;//时间
        private TextView item_new_reply_item_comment;//内容
        private LinearLayout item_new_reply_comment_ll;//整个子项的层级


        public ItemViewHolder(View itemView) {
            super(itemView);
            item_new_reply_comment_ll = (LinearLayout) itemView.findViewById(R.id.item_new_reply_comment_ll);
            item_new_reply_item_comment = (TextView) itemView.findViewById(R.id.item_new_reply_item_comment);
            item_new_reply_item_nick = (TextView) itemView.findViewById(R.id.item_new_reply_item_nick);
            item_new_reply_item_time = (TextView) itemView.findViewById(R.id.item_new_reply_item_time);
            item_new_reply_item_icon = (ImageView) itemView.findViewById(R.id.item_new_reply_item_icon);
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mTextView;//显示加载更多的字段

        public LoadMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_more_bar);
            mTextView = (TextView) itemView.findViewById(R.id.load_more_tv);
        }
    }
    //TODO 用户点赞

    /**
     * @param comment_id 内容id
     * @param zan        赞的图标
     * @param tvAdd      显示加一的字段tv
     * @param tvShowNum  显示点赞数值的tv
     */
    private void HttpCommnetZan(String comment_id, final ImageView zan, final TextView tvAdd, final TextView tvShowNum) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Utils.getDateToken());
        param.put("uid", SpUtils.getUserUid(mContext));
        param.put("comment_id", comment_id);
        HttpUtils.doPost(UrlConstans.COMMENT_PRAISE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败===" + e.toString());
                isZaning = false;
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
                        //点赞成功 动画
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
                                    isZaning = false;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isZaning = false;
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
                isZaning = false;
            }
        }, 1000);
    }
}
