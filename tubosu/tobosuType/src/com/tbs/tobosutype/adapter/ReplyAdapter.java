package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.NewLoginActivity;
import com.tbs.tobosutype.bean.AskListBean;
import com.tbs.tobosutype.bean.AskListDataBean;
import com.tbs.tobosutype.bean.ExpandableTextView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.SharePreferenceUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/12 09:40.
 */
public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExpandableTextView.OnExpandListener, View.OnClickListener {

    private Context context;    //上下文对象
    private List<AskListBean> stringList;   //评论内容集合
    private int etvWidth;
    private Activity activity;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    private OnReplyClickListener onReplyClickListener = null;   //回调
    private boolean isLike = false;

    public ReplyAdapter(Context context, AskListDataBean stringList) {
        this.context = context;
        this.stringList = stringList.getCommentList();
        activity = (Activity) context;
    }


    public static interface OnReplyClickListener {
        void onHuifuClickListen(int position);
    }

    public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.huifu_adapter_layout, parent, false);
        ReplyViewHolder holder = new ReplyViewHolder(view);
        holder.llDianzan.setOnClickListener(this);
        holder.llPinglun.setOnClickListener(this);
        holder.tv_answer_name.setOnClickListener(this);
        holder.image_answer_icon.setOnClickListener(this);
        holder.expandable_text.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).llDianzan.setTag(position);
            ((ReplyViewHolder) holder).llPinglun.setTag(position);
            ((ReplyViewHolder) holder).tv_answer_name.setTag(position);
            ((ReplyViewHolder) holder).image_answer_icon.setTag(position);
            ((ReplyViewHolder) holder).expandable_text.setTag(position);
            GlideUtils.glideLoader(context, stringList.get(position).getIcon(), ((ReplyViewHolder) holder).image_answer_icon);
            ((ReplyViewHolder) holder).tv_answer_name.setText(stringList.get(position).getComment_uid());
            if (etvWidth == 0) {
                ((ReplyViewHolder) holder).expandable_text.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = ((ReplyViewHolder) holder).expandable_text.getWidth();
                    }
                });
            }
            setExpandableTextItem(holder, position);
            //问答时间
            ((ReplyViewHolder) holder).tv_answer_time.setText(stringList.get(position).getAdd_time());
            //评论人
            ((ReplyViewHolder) holder).tv_answer_name.setText(stringList.get(position).getComment_name());
            //用户头像
            GlideUtils.glideLoader(context, stringList.get(position).getIcon(), ((ReplyViewHolder) holder).image_answer_icon);
            //回复被评论人
            if (stringList.get(position).getComment_id().equals("0")) {
                ((ReplyViewHolder) holder).llUnAnswerName.setVisibility(View.GONE);
            } else {
                ((ReplyViewHolder) holder).llUnAnswerName.setVisibility(View.VISIBLE);
                ((ReplyViewHolder) holder).tvUnAnswerName.setText(stringList.get(position).getRecomment_name());
            }
            if (stringList.get(position).getIs_agree() == 1) {   //是否点赞：1、是 2、否
                ((ReplyViewHolder) holder).image_dianzan_icon.setImageResource(R.drawable.like_selected);
                ((ReplyViewHolder) holder).tv_dianzan_num.setTextColor(Color.parseColor("#ff6b14"));
            } else {
                ((ReplyViewHolder) holder).image_dianzan_icon.setImageResource(R.drawable.like);
                ((ReplyViewHolder) holder).tv_dianzan_num.setTextColor(Color.parseColor("#6f6f8f"));
            }
            //点赞个数
            ((ReplyViewHolder) holder).tv_dianzan_num.setText((stringList.get(position).getAgree_count().equals("0") || stringList.get(position).getAgree_count().isEmpty()) ? "点赞" : stringList.get(position).getAgree_count());

        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    private void setExpandableTextItem(RecyclerView.ViewHolder holder, int position) {
        ((ReplyViewHolder) holder).expandable_text.setTag(position - 1);
        ((ReplyViewHolder) holder).expandable_text.setExpandListener(this);
        Integer state = mPositionsAndStates.get(position - 1);
        ((ReplyViewHolder) holder).expandable_text.updateForRecyclerView(stringList.get(position).getMessage(),
                etvWidth, state == null ? 0 : state);//第一次getview时肯定为etvWidth为0
    }

    @Override
    public void onExpand(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_huifu: //回复按钮
            case R.id.tv_answer_name://评论者
            case R.id.image_answer_icon://评论者头像
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(activity))) {
                    Toast.makeText(activity, "您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, NewLoginActivity.class);
                    activity.startActivityForResult(intent, 0);
                    return;
                }
                if (onReplyClickListener != null) {
                    onReplyClickListener.onHuifuClickListen((Integer) v.getTag());
                }
                break;
            case R.id.ll_dianzan: //点赞按钮
                if (TextUtils.isEmpty(AppInfoUtil.getTypeid(context))) {//未登录状态下点赞逻辑处理
                    if (!isLike) {
                        isLike = true;
                        //点赞成功将当前的数据模型改变
                        stringList.get((Integer) v.getTag()).setIs_agree(1);//修改点赞状态
                        //之前的点赞数量
                        int dianzanNum;
                        if (stringList.get((Integer) v.getTag()).getAgree_count().trim().isEmpty()) {
                            dianzanNum = 0;
                        } else {
                            dianzanNum = Integer.parseInt(stringList.get((Integer) v.getTag()).getAgree_count());
                        }
                        stringList.get((Integer) v.getTag()).setAgree_count((dianzanNum + 1) + "");
                        notifyItemChanged((Integer) v.getTag());
                        HttpDianZanRequest((Integer) v.getTag(), false);
                    } else {
                        isLike = false;
                        //改变当前的数据模型 取消点赞状态
                        stringList.get((Integer) v.getTag()).setIs_agree(2);//修改点赞状态
                        //之前的点赞数量
                        int dianzanNum = 0;
                        if (stringList.get((Integer) v.getTag()).getAgree_count().trim().isEmpty()) {
                            dianzanNum = 0;
                        } else {
                            dianzanNum = Integer.parseInt(stringList.get((Integer) v.getTag()).getAgree_count());
                        }
                        stringList.get((Integer) v.getTag()).setAgree_count((dianzanNum - 1) + "");
                        notifyItemChanged((Integer) v.getTag());
                    }
                    return;
                }
                HttpDianZanRequest((Integer) v.getTag(), true);
                break;
        }
    }

    /**
     * 点赞网络请求
     *
     * @param position
     * @param b
     */
    private void HttpDianZanRequest(final int position, final boolean b) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("comment_id", stringList.get(position).getAnswer_comment_id());
        params.put("agree_uid", AppInfoUtil.getUserid(context));
        OKHttpUtil.post(Constant.ASK_ANSWER_COMMENT_AGREE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        final String msg = jsonObject.optString("msg");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    //处理显示逻辑
                                    if (msg.equals("点赞成功")) {
                                        //点赞成功将当前的数据模型改变
                                        stringList.get(position).setIs_agree(1);//修改点赞状态
                                        //之前的点赞数量
                                        int dianzanNum;
                                        if (stringList.get(position).getAgree_count().trim().isEmpty()) {
                                            dianzanNum = 0;
                                        } else {
                                            dianzanNum = Integer.parseInt(stringList.get(position).getAgree_count());
                                        }
                                        stringList.get(position).setAgree_count((dianzanNum + 1) + "");
                                        notifyItemChanged(position);
                                    } else {
                                        //改变当前的数据模型 取消点赞状态
                                        stringList.get(position).setIs_agree(2);//修改点赞状态
                                        //之前的点赞数量
                                        int dianzanNum;
                                        if (stringList.get(position).getAgree_count().trim().isEmpty()) {
                                            dianzanNum = 0;
                                        } else {
                                            dianzanNum = Integer.parseInt(stringList.get(position).getAgree_count());
                                        }
                                        stringList.get(position).setAgree_count((dianzanNum - 1) + "");
                                        notifyItemChanged(position);
                                    }
                                }
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(context, jsonObject.optString("msg"));
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class ReplyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_answer_icon;    //头像
        private TextView tv_answer_name;    //回答者姓名
        private ExpandableTextView expandable_text;
        private TextView tv_answer_time;    //回答时间
        private LinearLayout llPinglun; //评论父布局
        private LinearLayout llDianzan; //点赞父布局
        private ImageView image_dianzan_icon;   //点赞icon
        private TextView tv_dianzan_num;    //点赞数
        private TextView tvUnAnswerName;    //被回复者姓名
        private LinearLayout llUnAnswerName;    //被回复者姓名父布局

        public ReplyViewHolder(View itemView) {
            super(itemView);
            image_answer_icon = itemView.findViewById(R.id.image_answer_icon);
            tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
            expandable_text = itemView.findViewById(R.id.expandable_text);
            tv_answer_time = itemView.findViewById(R.id.tv_answer_time);
            image_dianzan_icon = itemView.findViewById(R.id.image_dianzan_icon);
            tv_dianzan_num = itemView.findViewById(R.id.tv_dianzan_num);
            llPinglun = itemView.findViewById(R.id.ll_huifu);
            llDianzan = itemView.findViewById(R.id.ll_dianzan);
            llUnAnswerName = itemView.findViewById(R.id.ll_un_answer);
            tvUnAnswerName = itemView.findViewById(R.id.tv_un_answer_name);
        }
    }

}
