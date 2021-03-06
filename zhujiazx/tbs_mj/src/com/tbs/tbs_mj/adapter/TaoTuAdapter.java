package com.tbs.tbs_mj.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._ImageD;
import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/13.
 */

public class TaoTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = TaoTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<_ImageD> dataList;
    private boolean more = false;
    private boolean isDeleting = false;

    public TaoTuAdapter(Context context, ArrayList<_ImageD> dataList){
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
        View view = inflater.inflate(R.layout.taotu_adapter_item, parent, false);
        TaoTuHolder danTuHolder = new TaoTuHolder(view);
        return danTuHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
            .error(R.drawable.iamge_loading)
            .centerCrop()
            .override(w, h)
            .into(tao.tao_img);

        tao.tvTaoTextTitle.setText(dataList.get(position).getTitle());

        if(isDeleting){

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

    public void loadMoreData(boolean more){
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size();
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


    public interface OnTaotuItemClickListener{
        void OnTaotuItemClickListener(int position, ArrayList<_ImageD> taotuList);
    }
    public void setTaotuItemClickListener(OnTaotuItemClickListener taotuItemClickListener) {
        this.taotuItemClickListener = taotuItemClickListener;
    }

    private OnTaotuItemClickListener taotuItemClickListener;

    public ArrayList<_ImageD> getTaotuEntityList() {
        if (dataList == null) {
            dataList = new ArrayList<_ImageD>();
        }
        return dataList;
    }
}
