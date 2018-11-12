package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosutype.R;

import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/12 09:40.
 */
public class HuifuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> stringList;
    public HuifuAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.huifu_adapter_layout,parent,false);
        HuifuViewHolder holder = new HuifuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HuifuViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

   private class HuifuViewHolder extends RecyclerView.ViewHolder{
       public HuifuViewHolder(View itemView) {
           super(itemView);
       }
   }

}
