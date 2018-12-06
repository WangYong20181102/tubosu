package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.bean.AnswerListBean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/12/3 16:08.
 */
public class AskQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AnswerListBean> beanList;

    public AskQuestionAdapter(Context context, List<AnswerListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.askquestion_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder){
            //标题
            ((MyViewHolder) holder).tvTitle.setText(beanList.get(position).getTitle());
            //内容
            ((MyViewHolder) holder).tvContent.setText(beanList.get(position).getAnswer_content());
            //日期
            ((MyViewHolder) holder).tvData.setText(beanList.get(position).getAdd_time());
            //评论数
            ((MyViewHolder) holder).tvAnswerNum.setText(beanList.get(position).getComment_count());
            //点赞数
            ((MyViewHolder) holder).tvlikeNum.setText(beanList.get(position).getAgree_count());
            ((MyViewHolder) holder).llAskQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnswerItemDetailsActivity.class);
                    intent.putExtra("question_id", beanList.get(position).getQuestion_id());
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout llAskQuestion;   //父布局
        private TextView tvTitle;   //标题
        private TextView tvContent;   //内容
        private TextView tvData;   //时间
        private TextView tvAnswerNum;   //回答数
        private TextView tvlikeNum;   //点赞数

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tittle);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvData = itemView.findViewById(R.id.tv_data_time);
            tvAnswerNum = itemView.findViewById(R.id.tv_answer_num);
            tvlikeNum = itemView.findViewById(R.id.tv_like_num);
            llAskQuestion = itemView.findViewById(R.id.ll_ask_question);
        }
    }
}
