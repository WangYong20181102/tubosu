package com.tbs.tobosutype.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.AnswerItemDetailsActivity;
import com.tbs.tobosutype.bean.ExpandableTextView;
import com.tbs.tobosutype.customview.BaseSelectPopupWindow;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DialogUtil;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Wang on 2018/11/6 14:13.
 */
public class AnswerItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExpandableTextView.OnExpandListener {

    private Context context;
    private List<String> stringList;
    private AnswerDetailsGridViewAdapter gridViewAdapter;
    private List<String> integerList;
    private int currentPosition = 0;
    private int etvWidth;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    private List<String> xiagnguanQuestionList;
    private Activity activity;

    private ImageView dialogImageClose; //对话框关闭按钮
    private TextView dialogTvPinglunTotal;  //评论总数
    private RecyclerView dialogRecycle;
    private LinearLayout dialogLinear;  //发表父布局
    private RelativeLayout dialogLlBottom;
    private TextView dialogTvSend;  //发标按钮
    private View viewBg;    //半透明背景
    private RelativeLayout rlBottomLayout;
    private EditText editBottom;
    private TextView textBottomSend;
    private BaseSelectPopupWindow popWiw = null;
    private Animation animation;
    private HuifuAdapter huifuAdapter;

    public AnswerItemDetailsAdapter(Context context, List<String> list) {
        this.context = context;
        this.stringList = list;
        activity = (Activity) context;
        integerList = new ArrayList<>();
        integerList.add("http://b.hiphotos.baidu.com/image/h%3D300/sign=93c9d8d8e450352aae6123086342fb1a/f11f3a292df5e0fe301f5e6e516034a85edf725e.jpg");
        integerList.add("http://h.hiphotos.baidu.com/image/h%3D300/sign=0bb2d44937292df588c3aa158c305ce2/9345d688d43f87943b9e5948df1b0ef41bd53a76.jpg");
        integerList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2761076051,940312338&fm=26&gp=0.jpg");
        integerList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1128360276,1152697715&fm=26&gp=0.jpg");
        integerList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2471469961,958377768&fm=26&gp=0.jpg");
        xiagnguanQuestionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            xiagnguanQuestionList.add("家里卧室如何装修才会更好" + i);
        }
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
        currentPosition = position > stringList.size() - 1 ? stringList.size() - 1 : position;
        if (holder instanceof AnswerDetailsViewHolder1) {
            if (!stringList.isEmpty() && stringList != null) {
                ((AnswerDetailsViewHolder1) holder).llNoAnswer.setVisibility(View.GONE);
            } else {
                ((AnswerDetailsViewHolder1) holder).llNoAnswer.setVisibility(View.VISIBLE);
            }
            //用户的头像
            GlideUtils.glideLoader(context, AppInfoUtil.getUserIcon(context), R.drawable.iamge_loading, R.drawable.iamge_loading, ((AnswerDetailsViewHolder1) holder).imageDetailsIcon, 0);
            gridViewAdapter = new AnswerDetailsGridViewAdapter(context, integerList);
            ((AnswerDetailsViewHolder1) holder).gvDetails.setAdapter(gridViewAdapter);
            gridViewAdapter.notifyDataSetChanged();
        } else if (holder instanceof AnswerDetailsViewHolder2) {
            if (stringList.get(currentPosition - 1) == stringList.get(0)) {     //判断当前值是不是第一个
                ((AnswerDetailsViewHolder2) holder).rlAnswerNum.setVisibility(View.VISIBLE);
            }
            if (etvWidth == 0) {
                ((AnswerDetailsViewHolder2) holder).expandableTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = ((AnswerDetailsViewHolder2) holder).expandableTextView.getWidth();
                    }
                });
            }
            setExpandableTextItem(holder, position);
            GlideUtils.glideLoader(context, AppInfoUtil.getUserIcon(context), R.drawable.iamge_loading, R.drawable.iamge_loading, ((AnswerDetailsViewHolder2) holder).imageAnswerIcon, 0);


            gridViewAdapter = new AnswerDetailsGridViewAdapter(context, integerList);
            ((AnswerDetailsViewHolder2) holder).gvAnswer.setAdapter(gridViewAdapter);
            gridViewAdapter.notifyDataSetChanged();
