package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.database.DBManager;
import com.tobosu.mydecorate.entity.MessageData;
import com.tobosu.mydecorate.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/13.
 *
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private Context mContext;

    private LayoutInflater inflater = null;

    private ArrayList<MessageData> data = new ArrayList<MessageData>();

    private DBManager manager = null;

    private ArrayList<HashMap<String, String>> flagList = new ArrayList<HashMap<String, String>>();


    public MessageAdapter(Context mContext, ArrayList<MessageData> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater = LayoutInflater.from(mContext);

        if(manager==null){
            manager = DBManager.getInstance(mContext);
        }
        flagList = manager.queryMessageData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建新View，被LayoutManager所调用
        View view = inflater.inflate(R.layout.item_message_adapter_layout, null, false);
        MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据与界面进行绑定的操作

        holder.msg_text_title.setText(data.get(position).getArticleTitle());
        holder.msg_text_description.setText(data.get(position).getDescription());
        holder.msg_text_time.setText(data.get(position).getTime() + data.get(position).getTimeUnit());

        if(flagList.size()>0){
            if(flagList.get(position).get("read_flag").equals("0")){
                holder.msg_read_flag.setVisibility(View.VISIBLE);
            }else {
                holder.msg_read_flag.setVisibility(View.GONE);
            }
        }

        //公司头像
        Picasso.with(mContext)
                .load(data.get(position).getPicUrl())
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(holder.msg_picture);
        holder.itemView.setTag(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onRecyclerViewItemClick(v, (MessageData) v.getTag());
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg_text_title;
        ImageView msg_read_flag;
        TextView msg_text_time;
        TextView msg_text_description;
        RoundImageView msg_picture;

        public ViewHolder(View itemView) {
            super(itemView);

            msg_text_title = (TextView) itemView.findViewById(R.id.msg_title);
            msg_read_flag = (ImageView) itemView.findViewById(R.id.msg_iv_read_flag);
            msg_text_time = (TextView) itemView.findViewById(R.id.msg_time);
            msg_text_description = (TextView) itemView.findViewById(R.id.msg_text_description);
            msg_picture = (RoundImageView) itemView.findViewById(R.id.msg_head);

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, MessageData data);
    }
}