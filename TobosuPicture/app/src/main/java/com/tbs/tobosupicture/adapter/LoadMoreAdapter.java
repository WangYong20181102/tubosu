package com.tbs.tobosupicture.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.ConditionActivity;
import com.tbs.tobosupicture.bean.DistrictEntity;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.view.GridView2;
import java.util.ArrayList;

/**
 * Created by Lie on 2017/8/14.
 */

public class LoadMoreAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    GridView2 gView;
    ArrayList<DistrictEntity> dataList;
    Context context;
    public View footerView;
    public boolean isLoadding = false;
    public LoadMoreAdapter(GridView2 gView, ArrayList<DistrictEntity> dataList, Context context) {
        this.gView = gView;
        this.dataList = dataList;
        gView.setOnScrollListener(this);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == getCount() - 1) {
            if (footerView == null) {
                footerView = LayoutInflater.from(context).inflate(R.layout.footer_layout, parent, false);
                GridView.LayoutParams pl = new GridView.LayoutParams(
                        getDisplayWidth((Activity) context),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                footerView.setLayoutParams(pl);
                footerView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (gView.isPullRefreshCompleted() && gView.isLoadingMoreCompleted()) {
                            gView.setPullRefreshCompleted(false);
                            gView.setLoadingMoreCompleted(false);
                            setLoadding();
                            ((ConditionActivity) context).loadMore(1);
                        }

                    }
                });
            }
            return footerView;
        }

        ViewHolder holder;
        if (convertView == null || (convertView != null && convertView == footerView)) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_gv_district, parent, false);
            holder.textview = (TextView) convertView .findViewById(R.id.tvDistrictName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivDistrictIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtils.glideLoader(context, dataList.get(position).getImage_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.imageView, 1);
        holder.textview.setText(dataList.get(position).getName());

        return convertView;
    }

    private class ViewHolder {
        TextView textview;
        ImageView imageView;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        ((GridView2) gView).setGridViewScrollY(getGriViewScrollY());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        if (scrollState == SCROLL_STATE_IDLE && gView.isPullRefreshCompleted()
                && gView.isLoadingMoreCompleted()) {
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                gView.setPullRefreshCompleted(false);
                gView.setLoadingMoreCompleted(false);
                setLoadding();
                ((ConditionActivity) context).loadMore(1);
            }
        }
    }

    /**
     * 正在加载
     */
    public void setLoadding() {
        if (footerView != null && !isLoadding) {
            footerView.findViewById(R.id.footer_loading).setVisibility( View.VISIBLE);
            ((TextView) footerView.findViewById(R.id.footer_text)).setText("正在加载...");
            isLoadding = true;
            footerView.setEnabled(false);
        }
    }

    /**
     * 单击加载更多
     */
    public void setStopLoading() {
        if (footerView != null && isLoadding) {
            footerView.findViewById(R.id.footer_loading).setVisibility(
                    View.GONE);
            ((TextView) footerView.findViewById(R.id.footer_text))
                    .setText("点击加载更多");
            isLoadding = false;
            footerView.setEnabled(true);
        }
    }

    /**
     * 隐藏加载更多
     */
    public void setLoadingMoreHide() {
        footerView.setVisibility(View.GONE);
    }

    /**
     * 显示加载更多
     */
    public void setLoadingMoreShow() {
        footerView.setVisibility(View.VISIBLE);
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    private int getDisplayWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        return width;
    }

    /**
     * 获取GirdView滑动距离
     *
     * @return
     */
    @SuppressLint("NewApi")
    public int getGriViewScrollY() {
        View child = gView.getChildAt(0);
        if (child == null) {
            return 0;
        }
        int firstVisiblePosition = gView.getFirstVisiblePosition(); // 界面中当前显示的第一个孩子的位置
        int hideTopHeight = child.getTop(); // 第一个孩子被隐藏的高度
        return -hideTopHeight + (firstVisiblePosition * child.getHeight())
                + gView.getVerticalSpacing();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
