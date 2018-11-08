package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/6 14:13.
 */
public class AnswerItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> stringList;
    private AnswerDetailsGridViewAdapter gridViewAdapter;
    private List<String> integerList;


    public AnswerItemDetailsAdapter(Context context, List<String> list) {
        this.context = context;
        this.stringList = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.anwser_detail1, parent, false);
            AnswerDetailsViewHolder1 holder1 = new AnswerDetailsViewHolder1(view);
            return holder1;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.anwser_detail2, parent, false);
            AnswerDetailsViewHolder2 holder2 = new AnswerDetailsViewHolder2(view);
            return holder2;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.anwser_detail3, parent, false);
            AnswerDetailsViewHolder3 holder3 = new AnswerDetailsViewHolder3(view);
            return holder3;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AnswerDetailsViewHolder1) {
            integerList = new ArrayList<>();
            integerList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=93c9d8d8e450352aae6123086342fb1a/f11f3a292df5e0fe301f5e6e516034a85edf725e.jpg");
            integerList.add("http://h.hiphotos.baidu.com/image/h%3D300/sign=0bb2d44937292df588c3aa158c305ce2/9345d688d43f87943b9e5948df1b0ef41bd53a76.jpg");
            integerList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2761076051,940312338&fm=26&gp=0.jpg");
            integerList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1128360276,1152697715&fm=26&gp=0.jpg");
            integerList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2471469961,958377768&fm=26&gp=0.jpg");
            //用户的头像
            GlideUtils.glideLoader(context, AppInfoUtil.getUserIcon(context), R.drawable.iamge_loading, R.drawable.iamge_loading, ((AnswerDetailsViewHolder1) holder).imageDetailsIcon, 0);
            gridViewAdapter = new AnswerDetailsGridViewAdapter(context, integerList);
            ((AnswerDetailsViewHolder1) holder).gvDetails.setAdapter(gridViewAdapter);
            gridViewAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class AnswerDetailsViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvDetailsTittle; //标题
        private TextView tvDetailsContext; //内容
        private TextView tvDetailsName; //姓名
        private TextView tvDetailsDate; //日期
        private ImageView imageDetailsIcon; //头像
        private GridView gvDetails; //九宫格图片

        public AnswerDetailsViewHolder1(View itemView) {
            super(itemView);
            tvDetailsTittle = itemView.findViewById(R.id.tv_details_tittlt);
            tvDetailsContext = itemView.findViewById(R.id.tv_details_context);
            tvDetailsName = itemView.findViewById(R.id.tv_details_name);
            tvDetailsDate = itemView.findViewById(R.id.tv_details_date);
            imageDetailsIcon = itemView.findViewById(R.id.image_details_icon);
            gvDetails = itemView.findViewById(R.id.gv_details);
        }
    }

    private class AnswerDetailsViewHolder2 extends RecyclerView.ViewHolder {
        public AnswerDetailsViewHolder2(View itemView) {
            super(itemView);
        }
    }

    private class AnswerDetailsViewHolder3 extends RecyclerView.ViewHolder {
        public AnswerDetailsViewHolder3(View itemView) {
            super(itemView);
        }
    }
}
