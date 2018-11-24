package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.activity.NewWebViewActivity;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/5 13:54.
 */
public class DecorationQuestionFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文
     */
    private Context context;
    private List<AskQuestionBean> stringList;

    public DecorationQuestionFragmentAdapter(Context context, List<AskQuestionBean> list) {
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
            //设置文本
            ((DQViewHolder) holder).tvNumAnswer.setText(stringList.get(position).getAnswer_count());
            //设置标题
            ((DQViewHolder) holder).tvTittle.setText(stringList.get(position).getTitle());
            //设置内容
            ((DQViewHolder) holder).tvContext.setText(stringList.get(position).getContent());
            //设置图片
            if (!stringList.get(position).getImg_urls()[0].trim().isEmpty() && stringList.get(position).getImg_urls() != null) {
                ((DQViewHolder) holder).cardViewImage.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(context, stringList.get(position).getImg_urls()[0], ((DQViewHolder) holder).imageRvRight);
            } else {
                ((DQViewHolder) holder).cardViewImage.setVisibility(View.GONE);
            }
            //判断，如果有广告图片就隐藏广告图上方横线
            if (position < stringList.size() - 1) {
                if (stringList.get(position + 1).getTitle().isEmpty()){
                    ((DQViewHolder) holder).viewBottomLine.setVisibility(View.GONE);
                }else {
                    ((DQViewHolder) holder).viewBottomLine.setVisibility(View.VISIBLE);
                }
            }
            //设置广告图片
            if (stringList.get(position).getTitle().isEmpty()) {
                ((DQViewHolder) holder).rlRvLayout.setVisibility(View.GONE);
                ((DQViewHolder) holder).cardViewAdImage.setVisibility(View.VISIBLE);
                GlideUtils.glideLoader(context, stringList.get(position).getImg_urls()[0], ((DQViewHolder) holder).imageAdPhoto);
            } else {
                ((DQViewHolder) holder).rlRvLayout.setVisibility(View.VISIBLE);
                ((DQViewHolder) holder).cardViewAdImage.setVisibility(View.GONE);
            }
            ((DQViewHolder) holder).tvDateTime.setText(stringList.get(position).getAdd_time());

            ((DQViewHolder) holder).cardViewAdImage.setOnClickListener(new View.OnClickListener() {    //广告图片点击事件
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewWebViewActivity.class);
                    intent.putExtra("mLoadingUrl", stringList.get(position).getContent());
                    intent.putExtra("bAnswer", true);
                    context.startActivity(intent);
                }
            });
            ((DQViewHolder) holder).rlRvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnswerItemDetailsActivity.class);
                    intent.putExtra("question_id", stringList.get(position).getQuestion_id());
                    context.startActivity(intent);

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
        private CardView cardViewImage;    //右侧图片   父布局
        private ImageView imageAdPhoto;    //广告图片
        private CardView cardViewAdImage;    //广告图片父布局
        private RelativeLayout rlRvLayout;    //item父布局
        private View viewBottomLine;    //下划线

        public DQViewHolder(View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.tv_tittle);
            tvContext = itemView.findViewById(R.id.tv_context);
            tvNumAnswer = itemView.findViewById(R.id.tv_num_answer);
            tvDateTime = itemView.findViewById(R.id.tv_datetime);
            imageRvRight = itemView.findViewById(R.id.image_rv_right);
            cardViewImage = itemView.findViewById(R.id.cardview_image);
            imageAdPhoto = itemView.findViewById(R.id.image_ad_photo);
            cardViewAdImage = itemView.findViewById(R.id.cardview_ad_image);
            rlRvLayout = itemView.findViewById(R.id.rl_rv_layout);
            viewBottomLine = itemView.findViewById(R.id.view_bottom_line);
        }
    }

}