//            ((AnswerDetailsViewHolder2) holder).gvAnswer.setVisibility(View.GONE);

            //点击回复按钮
            ((AnswerDetailsViewHolder2) holder).imageHuifu.setOnClickListener(onClickListener);


        } else if (holder instanceof AnswerDetailsViewHolder3) {

        } else if (holder instanceof AnswerDetailsViewHolder4) {
            ((AnswerDetailsViewHolder4) holder).tvRHZXQuestionItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "相关问题" + (position - (stringList.size() + 2)), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    /**
     * 点击回复按钮回调事件
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_pinglun, null);
            dialogImageClose = view.findViewById(R.id.image_close);
            dialogTvPinglunTotal = view.findViewById(R.id.tv_pinglun_total_num);
            dialogRecycle = view.findViewById(R.id.rv_pinglun);
            dialogLinear = view.findViewById(R.id.ll_bottom);
            dialogLlBottom = view.findViewById(R.id.rl_bottom);
            dialogTvSend = view.findViewById(R.id.tv_send);
            viewBg = view.findViewById(R.id.view_bg);
            huifuAdapter = new HuifuAdapter(context, stringList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dialogRecycle.setLayoutManager(linearLayoutManager);
            dialogRecycle.setAdapter(huifuAdapter);
            huifuAdapter.notifyDataSetChanged();

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
                    // 点击 跳转去搜索页面
                    dialogLlBottom.setVisibility(View.GONE);
                    View view1 = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
                    rlBottomLayout = view1.findViewById(R.id.rl_bottom_layout);
                    editBottom = view1.findViewById(R.id.edit_bottom);
                    textBottomSend = view1.findViewById(R.id.text_bottom_send);

                    editBottom.setText("");
                    editBottom.setFocusable(true);
                    editBottom.setFocusableInTouchMode(true);
                    editBottom.requestFocus();
                    if (popWiw == null) {
                        popWiw = new BaseSelectPopupWindow(context, view1);
                    }
                    InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    popWiw.showAtLocation(dialogTvPinglunTotal, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in1);
                    viewBg.startAnimation(animation);
                    viewBg.setVisibility(View.VISIBLE);
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
            });


        }
    };

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
        ((AnswerDetailsViewHolder2) holder).expandableTextView.updateForRecyclerView("java中如何判断一个list中的元素是否全都为某个值,java中如何判断一个list" +
                        "中的元素是否全都为某个值,java中如何判断一个list中的元素是否全都为某个值,java中如何判断一个list中的元素是否全都为某个值,java中如何" +
                        "判断一个list中的元素是否全都为某个值,java中如何判断一个list中的元素是否全都为某个值,java中如何判断一个list中的元素是否全都为某个值," +
                        "java中如何判断一个list中的元素是否全都为某个值,java中如何判断一个list中的元素是否全都为某个值,",
                etvWidth, state == null ? 0 : state);//第一次getview时肯定为etvWidth为0
    }


    @Override
    public int getItemCount() {
        return !stringList.isEmpty() ? stringList.size() + 12 : 12;
    }

    @Override
    public int getItemViewType(int position) {
        if (!stringList.isEmpty() && stringList != null) {
            if (position == 0) {
                return 0;
            } else if (position < stringList.size() + 1) {
                return 1;
            } else if (position == stringList.size() + 1) {
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
        }
    }

    private class AnswerDetailsViewHolder3 extends RecyclerView.ViewHolder {
        private CardView cardViewBottom;//  底部图片
        private ImageView imageBottom;  //底部图片

        public AnswerDetailsViewHolder3(View itemView) {
            super(itemView);
            cardViewBottom = itemView.findViewById(R.id.cardview_bottom);
            imageBottom = itemView.findViewById(R.id.image_bottom);
        }
    }

    private class AnswerDetailsViewHolder4 extends RecyclerView.ViewHolder {
        private TextView tvRHZXQuestionItem;

        public AnswerDetailsViewHolder4(View itemView) {
            super(itemView);
            tvRHZXQuestionItem = itemView.findViewById(R.id.tv_xiangguan_question_item);
        }
    }

}
