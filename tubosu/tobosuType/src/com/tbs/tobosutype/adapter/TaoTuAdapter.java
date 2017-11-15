package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.DanTuJsonItem;
import com.tbs.tobosutype.bean.TaotuJsonItem;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/13.
 */

public class TaoTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = TaoTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TaotuJsonItem.TaotuBean> dataList;
    private int ITEM_BODY_TYPE = 1;
    private int ITEM_FOOT_TYPE = 2;
    private boolean more = false;

    public TaoTuAdapter(Context context, ArrayList<TaotuJsonItem.TaotuBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return ITEM_FOOT_TYPE;
        }else{
            return ITEM_BODY_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_BODY_TYPE){
            View view = inflater.inflate(R.layout.taotu_adapter_item, parent, false);
            TaoTuHolder danTuHolder = new TaoTuHolder(view);
            return danTuHolder;
        }else {
            View foot = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            TaoFootHolder footHolder = new TaoFootHolder(foot);
            return footHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TaoTuHolder){
            TaoTuHolder dan = (TaoTuHolder) holder;

        }

        if(holder instanceof TaoFootHolder){
            TaoFootHolder foot = (TaoFootHolder) holder;
            if(more){
                foot.bar.setVisibility(View.VISIBLE);
                foot.loadText.setVisibility(View.VISIBLE);
            }else{
                foot.bar.setVisibility(View.GONE);
                foot.loadText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size() + 1;
    }


    class TaoTuHolder extends RecyclerView.ViewHolder{
        ImageView dan_img;
        ImageView dan_fav_img;
        TextView tvTaoTextTitle;
        public TaoTuHolder(View itemView) {
            super(itemView);
            dan_img = (ImageView) itemView.findViewById(R.id.tao_img);
            dan_fav_img = (ImageView) itemView.findViewById(R.id.tao_fav_img);
            tvTaoTextTitle = (TextView) itemView.findViewById(R.id.tvTaoTextTitle);
        }
    }

    class TaoFootHolder extends RecyclerView.ViewHolder{
        ProgressBar bar;
        TextView loadText;
        public TaoFootHolder(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            loadText = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }
}
