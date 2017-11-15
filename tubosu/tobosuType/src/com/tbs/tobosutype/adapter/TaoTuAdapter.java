package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.CompanyBean;
import com.tbs.tobosutype.bean.DanTuJsonItem;
import com.tbs.tobosutype.bean.TaotuEntity;
import com.tbs.tobosutype.bean.TaotuJsonItem;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/13.
 */

public class TaoTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = TaoTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TaotuEntity> dataList;
    private int ITEM_BODY_TYPE = 1;
    private int ITEM_FOOT_TYPE = 2;
    private boolean more = false;
    private boolean isDeleting = false;

    public TaoTuAdapter(Context context, ArrayList<TaotuEntity> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    public void setDeletingStutas(boolean delete){
        isDeleting = delete;
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
            final TaoTuHolder tao = (TaoTuHolder) holder;
            ViewGroup.LayoutParams params = tao.tao_img.getLayoutParams();
            int spWidth = ((Activity)(tao.tao_img.getContext())).getWindowManager().getDefaultDisplay().getWidth();
            params.width = spWidth / 2;
            float imgWidth = dataList.get(position).getImage_width();
            float imgHeight = dataList.get(position).getImage_height();
            params.height = (int) ((imgHeight / imgWidth) * (spWidth / 2));

            int w = spWidth / 2;
            int h = (int) ((imgHeight / imgWidth) * (spWidth / 2));
            tao.tao_img.setLayoutParams(params);

            Glide
                .with(context)
                .load(dataList.get(position).getCover_url())
                .placeholder(R.drawable.iamge_loading)
                .error(R.drawable.iamge_loading).centerCrop()
                .override(w, h)
                .into(tao.tao_img);

            tao.tvTaoTextTitle.setText(dataList.get(position).getTitle());

            if(isDeleting){
                // 在编辑状态下
                tao.tao_fav_img.setVisibility(View.VISIBLE);
                if(dataList.get(position).isSeleteStatus()){
                    tao.tao_fav_img.setBackgroundResource(R.drawable.d_selected);
                }else {
                    tao.tao_fav_img.setBackgroundResource(R.drawable.d_unselect);
                }
            }else {
                tao.tao_fav_img.setVisibility(View.GONE);
            }
            tao.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taotuItemClickListener.OnTaotuItemClickListener(tao.getAdapterPosition(), dataList);
                }
            });


        }

        if(holder instanceof TaoFootHolder){
            TaoFootHolder foot = (TaoFootHolder) holder;
            if(more){
                foot.bar.setVisibility(View.GONE);
                foot.loadText.setVisibility(View.GONE);
            }else{
                foot.bar.setVisibility(View.GONE);
                foot.loadText.setVisibility(View.GONE);
            }
        }
    }

    public void loadMoreData(boolean more){
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size() + 1;
    }


    class TaoTuHolder extends RecyclerView.ViewHolder{
        ImageView tao_img;
        ImageView tao_fav_img;
        TextView tvTaoTextTitle;
        public TaoTuHolder(View itemView) {
            super(itemView);
            tao_img = (ImageView) itemView.findViewById(R.id.tao_img);
            tao_fav_img = (ImageView) itemView.findViewById(R.id.tao_fav_img);
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

    public interface OnTaotuItemClickListener{
        void OnTaotuItemClickListener(int position, ArrayList<TaotuEntity> taotuList);
    }
    public void setTaotuItemClickListener(OnTaotuItemClickListener taotuItemClickListener) {
        this.taotuItemClickListener = taotuItemClickListener;
    }

    private OnTaotuItemClickListener taotuItemClickListener;
}
