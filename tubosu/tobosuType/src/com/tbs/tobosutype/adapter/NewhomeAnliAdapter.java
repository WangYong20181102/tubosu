package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import java.util.List;

/**
 * Created by Lie on 2017/10/25.
 */

public class NewhomeAnliAdapter extends RecyclerView.Adapter<NewhomeAnliAdapter.CaseViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.CasesBean> casesBeenList;


    public NewhomeAnliAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.CasesBean> casesBeenList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.casesBeenList = casesBeenList;
    }

    @Override
    public CaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_newhome_anli_item, parent, false);
        CaseViewHolder holder = new CaseViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CaseViewHolder holder, int position) {
        Glide.with(context).load(casesBeenList.get(position).getCover_url()).error(R.drawable.new_home_loading).placeholder(R.drawable.new_home_loading).into(holder.iv);
        holder.title.setText(casesBeenList.get(position).getCommunity_name());
        holder.name.setText(casesBeenList.get(position).getOwner_name());
        holder.desc.setText(casesBeenList.get(position).getSub_title());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return casesBeenList == null?0:casesBeenList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnCaseClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnCaseClickListener.onCaseClickListener(v,(int)v.getTag());
        }
    }


    class CaseViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView title;
        TextView name;
        TextView desc;

        public CaseViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.newhome_anli_item_img);
            title = (TextView) itemView.findViewById(R.id.newhome_anli_item_title);
            name = (TextView) itemView.findViewById(R.id.newhome_anli_item_name);
            desc = (TextView) itemView.findViewById(R.id.newhome_anli_item_desc);
        }
    }


    public interface OnCaseClickListener{
        void onCaseClickListener(View parent, int postion);
    }

    public OnCaseClickListener getmOnCaseClickListener() {
        return mOnCaseClickListener;
    }

    public void setmOnCaseClickListener(OnCaseClickListener mOnCaseClickListener) {
        this.mOnCaseClickListener = mOnCaseClickListener;
    }

    private OnCaseClickListener mOnCaseClickListener;



}
