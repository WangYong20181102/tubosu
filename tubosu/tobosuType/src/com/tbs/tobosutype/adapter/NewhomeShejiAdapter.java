package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;

import java.util.List;

/**
 * Created by Lie on 2017/10/26.
 */

public class NewhomeShejiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.ImpressionBean> dataList;
    private final int ITEM_ITEM = 2;
    private final int ITEM_RIGHT_FOOT =3;


    public NewhomeShejiAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.ImpressionBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() + 1){
            return ITEM_RIGHT_FOOT;
        }else {
            return ITEM_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_ITEM){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji,parent, false);
            ShejiHolder holder = new ShejiHolder(view);
            return holder;
        }

        if(viewType == ITEM_RIGHT_FOOT){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji,parent, false);
            ShejiHolder holder = new ShejiHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ShejiHolder extends RecyclerView.ViewHolder{


        public ShejiHolder(View itemView) {
            super(itemView);
        }
    }
}
