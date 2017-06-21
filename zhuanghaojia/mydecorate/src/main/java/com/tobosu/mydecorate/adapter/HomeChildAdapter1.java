package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity.HomeFragmentData;

import java.util.ArrayList;


/**
 * Created by dec on 2016/10/19.
 */

public class HomeChildAdapter1 extends RecyclerView.Adapter<HomeChildAdapter1.ChildFragmetHolder> implements View.OnClickListener {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
//    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private Context mContext;

    private LayoutInflater inflater = null;

    private ArrayList<HomeFragmentData> data = new ArrayList<HomeFragmentData>();

    private View mHeaderView;
//    private View mFooterView;
    private boolean isContain;


    private FragmentPagerAdapter adapter;


    public HomeChildAdapter1(Context context, ArrayList<HomeFragmentData> data){
        this.mContext = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView, FragmentPagerAdapter adapter, boolean isContain) {
        mHeaderView = headerView;
        this.adapter = adapter;
        this.isContain = isContain;
        notifyItemInserted(0);
    }



//    public View getFooterView() {
//        return mFooterView;
//    }
//
//    public void setFooterView(View footerView) {
//        mFooterView = footerView;
//        notifyItemInserted(getItemCount()-1);
//    }


    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view  */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null/* && mFooterView == null*/){
            return TYPE_NORMAL;
        }
        if (position == 0 && mHeaderView != null){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
//        if (position == getItemCount()-1){
//            //最后一个,应该加载Footer
//            return TYPE_FOOTER;
//        }
        return TYPE_NORMAL;
    }



    @Override
    public ChildFragmetHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ChildFragmetHolder(mHeaderView);
        }
//        if(mFooterView != null && viewType == TYPE_FOOTER){
//            return new ChildFragmetHolder(mFooterView);
//        }

        View view = inflater.inflate(R.layout.item_homefragment_adapter_layout, null, false);
        HomeChildAdapter1.ChildFragmetHolder holder = new HomeChildAdapter1.ChildFragmetHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的， HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(ChildFragmetHolder holder, int position) {

//        holder.tv_item_time.setText(data.get(position).getTime());
//        holder.tv_item_title_unit.setText(data.get(position).getTime_unit());
//        holder.tv_item_title.setText(data.get(position).getTitle());
//        holder.tv_item_type.setText(data.get(position).getC_title());
//        holder.tv_shop_name.setText(data.get(position).getName());
//        holder.tv_browse_num.setText(data.get(position).getView_count());
//        holder.tv_fav_num.setText(data.get(position).getCollect_count());
//        holder.tv_good_num.setText(data.get(position).getTup_count());
//
//        Picasso.with(mContext)
//                .load(data.get(position).getImage_url())
//                .fit()
//                .placeholder(R.mipmap.occupied1)
////                .error(R.mipmap.icon_head_default)
//                .into(holder.iv_item_image);
//
//        holder.itemView.setTag(data.get(position));

        if(getItemViewType(position) == TYPE_NORMAL){
//            if(holder instanceof ChildFragmetHolder) {
//
//
//            }

            //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
//                ((ChildFragmetHolder) holder).tv.setText(mDatas.get(position-1));
            holder.tv_item_time.setText(data.get(position).getTime()); // 要不要-1？？
            holder.tv_item_title_unit.setText(data.get(position).getTime_unit());
            holder.tv_item_title.setText(data.get(position).getTitle());
            holder.tv_item_type.setText(data.get(position).getC_title());
            holder.tv_shop_name.setText(data.get(position).getName());
            holder.tv_browse_num.setText(data.get(position).getView_count());
            holder.tv_fav_num.setText(data.get(position).getCollect_count());
            holder.tv_good_num.setText(data.get(position).getTup_count());

            Picasso.with(mContext)
                    .load(data.get(position).getImage_url())
                    .fit()
                    .placeholder(R.mipmap.occupied1)
//                .error(R.mipmap.icon_head_default)
                    .into(holder.iv_item_image);

            holder.itemView.setTag(data.get(position));

        }else if(getItemViewType(position) == TYPE_HEADER && isContain){
            holder.home_fragment_viewpager.setAdapter(adapter);

        }/*else if(getItemViewType(position) == TYPE_FOOTER){
            holder.tv_footer.setText("加載中....");
        }*/
        System.out.println("-----position是0吗？ 若是 有pager吗？--->>>"+ position);

    }


    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(data!=null){
            if(mHeaderView == null/* && mFooterView == null*/){
                return data.size();
            }/*else if(mHeaderView == null && mFooterView != null){
                return data.size() + 1;
            }else if (mHeaderView != null && mFooterView == null){
                return data.size() + 1;
            }*/else {
                return data.size() + 1; //原来是 + 2
            }
        }else {
            return 0;
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onRecyclerViewItemClick(view,(HomeFragmentData)view.getTag());
        }
    }


    class ChildFragmetHolder extends RecyclerView.ViewHolder {
        TextView tv_item_time;
        TextView tv_item_title_unit;
        TextView tv_item_title;
        TextView tv_item_type;
        TextView tv_shop_name;
        TextView tv_browse_num;
        TextView tv_fav_num;
        TextView tv_good_num;
        ImageView iv_item_image;

        // header footer
        ViewPager home_fragment_viewpager;
//        TextView tv_footer;


        public ChildFragmetHolder(View itemView) {
            super(itemView);
            tv_item_time = (TextView) itemView.findViewById(R.id.tv_item_time);
            tv_item_title_unit = (TextView) itemView.findViewById(R.id.tv_item_title_unit);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            tv_item_type = (TextView) itemView.findViewById(R.id.tv_item_type);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            tv_browse_num = (TextView) itemView.findViewById(R.id.tv_browse_num);
            tv_fav_num = (TextView) itemView.findViewById(R.id.tv_fav_num);
            tv_good_num = (TextView) itemView.findViewById(R.id.tv_good_num);
            iv_item_image = (ImageView) itemView.findViewById(R.id.iv_item_image);

            if (itemView == mHeaderView){
                home_fragment_viewpager = (ViewPager) itemView.findViewById(R.id.ad_viewpager);
            }
//            if (itemView == mFooterView){
//                tv_footer = (TextView) itemView.findViewById(R.id.tv_footer);
//            }

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, HomeFragmentData data);
    }

}
