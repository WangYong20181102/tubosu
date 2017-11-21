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
import java.util.ArrayList;
import com.tbs.tobosutype.bean._ImageS;
/**
 * Created by Lie on 2017/11/13.
 */

public class DanTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = DanTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<_ImageS> dataList;
    private boolean more = false;
    private boolean isDeleting = false;

    public DanTuAdapter(Context context, ArrayList<_ImageS> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    public void setDeletingStutas(boolean delete){
        isDeleting = delete;
        for(int i=0;i<dataList.size();i++){
            dataList.get(i).setSeleteStatus(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    }

    public void loadMoreData(boolean more){
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size();
    }


    public ArrayList<_ImageS> getDantuEntityList() {
        if (dataList == null) {
            dataList = new ArrayList<_ImageS>();
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

    public interface OnDantuItemClickListener{
        void OnDantuItemClickListener(int position, ArrayList<_ImageS> taotuList);
    }
    public void setDantuItemClickListener(OnDantuItemClickListener dantuItemClickListener) {
        this.dantuItemClickListener = dantuItemClickListener;
    }
    private OnDantuItemClickListener dantuItemClickListener;
}
