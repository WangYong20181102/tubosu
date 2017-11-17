package com.tbs.tobosutype.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.DantuEntity;
import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/13.
 */

public class DanTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = DanTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DantuEntity> dataList;
    private boolean more = false;
    private boolean isDeleting = false;

    public DanTuAdapter(Context context, ArrayList<DantuEntity> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position == getItemCount()-1){
//            return ITEM_FOOT_TYPE;
//        }else{
//            return ITEM_BODY_TYPE;
//        }
//    }

    public void setDeletingStutas(boolean delete){
        isDeleting = delete;
        for(int i=0;i<dataList.size();i++){
            dataList.get(i).setSeleteStatus(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if(viewType == ITEM_BODY_TYPE){
            View view = inflater.inflate(R.layout.dantu_adapter_item, parent, false);
            DanTuHolder danTuHolder = new DanTuHolder(view);
            return danTuHolder;
        }else {
            View foot = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            DanFootHolder footHolder = new DanFootHolder(foot);
            return footHolder;
        }*/
        View view = inflater.inflate(R.layout.dantu_adapter_item, parent, false);
        DanTuHolder danTuHolder = new DanTuHolder(view);
        return danTuHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DanTuHolder dan = (DanTuHolder) holder;

        ViewGroup.LayoutParams params = dan.dan_img.getLayoutParams();
        int spWidth = ((Activity)(dan.dan_img.getContext())).getWindowManager().getDefaultDisplay().getWidth();
        params.width = spWidth / 2;
        float imgWidth = dataList.get(position).getImage_width();
        float imgHeight = dataList.get(position).getImage_height();
        params.height = (int) ((imgHeight / imgWidth) * (spWidth / 2));

        int w = spWidth / 2;
        int h = (int) ((imgHeight / imgWidth) * (spWidth / 2));
        dan.dan_img.setLayoutParams(params);

        Glide
                .with(context)
                .load(dataList.get(position).getCover_url())
                .placeholder(R.drawable.iamge_loading)
                .error(R.drawable.iamge_loading)
                .centerCrop()
                .override(w, h)
                .into(dan.dan_img);

        if(isDeleting){
            dan.dan_fav_img.setVisibility(View.VISIBLE);
            if(dataList.get(position).getSeleteStatus()){
                dan.dan_fav_img.setBackgroundResource(R.drawable.d_selected);
            }else {
                dan.dan_fav_img.setBackgroundResource(R.drawable.d_unselect);
            }
        }else {
            dan.dan_fav_img.setVisibility(View.GONE);
        }

        dan.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dantuItemClickListener.OnDantuItemClickListener(dan.getAdapterPosition(), dataList);
            }
        });
//        if(holder instanceof DanTuHolder){
//
//        }

//        if(holder instanceof DanFootHolder){
//            DanFootHolder foot = (DanFootHolder) holder;
//            // 这里不需要使用页脚
//            if(more){
//                foot.bar.setVisibility(View.GONE);
//                foot.loadText.setVisibility(View.GONE);
//            }else{
//                foot.bar.setVisibility(View.GONE);
//                foot.loadText.setVisibility(View.GONE);
//            }
//        }
    }

    public void loadMoreData(boolean more){
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size() + 1;
    }


    public ArrayList<DantuEntity> getDantuEntityList() {
        if (dataList == null) {
            dataList = new ArrayList<DantuEntity>();
        }
        return dataList;
    }

    class DanTuHolder extends RecyclerView.ViewHolder{
        ImageView dan_img;
        ImageView dan_fav_img;

        public DanTuHolder(View itemView) {
            super(itemView);
            dan_img = (ImageView) itemView.findViewById(R.id.dan_img);
            dan_fav_img = (ImageView) itemView.findViewById(R.id.dan_fav_img);
        }
    }

//    class DanFootHolder extends RecyclerView.ViewHolder{
//        ProgressBar bar;
//        TextView loadText;
//
//        public DanFootHolder(View itemView) {
//            super(itemView);
//            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
//            loadText = (TextView) itemView.findViewById(R.id.newhome_loadmore);
//        }
//    }


    public interface OnDantuItemClickListener{
        void OnDantuItemClickListener(int position, ArrayList<DantuEntity> taotuList);
    }
    public void setDantuItemClickListener(OnDantuItemClickListener dantuItemClickListener) {
        this.dantuItemClickListener = dantuItemClickListener;
    }
    private OnDantuItemClickListener dantuItemClickListener;
}
