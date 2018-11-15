package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.bean.AnswerListBean;
import com.tbs.tobosutype.bean.AskDetailDataBean;
import com.tbs.tobosutype.bean.AskListBean;
import com.tbs.tobosutype.bean.AskListDataBean;
import com.tbs.tobosutype.bean.AskQuestionBean;
import com.tbs.tobosutype.bean.ExpandableTextView;
import com.tbs.tobosutype.bean.RelationListBean;
import com.tbs.tobosutype.customview.BaseSelectPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DialogUtil;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/6 14:13.
 * 问答详情页适配器
 */
public class AnswerItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExpandableTextView.OnExpandListener, View.OnClickListener {

    private Context context;    //上下文对象
    private AnswerDetailsGridViewAdapter gridViewAdapter;   //问答详情页图片九宫格适配器
    private int currentPosition = 0;    //判断当前值，是第一个就隐藏显示有多少个问答数
    private int etvWidth;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    private Activity activity;

    private ImageView dialogImageClose; //对话框关闭按钮
    private TextView dialogTvPinglunTotal;  //评论总数
    private RecyclerView dialogRecycle;     //回复按钮弹出对话框的recycleView
    private LinearLayout dialogLinear;  //发表父布局
    private RelativeLayout dialogLlBottom;
    private TextView dialogTvSend;  //发表按钮
    private View viewBg;    //半透明背景
    private RelativeLayout rlBottomLayout;
    private EditText editBottom;
    private TextView textBottomSend;
    private TextView tvName;    //被回复人姓名
    private BaseSelectPopupWindow popWiw = null;
    private Animation animation;    //回复输入文本框弹起动画效果
    private HuifuAdapter huifuAdapter;
    private List<AnswerListBean> answerListBeanList;
    private AskQuestionBean askQuestionBean;
    private List<RelationListBean> relationListBeanList;
    private int mPageSize = 10; //一页多少条数据
    private int mPage = 1;  //当前请求页数
    private Gson gson;
    private AskListDataBean askListBeanList;  //回答列表集合
    private ViewPagerBottomAdapter viewPagerAdapter;    //底部广告轮播适配器
    private List<String> strAdList; //底部广告图片集合

    private AskDetailDataBean beanList;
    private boolean isLooper = false;
    private View view1; //回复文本输入框视图
    private View view;  //所有评论对话框视图

    public AnswerItemDetailsAdapter(Context context, AskDetailDataBean beanList) {
        this.context = context;
        this.beanList = beanList;
        askQuestionBean = beanList.getQuestionList();
        answerListBeanList = beanList.getAnswerList();
        relationListBeanList = beanList.getRelationList();
        gson = new Gson();
        activity = (Activity) context;
        initHuifuEditView();

        strAdList = new ArrayList<>();
        strAdList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2799867757,3932089809&fm=26&gp=0.jpg");
        strAdList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3846888849,2108391107&fm=26&gp=0.jpg");
        strAdList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2152174003,412466376&fm=26&gp=0.jpg");
        strAdList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2756413124,951488050&fm=26&gp=0.jpg");
        strAdList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1131960657,1262646568&fm=26&gp=0.jpg");
        strAdList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3264861699,2749249450&fm=26&gp=0.jpg");


    }

    /**
     * 初始化回复文本框view
     */
    private void initHuifuEditView() {
        view1 = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
        rlBottomLayout = view1.findViewById(R.id.rl_bottom_layout);
        editBottom = view1.findViewById(R.id.edit_bottom);
        textBottomSend = view1.findViewById(R.id.text_bottom_send);
        tvName = view1.findViewById(R.id.tv_name);

    }

    //点击事件回调接口
    public static interface OnDianZanAdapterClickLister {
        void onDianZanAdapterClick(View view, int position);
    }

    private OnDianZanAdapterClickLister onDianZanAdapterClickLister = null;

