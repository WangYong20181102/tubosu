package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.AskQuestionBean;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/12/3 15:23.
 */
public class ReplyFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AskQuestionBean> questionBeanList;
    public ReplyFragmentAdapter(Context context, List<AskQuestionBean> questionBeanList) {
        this.context = context;
        this.questionBeanList = questionBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reply_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            //标题
            ((MyViewHolder) holder).tvTittle.setText(questionBeanList.get(position).getTitle());
            //回答总数
            ((MyViewHolder) holder).tvAnswerNum.setText(questionBeanList.get(position).getAnswer_count());
            //浏览总数
            ((MyViewHolder) holder).tvViewNum.setText(questionBeanList.get(position).getView_count());
            //时间
            ((MyViewHolder) holder).tvDataTime.setText(questionBeanList.get(position).getAdd_time());
        }
    }

    @Override
    public int getItemCount() {
        return questionBeanList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTittle;  //标题
        private TextView tvAnswerNum;  //回答数
        private TextView tvViewNum;  //浏览数
        private TextView tvDataTime;  //时间

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.tv_tittle);
            tvAnswerNum = itemView.findViewById(R.id.tv_answer_num);
            tvViewNum = itemView.findViewById(R.id.tv_view_num);
            tvDataTime = itemView.findViewById(R.id.tv_data_time);
        }
    }
}
