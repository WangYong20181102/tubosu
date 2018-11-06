package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.ArticleWebViewActivity;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.Util;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/5 13:54.
 */
public class DecorationQuestionFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> stringList;
    private int n = 4;
    public DecorationQuestionFragmentAdapter(Context context, List<String> list) {
        this.context = context;
        this.stringList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dq_recycleview, parent, false);
        return new DQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DQViewHolder) {
            String str = "<font color = \"#00ff00\">" + stringList.get(position) + "</font>" + "问答";
            //设置文本
            ((DQViewHolder) holder).tvNumAnswer.setText(Html.fromHtml(str));
            if (position == n && position >= 4) {
                ((DQViewHolder) holder).imageAdPhoto.setVisibility(View.VISIBLE);
                n =  n + 5;
            } else {
                ((DQViewHolder) holder).imageAdPhoto.setVisibility(View.GONE);
            }
            ((DQViewHolder) holder).imageAdPhoto.setOnClickListener(new View.OnClickListener() {    //广告图片点击事件
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击image", Toast.LENGTH_LONG).show();
                }
            });
            ((DQViewHolder) holder).rlRvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击父布局" + position, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class DQViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTittle;  //标题
        private TextView tvContext; //内容
        private TextView tvNumAnswer;   //问答个数
        private TextView tvDateTime;    //日期
        private ImageView imageRvRight;    //右侧图片
        private ImageView imageAdPhoto;    //广告图片
        private RelativeLayout rlRvLayout;    //item父布局

        public DQViewHolder(View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.tv_tittle);
            tvContext = itemView.findViewById(R.id.tv_context);
            tvNumAnswer = itemView.findViewById(R.id.tv_num_answer);
            tvDateTime = itemView.findViewById(R.id.tv_datetime);
            imageRvRight = itemView.findViewById(R.id.image_rv_right);
            imageAdPhoto = itemView.findViewById(R.id.image_ad_photo);
            rlRvLayout = itemView.findViewById(R.id.rl_rv_layout);
        }
    }

}