    public void setOnDianZanAdapterClickLister(OnDianZanAdapterClickLister onDianZanAdapterClickLister) {
        this.onDianZanAdapterClickLister = onDianZanAdapterClickLister;
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
            holder2.imageDianzanIcon.setOnClickListener(this);
            holder2.llDianzan.setOnClickListener(this);
            return holder2;
        } else if (viewType == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.anwser_detail3, parent, false);
            AnswerDetailsViewHolder3 holder3 = new AnswerDetailsViewHolder3(view);
            return holder3;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.anwser_detail4, parent, false);
            AnswerDetailsViewHolder4 holder4 = new AnswerDetailsViewHolder4(view);
            return holder4;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        currentPosition = position > answerListBeanList.size() - 1 ? answerListBeanList.size() - 1 : position;
        if (holder instanceof AnswerDetailsViewHolder1) {
            if (!answerListBeanList.isEmpty() && answerListBeanList != null) {
                ((AnswerDetailsViewHolder1) holder).llNoAnswer.setVisibility(View.GONE);
            } else {
                ((AnswerDetailsViewHolder1) holder).llNoAnswer.setVisibility(View.VISIBLE);
            }
            //用户的头像
            GlideUtils.glideLoader(context, askQuestionBean.getIcon(), R.drawable.iamge_loading, R.drawable.iamge_loading, ((AnswerDetailsViewHolder1) holder).imageDetailsIcon, 0);
            ((AnswerDetailsViewHolder1) holder).tvDetailsTittle.setText(askQuestionBean.getTitle());
            ((AnswerDetailsViewHolder1) holder).tvDetailsContext.setText(askQuestionBean.getContent());
            ((AnswerDetailsViewHolder1) holder).tvDetailsDate.setText("提问于" + askQuestionBean.getAdd_time());


            if (!askQuestionBean.getImg_urls()[0].trim().isEmpty() && askQuestionBean.getImg_urls() != null) {
                ((AnswerDetailsViewHolder1) holder).gvDetails.setVisibility(View.VISIBLE);
                gridViewAdapter = new AnswerDetailsGridViewAdapter(context, askQuestionBean.getImg_urls());
                ((AnswerDetailsViewHolder1) holder).gvDetails.setAdapter(gridViewAdapter);
                gridViewAdapter.notifyDataSetChanged();
            } else {
                if (gridViewAdapter != null) {
                    gridViewAdapter = null;
                }
                ((AnswerDetailsViewHolder1) holder).gvDetails.setVisibility(View.GONE);
            }

        } else if (holder instanceof AnswerDetailsViewHolder2) {
            if (answerListBeanList.get(currentPosition - 1) == answerListBeanList.get(0)) {     //判断当前值是不是第一个
                ((AnswerDetailsViewHolder2) holder).rlAnswerNum.setVisibility(View.VISIBLE);
                ((AnswerDetailsViewHolder2) holder).tvAnswerNum.setText(askQuestionBean.getAnswer_count());
            } else {
                ((AnswerDetailsViewHolder2) holder).rlAnswerNum.setVisibility(View.GONE);
            }
            if (etvWidth == 0) {
                ((AnswerDetailsViewHolder2) holder).expandableTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = ((AnswerDetailsViewHolder2) holder).expandableTextView.getWidth();
                    }
                });
            }
            setExpandableTextItem(holder, position - 1);
            GlideUtils.glideLoader(context, answerListBeanList.get(position - 1).getIcon(), R.drawable.iamge_loading, R.drawable.iamge_loading, ((AnswerDetailsViewHolder2) holder).imageAnswerIcon, 0);
            ((AnswerDetailsViewHolder2) holder).tvAnswerName.setText(answerListBeanList.get(position - 1).getAnswer_id());
            //评论时间
            ((AnswerDetailsViewHolder2) holder).tvTime.setText(answerListBeanList.get(position - 1).getAdd_time());
            //评论总数
            ((AnswerDetailsViewHolder2) holder).tvPinglunNum.setText(answerListBeanList.get(position - 1).getComment_count());
            ((AnswerDetailsViewHolder2) holder).imageDianzanIcon.setTag(position - 1);
            ((AnswerDetailsViewHolder2) holder).llDianzan.setTag(position - 1);
            if (answerListBeanList.get(position - 1).getIs_agree() == 1) {   //是否点赞：1、是 2、否
                //已点赞
                ((AnswerDetailsViewHolder2) holder).imageDianzanIcon.setImageResource(R.drawable.like_selected);
                //已点赞字体颜色
                ((AnswerDetailsViewHolder2) holder).tvDianzanNum.setTextColor(Color.parseColor("#ff6b14"));
            } else {
                //未点赞
                ((AnswerDetailsViewHolder2) holder).imageDianzanIcon.setImageResource(R.drawable.like);
                //未点赞字体颜色
                ((AnswerDetailsViewHolder2) holder).tvDianzanNum.setTextColor(Color.parseColor("#6f6f8f"));
            }
            ((AnswerDetailsViewHolder2) holder).tvDianzanNum.setText(answerListBeanList.get(position - 1).getAgree_count());

            if (!answerListBeanList.get(position - 1).getImg_urls()[0].trim().isEmpty() && answerListBeanList.get(position - 1).getImg_urls() != null) {
                ((AnswerDetailsViewHolder2) holder).gvAnswer.setVisibility(View.VISIBLE);
                gridViewAdapter = new AnswerDetailsGridViewAdapter(context, answerListBeanList.get(position - 1).getImg_urls());
                ((AnswerDetailsViewHolder2) holder).gvAnswer.setAdapter(gridViewAdapter);
            } else {
                if (gridViewAdapter != null) {
                    gridViewAdapter = null;
                }
                ((AnswerDetailsViewHolder2) holder).gvAnswer.setVisibility(View.GONE);
            }

            //回复按钮父布局点击跳转
            ((AnswerDetailsViewHolder2) holder).llHuifu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAnswerTotalView(position - 1);
                }
            });


        } else if (holder instanceof AnswerDetailsViewHolder3) {

            viewPagerAdapter = new ViewPagerBottomAdapter(context, strAdList);
            ((AnswerDetailsViewHolder3) holder).viewPagerBottom.setOffscreenPageLimit(3);
            ((AnswerDetailsViewHolder3) holder).viewPagerBottom.setCurrentItem(Integer.MAX_VALUE / 2);  //开始为中间位置，实现左右滑动轮播
            ((AnswerDetailsViewHolder3) holder).viewPagerBottom.setAdapter(viewPagerAdapter);
            startViewPagerThread(holder);

        } else if (holder instanceof AnswerDetailsViewHolder4) {
            ((AnswerDetailsViewHolder4) holder).tvRHZXQuestionItem.setText(relationListBeanList.get(position - (answerListBeanList.size() + 2)).getTitle());
            ((AnswerDetailsViewHolder4) holder).tvRHZXQuestionItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "相关问题" + relationListBeanList.get(position - (answerListBeanList.size() + 2)).getQuestion_id(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    /**
     * 开启线程
     *
     * @param holder
     */
    private void startViewPagerThread(final RecyclerView.ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLooper = true;
                while (isLooper) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((AnswerDetailsViewHolder3) holder).viewPagerBottom.setCurrentItem(((AnswerDetailsViewHolder3) holder).viewPagerBottom.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 跳转到所有回答界面
     *
     * @param position
     */
    private void showAnswerTotalView(int position) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_show_pinglun, null);
        dialogImageClose = view.findViewById(R.id.image_close);
        dialogTvPinglunTotal = view.findViewById(R.id.tv_pinglun_total_num);
        dialogRecycle = view.findViewById(R.id.rv_pinglun);
        dialogLinear = view.findViewById(R.id.ll_bottom);
        dialogLlBottom = view.findViewById(R.id.rl_bottom);
        dialogTvSend = view.findViewById(R.id.tv_send);
        viewBg = view.findViewById(R.id.view_bg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dialogRecycle.setLayoutManager(linearLayoutManager);
        final Dialog dialog = DialogUtil.upSlideDialog(context, view);
        dialogImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (huifuAdapter != null) {
                    huifuAdapter = null;
                }
            }
        });

        dialogLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHuiFuEdit(-1);
            }
        });
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
//        params.put("answer_id", answerListBeanList.get(position).getAnswer_id());
        params.put("answer_id", 677);
        params.put("page", mPage);
        params.put("pagesize", mPageSize);
        OKHttpUtil.post(Constant.ASK_QUESTION_GETLIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        askListBeanList = gson.fromJson(jsonObject.optString("data"), AskListDataBean.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogTvPinglunTotal.setText("全部" + askListBeanList.getCommentCount() + "条评论");
                                huifuAdapter = new HuifuAdapter(context, askListBeanList);
                                huifuAdapter.setOnHuifuClickListener(onHuifuClickListener);
                                dialogRecycle.setAdapter(huifuAdapter);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 评论页点击回复按钮回调事件
     */
    private HuifuAdapter.OnHuifuClickListener onHuifuClickListener = new HuifuAdapter.OnHuifuClickListener() {
        @Override
        public void onHuifuClickListen(int position) {
            showHuiFuEdit(position);
        }
    };

    /**
     * 点击弹出回复文本框
     */
    private void showHuiFuEdit(int position) {
        dialogLlBottom.setVisibility(View.GONE);

        if (position == -1) {
            tvName.setText("外层" + position);
        } else {
            tvName.setText(askListBeanList.getCommentList().get(position).getRecomment_name() + position);
        }
        editBottom.setText("");
        editBottom.setFocusable(true);
        editBottom.setFocusableInTouchMode(true);
        editBottom.requestFocus();
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in1);
        viewBg.startAnimation(animation);
        viewBg.setVisibility(View.VISIBLE);
        if (popWiw == null) {
            popWiw = new BaseSelectPopupWindow(context, view1);
        }
        popWiw.showAtLocation(dialogTvPinglunTotal, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogLlBottom.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out1);
                viewBg.startAnimation(animation);
                viewBg.setVisibility(View.GONE);
                popWiw.backgroundAlpha((AnswerItemDetailsActivity) context, 1f);
                if (animation != null) {    //取消动画
                    animation.cancel();
                }
            }
        });

        System.gc();

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private void setExpandableTextItem(RecyclerView.ViewHolder holder, int position) {
        ((AnswerDetailsViewHolder2) holder).expandableTextView.setTag(position - 1);
        ((AnswerDetailsViewHolder2) holder).expandableTextView.setExpandListener(this);
        Integer state = mPositionsAndStates.get(position - 1);
        ((AnswerDetailsViewHolder2) holder).expandableTextView.updateForRecyclerView(answerListBeanList.get(position).getAnswer_content(),
                etvWidth, state == null ? 0 : state);//第一次getview时肯定为etvWidth为0
    }


    @Override
    public int getItemCount() {
        return !answerListBeanList.isEmpty() ? answerListBeanList.size() + 12 : 12;
    }

    @Override
    public int getItemViewType(int position) {
        if (!answerListBeanList.isEmpty() && answerListBeanList != null) {
            if (position == 0) {
                return 0;
            } else if (position < answerListBeanList.size() + 1) {
                return 1;
            } else if (position == answerListBeanList.size() + 1) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    @Override
    public void onExpand(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    @Override
    public void onClick(View v) {
        if (onDianZanAdapterClickLister != null) {
            onDianZanAdapterClickLister.onDianZanAdapterClick(v, (int) v.getTag());
        }
    }

    private class AnswerDetailsViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvDetailsTittle; //标题
        private TextView tvDetailsContext; //内容
        private TextView tvDetailsName; //姓名
        private TextView tvDetailsDate; //日期
        private ImageView imageDetailsIcon; //头像
        private GridView gvDetails; //九宫格图片
        private LinearLayout llNoAnswer; //暂无回答父布局

        public AnswerDetailsViewHolder1(View itemView) {
            super(itemView);
            tvDetailsTittle = itemView.findViewById(R.id.tv_details_tittlt);
            tvDetailsContext = itemView.findViewById(R.id.tv_details_context);
            tvDetailsName = itemView.findViewById(R.id.tv_details_name);
            tvDetailsDate = itemView.findViewById(R.id.tv_details_date);
            imageDetailsIcon = itemView.findViewById(R.id.image_details_icon);
            gvDetails = itemView.findViewById(R.id.gv_details);
            llNoAnswer = itemView.findViewById(R.id.ll_no_answer);
        }
    }

    private class AnswerDetailsViewHolder2 extends RecyclerView.ViewHolder {
        private RelativeLayout rlAnswerNum;     //回答个数父布局
        private TextView tvAnswerNum;   //回答个数
        private CardView cardView; //头像父布局
        private ImageView imageAnswerIcon;  //头像控件
        private TextView tvAnswerName;  //名字
        private ExpandableTextView expandableTextView; //内容
        private GridView gvAnswer;  //图片
        private TextView tvTime;    //回答时间
        private TextView tvPinglunNum;  //评论个数
        private ImageView imageDianzanIcon; //点赞图标
        private ImageView imageHuifu; //回复图标
        private TextView tvDianzanNum; //点赞个数
        private View viewBottomLine;    //底部下划线
        private LinearLayout llHuifu;    //回复父布局
        private LinearLayout llDianzan;    //点赞父布局

        public AnswerDetailsViewHolder2(View itemView) {
            super(itemView);
            rlAnswerNum = itemView.findViewById(R.id.rl_answer_num);
            tvAnswerNum = itemView.findViewById(R.id.tv_answer_num);
            cardView = itemView.findViewById(R.id.cardview_answer);
            imageAnswerIcon = itemView.findViewById(R.id.image_answer_icon);
            tvAnswerName = itemView.findViewById(R.id.tv_answer_name);
            expandableTextView = itemView.findViewById(R.id.expandable_text);
            gvAnswer = itemView.findViewById(R.id.gv_answer_details);
            tvTime = itemView.findViewById(R.id.tv_answer_time);
            tvPinglunNum = itemView.findViewById(R.id.tv_pinglun_num);
            imageHuifu = itemView.findViewById(R.id.image_huifu);
            imageDianzanIcon = itemView.findViewById(R.id.image_dianzan_icon);
            tvDianzanNum = itemView.findViewById(R.id.tv_dianzan_num);
            viewBottomLine = itemView.findViewById(R.id.view_bottom_line);
            llHuifu = itemView.findViewById(R.id.ll_huifu);
            llDianzan = itemView.findViewById(R.id.ll_dianzan);
        }
    }

    private class AnswerDetailsViewHolder3 extends RecyclerView.ViewHolder {
        private ViewPager viewPagerBottom;

        public AnswerDetailsViewHolder3(View itemView) {
            super(itemView);
            viewPagerBottom = itemView.findViewById(R.id.viewpager_bottom);
        }
    }

    private class AnswerDetailsViewHolder4 extends RecyclerView.ViewHolder {
        private TextView tvRHZXQuestionItem;

        public AnswerDetailsViewHolder4(View itemView) {
            super(itemView);
            tvRHZXQuestionItem = itemView.findViewById(R.id.tv_xiangguan_question_item);
        }
    }

    /**
     * 停止轮播
     */
    public void stopViewPagerThread() {
        isLooper = false;
    }
}
