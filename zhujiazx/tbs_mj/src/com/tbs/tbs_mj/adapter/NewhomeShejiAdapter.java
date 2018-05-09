package com.tbs.tbs_mj.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.utils.TRoundView;
import com.tbs.tbs_mj.utils.Util;
import java.util.ArrayList;
import com.tbs.tbs_mj.bean._ImageD;

/**
 * Created by Lie on 2017/10/26.
 */

public class NewhomeShejiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<_ImageD> dataList;
    private final int ITEM_ITEM = 4;
    private final int ITEM_RIGHT_FOOT = 6;
    private boolean loadmore = true;


    public NewhomeShejiAdapter(Context context, ArrayList<_ImageD> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        Util.setErrorLog("<><><><><> ", dataList.size() + "<><><><>");
    }


    @Override
    public int getItemViewType(int position) {
        if(position < dataList.size()){
            return ITEM_ITEM;
        }else if(position == dataList.size()){
            return ITEM_RIGHT_FOOT;
        }else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_ITEM){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji, parent, false);
            ShejiHolder holder = new ShejiHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        if(viewType == ITEM_RIGHT_FOOT){
            View view = inflater.inflate(R.layout.newhome_adapter_sheji_foot, parent, false);
            ShejiFoot foot = new ShejiFoot(view);
            return foot;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position<dataList.size()) {
            if (holder instanceof ShejiHolder) {
                ShejiHolder shejiHolder = (ShejiHolder) holder;
                shejiHolder.iv.setType(1);
                Glide.with(context).load(dataList.get(position).getCover_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(shejiHolder.iv);
                shejiHolder.tvTitle.setText(dataList.get(position).getTitle());
                shejiHolder.tvDesc.setText(dataList.get(position).getDesigner_name());

                if(position == 0){ // 左边
                    shejiHolder.sleft_six.setVisibility(View.VISIBLE);
                    shejiHolder.slsix.setVisibility(View.VISIBLE);
                    shejiHolder.sright_six.setVisibility(View.GONE);
                    shejiHolder.srights1ix.setVisibility(View.GONE);
                }else if(position == dataList.size() - 1){  // 右
                    shejiHolder.sleft_six.setVisibility(View.GONE);
                    shejiHolder.slsix.setVisibility(View.VISIBLE);
                    shejiHolder.sright_six.setVisibility(View.VISIBLE);
                    shejiHolder.srights1ix.setVisibility(View.VISIBLE);
                }else{
                    shejiHolder.sleft_six.setVisibility(View.GONE);       //    6
                    shejiHolder.slsix.setVisibility(View.VISIBLE);        //    10
                    shejiHolder.sright_six.setVisibility(View.GONE);      //    10
                    shejiHolder.srights1ix.setVisibility(View.GONE);      //    6
                }

                shejiHolder.itemView.setTag(position);
            }
        }

        if(position == dataList.size()){
            if(holder instanceof ShejiFoot){
                ShejiFoot shejiFoot = (ShejiFoot) holder;
                if(loadmore){
                    shejiFoot.foot.setVisibility(View.VISIBLE);
                }else {
                    shejiFoot.foot.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null? 0 : dataList.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onRecyclerViewItemClick(v,(int)v.getTag());
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void showLoadMore(boolean loadmore){
        this.loadmore = loadmore;
    }

    class ShejiFoot extends RecyclerView.ViewHolder{

        ImageView foot;
        public ShejiFoot(View itemView) {
            super(itemView);
            foot = (ImageView) itemView.findViewById(R.id.iv_sheji_foot);
        }
    }

    class ShejiHolder extends RecyclerView.ViewHolder{
        TRoundView iv;
        TextView tvTitle;
        TextView tvDesc;

        TextView sleft_six;
        TextView slsix;
        TextView sright_six;
        TextView srights1ix;

        public ShejiHolder(View itemView) {
            super(itemView);
            iv = (TRoundView) itemView.findViewById(R.id.newhome_sheji_item_img);
            tvTitle = (TextView) itemView.findViewById(R.id.newhome_sheji_item_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_newhome_sheji_item_desc);

            sleft_six = (TextView) itemView.findViewById(R.id.sleft_six);
            slsix = (TextView) itemView.findViewById(R.id.slsix);
            sright_six = (TextView) itemView.findViewById(R.id.sright_six);
            srights1ix = (TextView) itemView.findViewById(R.id.srights1ix);
        }
    }
}
