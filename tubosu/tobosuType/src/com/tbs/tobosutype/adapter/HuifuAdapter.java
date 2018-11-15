package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.AskListBean;
import com.tbs.tobosutype.bean.AskListDataBean;
import com.tbs.tobosutype.bean.ExpandableTextView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.GlideUtils;
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
public class HuifuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExpandableTextView.OnExpandListener, View.OnClickListener {

    private Context context;    //上下文对象
    private List<AskListBean> stringList;   //评论内容集合
    private int etvWidth;
    private Activity activity;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    private OnHuifuClickListener onHuifuClickListener = null;   //回调

    public HuifuAdapter(Context context, AskListDataBean stringList) {
        this.context = context;
        this.stringList = stringList.getCommentList();
        activity = (Activity) context;
    }


    public static interface OnHuifuClickListener {
        void onHuifuClickListen(int position);
    }

    public void setOnHuifuClickListener(OnHuifuClickListener onHuifuClickListener) {
        this.onHuifuClickListener = onHuifuClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.huifu_adapter_layout, parent, false);
        HuifuViewHolder holder = new HuifuViewHolder(view);
        holder.llDianzan.setOnClickListener(this);
        holder.llPinglun.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HuifuViewHolder) {
            GlideUtils.glideLoader(context, stringList.get(position).getIcon(), ((HuifuViewHolder) holder).image_answer_icon);
            ((HuifuViewHolder) holder).tv_answer_name.setText(stringList.get(position).getComment_uid());
            if (etvWidth == 0) {
                ((HuifuViewHolder) holder).expandable_text.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = ((HuifuViewHolder) holder).expandable_text.getWidth();
                    }
                });
            }
            setExpandableTextItem(holder, position);
            //问答时间
            ((HuifuViewHolder) holder).tv_answer_time.setText(stringList.get(position).getAdd_time());
            //评论人
            ((HuifuViewHolder) holder).tv_answer_name.setText(stringList.get(position).getComment_name());
            //用户头像
            GlideUtils.glideLoader(context, stringList.get(position).getIcon(), ((HuifuViewHolder) holder).image_answer_icon);
            ((HuifuViewHolder) holder).llDianzan.setTag(position);
            ((HuifuViewHolder) holder).llPinglun.setTag(position);
            if (stringList.get(position).getIs_agree() == 1) {   //是否点赞：1、是 2、否
                ((HuifuViewHolder) holder).image_dianzan_icon.setImageResource(R.drawable.like_selected);
                ((HuifuViewHolder) holder).tv_dianzan_num.setTextColor(Color.parseColor("#ff6b14"));
            } else {
                ((HuifuViewHolder) holder).image_dianzan_icon.setImageResource(R.drawable.like);
                ((HuifuViewHolder) holder).tv_dianzan_num.setTextColor(Color.parseColor("#6f6f8f"));
            }
            ((HuifuViewHolder) holder).tv_dianzan_num.setText(stringList.get(position).getAgree_count());

        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    private void setExpandableTextItem(RecyclerView.ViewHolder holder, int position) {
        ((HuifuViewHolder) holder).expandable_text.setTag(position - 1);
        ((HuifuViewHolder) holder).expandable_text.setExpandListener(this);
        Integer state = mPositionsAndStates.get(position - 1);
        ((HuifuViewHolder) holder).expandable_text.updateForRecyclerView(stringList.get(position).getMessage(),
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
                ToastUtil.showShort(context, "点击回复按钮" + v.getTag());
                if (onHuifuClickListener != null) {
                    onHuifuClickListener.onHuifuClickListen((Integer) v.getTag());
                }
                break;
            case R.id.ll_dianzan: //点赞按钮
                HttpDianZanRequest((Integer) v.getTag());
                break;
        }
    }

    /**
     * 点赞网络请求
     *
     * @param position
     */
    private void HttpDianZanRequest(final int position) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        params.put("comment_id", stringList.get(position).getComment_uid());
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
                                //处理显示逻辑
                                if (msg.equals("操作成功")) {
                                    //点赞成功将当前的数据模型改变
                                    stringList.get(position).setIs_agree(1);//修改点赞状态
                                    //之前的点赞数量
                                    int dianzanNum = Integer.parseInt(stringList.get(position).getAgree_count());
                                    stringList.get(position).setAgree_count((dianzanNum + 1) + "");
                                    notifyItemChanged(position + 1);
                                } else {
                                    //改变当前的数据模型 取消点赞状态
                                    stringList.get(position).setIs_agree(2);//修改点赞状态
                                    //之前的点赞数量
                                    int dianzanNum = Integer.parseInt(stringList.get(position).getAgree_count());
                                    stringList.get(position).setAgree_count((dianzanNum - 1) + "");
                                    notifyItemChanged(position + 1);
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

    private class HuifuViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_answer_icon;    //头像
        private TextView tv_answer_name;    //回答者姓名
        private ExpandableTextView expandable_text;
        private GridView gv_answer_details;
        private TextView tv_answer_time;    //回答时间
        private LinearLayout llPinglun; //评论父布局
        private ImageView image_huifu;  //评论icon
        private TextView tv_pinglun_num;    //评论数
        private LinearLayout llDianzan; //点赞父布局
        private ImageView image_dianzan_icon;   //点赞icon
        private TextView tv_dianzan_num;    //点赞数
        private TextView tvUnAnswerName;    //被回复者姓名
        private LinearLayout llUnAnswerName;    //被回复者姓名父布局

        public HuifuViewHolder(View itemView) {
            super(itemView);
            image_answer_icon = itemView.findViewById(R.id.image_answer_icon);
            tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
            expandable_text = itemView.findViewById(R.id.expandable_text);
            gv_answer_details = itemView.findViewById(R.id.gv_answer_details);
            tv_answer_time = itemView.findViewById(R.id.tv_answer_time);
            image_huifu = itemView.findViewById(R.id.image_huifu);
            tv_pinglun_num = itemView.findViewById(R.id.tv_pinglun_num);
            image_dianzan_icon = itemView.findViewById(R.id.image_dianzan_icon);
            tv_dianzan_num = itemView.findViewById(R.id.tv_dianzan_num);
            llPinglun = itemView.findViewById(R.id.ll_huifu);
            llDianzan = itemView.findViewById(R.id.ll_dianzan);
            llUnAnswerName = itemView.findViewById(R.id.ll_un_answer);
            tvUnAnswerName = itemView.findViewById(R.id.tv_un_answer_name);
        }
    }

}
