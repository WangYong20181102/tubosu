package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/10/14.
 */

public class RecommandFragmentAdapter extends RecyclerView.Adapter<RecommandFragmentAdapter.ActicleHolder> {
    private static final String TAG = RecommandFragmentAdapter.class.getSimpleName();

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private View mHeaderView;
    private View mFooterView;

    private Context mContext;

    private LayoutInflater inflater = null;

    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    public RecommandFragmentAdapter(Context mContext, ArrayList<HashMap<String, String>> data) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.data = data;
    }


    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }



    @Override
    public ActicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new RecommandFragmentAdapter.ActicleHolder(mHeaderView);
        }

        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new RecommandFragmentAdapter.ActicleHolder(mFooterView);
        }

        View view = inflater.inflate(R.layout.item_user_acticle_adapter_layout, null, false);
        RecommandFragmentAdapter.ActicleHolder holder = new RecommandFragmentAdapter.ActicleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActicleHolder holder, int position) {
//        Picasso.with(mContext)
//                .load(data.get(position).get(""))
//                .fit()
//                .placeholder(R.mipmap.occupied1)
//                .into(holder.iv_image);






        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ActicleHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                ((ActicleHolder) holder).tv_time.setText("12");
                ((ActicleHolder) holder).tv_time_text.setText("分钟前");
                ((ActicleHolder) holder).tv_title.setText("标题是很长的哦，你能看到了吗？");
                ((ActicleHolder) holder)._tv_type.setText("卫浴");
                ((ActicleHolder) holder).tv_see_num.setText("53"+"人看过");

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            ((ActicleHolder) holder).textViexxw.setText("乌托邦");
            return;
        }else{

            return;
        }


    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return 7;
        }else if(mHeaderView == null && mFooterView != null){
            return 7 + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return 7 + 1;
        }else {
            return 7 + 2;
        }
    }


    class ActicleHolder extends RecyclerView.ViewHolder {

        TextView tv_time;
        TextView tv_time_text;
        TextView tv_title;
        TextView tv_see_num;
        TextView _tv_type;
        ImageView iv_image;

        TextView textViexxw;

        public ActicleHolder(View itemView) {
            super(itemView);

            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                textViexxw = (TextView) mHeaderView.findViewById(R.id.tv_concern_username);
                return;
            }
            if (itemView == mFooterView){
                return;
            }


            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_time_text = (TextView) itemView.findViewById(R.id.tv_time_text);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_see_num = (TextView) itemView.findViewById(R.id.tv_see_num);
            _tv_type = (TextView)itemView.findViewById(R.id._tv_type);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }



    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


}
