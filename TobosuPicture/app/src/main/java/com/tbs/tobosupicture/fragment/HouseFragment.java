package com.tbs.tobosupicture.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.adapter.SamplePictureAdapter;
import com.tbs.tobosupicture.base.BaseFragment;
import com.tbs.tobosupicture.bean.DecorateImgStyle;
import com.tbs.tobosupicture.bean.EC;
import com.tbs.tobosupicture.bean.Event;
import com.tbs.tobosupicture.bean.SamplePicBeanEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 家装
 * Created by Lie on 2017/7/14.
 */

public class HouseFragment extends BaseFragment {
    private static final String TAG = "HouseFragment";
    private static Context context;

    @BindView(R.id.tvSpace)
    TextView tvSpace;
    @BindView(R.id.ivSpaceIcon)
    ImageView ivSpaceIcon;
    @BindView(R.id.layoutSpace)
    LinearLayout layoutSpace;
    @BindView(R.id.tvStyle)
    TextView tvStyle;
    @BindView(R.id.ivStyleIcon)
    ImageView ivStyleIcon;
    @BindView(R.id.layoutStyle)
    LinearLayout layoutStyle;
    @BindView(R.id.tvPart)
    TextView tvPart;
    @BindView(R.id.ivPartIcon)
    ImageView ivPartIcon;
    @BindView(R.id.layoutPart)
    LinearLayout layoutPart;
    @BindView(R.id.tvHouseStyle)
    TextView tvHouseStyle;
    @BindView(R.id.ivHouseStyleIcon)
    ImageView ivHouseStyleIcon;
    @BindView(R.id.layoutHouseStyle)
    LinearLayout layoutHouseStyle;
    @BindView(R.id.tvHouseColor)
    TextView tvHouseColor;
    @BindView(R.id.ivColorIcon)
    ImageView ivColorIcon;
    @BindView(R.id.layoutColor)
    LinearLayout layoutColor;
    Unbinder unbinder;
    @BindView(R.id.v_anchor)
    LinearLayout vAnchor;
    @BindView(R.id.houseRecyclerview)
    RecyclerView houseRecyclerview;
    @BindView(R.id.houseSwipRefreshLayout)
    SwipeRefreshLayout houseSwipRefreshLayout;

    @BindView(R.id.iv_template_no_data)
    ImageView iv_template_no_data;

    private String city;


    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SamplePicBeanEntity> samplePicList = new ArrayList<SamplePicBeanEntity>();

    private SamplePictureAdapter samplePicAdapter;

    private ArrayList<DecorateImgStyle.DecorateStyleBeanArray.SpaceBean> space;
    private ArrayList<DecorateImgStyle.DecorateStyleBeanArray.StyleBean> style;
    private ArrayList<DecorateImgStyle.DecorateStyleBeanArray.PartialBean> partial;
    private ArrayList<DecorateImgStyle.DecorateStyleBeanArray.LayoutBean> layout;
    private ArrayList<DecorateImgStyle.DecorateStyleBeanArray.ColorBean> color;

    private PopupWindow popupWindow;
//    private ArrayList<String> paramNameList = new ArrayList<String>;
//    private ArrayList<String> selectTypeIdList = new ArrayList<String>;

    private HashMap<String, Object> parameter = new HashMap<String, Object>();

    private DecorateImgStyle.DecorateStyleBeanArray dataBeanArray;
    private ArrayList<String> textDataList = new ArrayList<String>();
    private ArrayList<String> idDataList = new ArrayList<String>();
    private ArrayList<Integer> iconDataList = new ArrayList<Integer>();

    private int page = 1;
    private int pageSize = 3;

    private int[] iconPic = new int[]{R.mipmap.fenlei01,R.mipmap.fenlei02,R.mipmap.fenlei03,R.mipmap.fenlei04,R.mipmap.fenlei05,
            R.mipmap.fenlei06,R.mipmap.fenlei07,R.mipmap.fenlei08,R.mipmap.fenlei09,R.mipmap.fenlei010,R.mipmap.fenlei011,R.mipmap.fenlei012,
            R.mipmap.fenlei01,R.mipmap.fenlei02,R.mipmap.fenlei03,R.mipmap.fenlei04,R.mipmap.fenlei05,
            R.mipmap.fenlei06,R.mipmap.fenlei07,R.mipmap.fenlei08,R.mipmap.fenlei09,R.mipmap.fenlei010,R.mipmap.fenlei011,R.mipmap.fenlei012,
            R.mipmap.fenlei01,R.mipmap.fenlei02,R.mipmap.fenlei03,R.mipmap.fenlei04,R.mipmap.fenlei05,
            R.mipmap.fenlei06,R.mipmap.fenlei07,R.mipmap.fenlei08,R.mipmap.fenlei09,R.mipmap.fenlei010,R.mipmap.fenlei011,R.mipmap.fenlei012};

    private int[] iconColor = new int[]{R.mipmap.color_all,R.mipmap.color_white,R.mipmap.color_mi,R.mipmap.color_yellow,R.mipmap.color_orange,
            R.mipmap.color_red,R.mipmap.color_pink,R.mipmap.color_green,R.mipmap.color_blue,R.mipmap.color_puer,R.mipmap.color_black,
            R.mipmap.color_coffe,R.mipmap.color_gray,R.mipmap.color_colorful};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house, null, false);
        unbinder = ButterKnife.bind(this, view);

        context = getActivity();
        initListViewSetting();
        initStyleData();
        return view;
    }


    /***
     * 初始化标题上的类型的数据
     */
    private void initStyleData() {
        // 未选择类型时， 未选择城市时  显示数据
        city = SpUtils.getTemplateFragmentCity(getActivity());
        getDataFromNet(null, page, pageSize, city, false);

        String json = SpUtils.getHouseStyleJson(context);
        Utils.setErrorLog(TAG, json);
        if (!"".equals(json)) {
            DecorateImgStyle decorateStyle = new DecorateImgStyle(json);
            if (decorateStyle.getStatus() == 200) {
                dataBeanArray = new DecorateImgStyle.DecorateStyleBeanArray(decorateStyle.getData());
            } else {
                Utils.setErrorLog(TAG, "error_code!=200");
            }
        } else {
            // 没数据填充其他图， 或者继续请求数据
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        ButterKnife.bind(getActivity()).unbind();
    }

    @OnClick({R.id.layoutSpace, R.id.layoutStyle, R.id.layoutPart, R.id.layoutHouseStyle, R.id.layoutColor})
    public void onViewClickedHouseFragment(View view) {
        if(popupWindow!=null && popupWindow.isShowing()){
            return;
        }
        boolean flag = false;
        textDataList.clear();
        iconDataList.clear();
        idDataList.clear();
        int type = -1;
        switch (view.getId()) {
            case R.id.layoutSpace:
                setTextStyle(0);
                space = dataBeanArray.getSpaceList();
                for (int i = 0, size = space.size(); i < size; i++) {
                    idDataList.add(space.get(i).getId());
                    textDataList.add(space.get(i).getClass_name());
                    iconDataList.add(iconPic[i]);
                }
                type = 0;
                break;
            case R.id.layoutStyle:
                setTextStyle(1);
                style = dataBeanArray.getStyleList();
                for (int i = 0, size = style.size(); i < size; i++) {
                    idDataList.add(style.get(i).getId());
                    textDataList.add(style.get(i).getClass_name());
                    iconDataList.add(iconPic[i]);
                }
                type = 1;
                break;
            case R.id.layoutPart:
                setTextStyle(2);
                partial = dataBeanArray.getPartialList();
                for (int i = 0, size = partial.size(); i < size; i++) {
                    idDataList.add(partial.get(i).getId());
                    textDataList.add(partial.get(i).getClass_name());
                    iconDataList.add(iconPic[i]);
                }
                type = 2;
                break;
            case R.id.layoutHouseStyle:
                setTextStyle(3);
                layout = dataBeanArray.getLayoutList();
                for (int i = 0, size = layout.size(); i < size; i++) {
                    idDataList.add(layout.get(i).getId());
                    textDataList.add(layout.get(i).getClass_name());
                    iconDataList.add(R.mipmap.circle_normal);
                    flag = true;
                }
                type = 3;
                break;
            case R.id.layoutColor:
                setTextStyle(4);
                color = dataBeanArray.getColorList();
                for (int i = 0, size = color.size(); i < size; i++) {
                    idDataList.add(color.get(i).getId());
                    textDataList.add(color.get(i).getClass_name());
                    iconDataList.add(iconColor[i]);
                }
                type = 4;
                break;
        }
        initPopupWindow(type, textDataList, idDataList, iconDataList, flag);
    }


    /**
     *
     * @param type 大类
     * @param textList 选中的文字
     * @param idList 选中的文字的id
     * @param iconList 选中的文字的图片
     * @param flag
     */
    private void initPopupWindow(final int type, final ArrayList<String> textList, final ArrayList<String> idList, ArrayList<Integer> iconList,boolean flag) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popuplayout, null);
        GridView gv = (GridView) contentView.findViewById(R.id.gv_converview);
        popupWindow
                = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
//        ColorDrawable cd = new ColorDrawable();
//        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        GvAdapter gvAdapter = new GvAdapter(getActivity(), iconList, textList, flag);
        gv.setAdapter(gvAdapter);
        switch (type){
            case 0:
                gvAdapter.setSelectedPosition(SpUtils.getHouseSpaceNum(getActivity()));
                break;
            case 1:
                gvAdapter.setSelectedPosition(SpUtils.getHouseStyleNum(getActivity()));
                break;
            case 2:
                gvAdapter.setSelectedPosition(SpUtils.getHousePartNum(getActivity()));
                break;
            case 3:
                gvAdapter.setSelectedPosition(SpUtils.getHouseHuxingNum(getActivity()));
                break;
            case 4:
                gvAdapter.setSelectedPosition(SpUtils.getHouseColorNum(getActivity()));
                break;
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backPlace();
                popupWindow.dismiss();
                switch (type) {
                    case 0:
                        SpUtils.setHouseSpaceNum(getActivity(), position);
                        if(position>0){
                            tvSpace.setText(textList.get(position));
                            parameter.put("space_id", idList.get(position));
                        }else{
                            tvSpace.setText("空间");
                            parameter.put("space_id", "0");
                        }
                        break;
                    case 1:
                        SpUtils.setHouseStyleNum(getActivity(), position);
                        if(position>0){
                            tvStyle.setText(textList.get(position));
                            parameter.put("style_id", idList.get(position));
                        }else{
                            tvStyle.setText("风格");
                            parameter.put("style_id", "0");
                        }
                        break;
                    case 2:
                        SpUtils.setHousePartNum(getActivity(), position);
                        if(position>0){
                            tvPart.setText(textList.get(position));
                            parameter.put("part_id", idList.get(position));
                        }else{
                            tvPart.setText("局部");
                            parameter.put("part_id", "0");
                        }
                        break;
                    case 3:
                        SpUtils.setHouseHuxingNum(getActivity(), position);
                        if(position>0){
                            tvHouseStyle.setText(textList.get(position));
                            parameter.put("layout_id", idList.get(position));
                        }else{
                            tvHouseStyle.setText("户型");
                            parameter.put("layout_id", "0");
                        }
                        break;
                    case 4:
                        SpUtils.setHouseColorNum(getActivity(), position);
                        if(position>0){
                            tvHouseColor.setText(textList.get(position));
                            parameter.put("color_id", idList.get(position));
                        }else{
                            tvHouseColor.setText("颜色");
                            parameter.put("color_id", "0");
                        }
                        break;

                    default:
                        tvSpace.setText("空间");
                        tvPart.setText("局部");
                        tvStyle.setText("风格");
                        tvHouseStyle.setText("户型");
                        tvHouseColor.setText("颜色");
                        break;
                }
                page = 1;
                pageSize = 10;
                getDataFromNet(parameter, page, pageSize, city, false);
            }
        });
        popupWindow.showAsDropDown(vAnchor);
//        popupWindow.showAtLocation(vAnchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /***
     * 初始化页面列表的数据
     * @param param
     * @param _page
     * @param _pageSize
     */
    private void getDataFromNet(HashMap<String, Object> param, int _page, int _pageSize, String city,  final boolean addMore) {
        if(!addMore){
            samplePicList.clear();
        }

        if (param==null) {
            param = new HashMap<String, Object>();
        }

        param.put("token", Utils.getDateToken());
        param.put("page", _page);
        param.put("city_name", city);
        param.put("page_size", _pageSize);
        HttpUtils.doPost(UrlConstans.GET_LIST, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.setToast(getActivity(), "系统繁忙，稍后再试!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    if(object.getInt("status")==200){
                        JSONArray arr = object.getJSONArray("data");
                        SamplePicBeanEntity entity = null;
                        for(int i=0, len=arr.length();i<len;i++){
                            entity = new SamplePicBeanEntity(arr.getJSONObject(i));
                            samplePicList.add(entity);
                        }
                    }else if(object.getInt("status")==201){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(samplePicAdapter!=null){
                                    samplePicAdapter.noMoreData();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.setErrorLog(TAG, json);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initListViewAdapter();
                    }
                });

            }
        });
    }

    private void initListViewAdapter(){

        houseSwipRefreshLayout.setRefreshing(false);
        if(samplePicAdapter==null){
            samplePicAdapter = new SamplePictureAdapter(context, samplePicList);
            houseRecyclerview.setAdapter(samplePicAdapter);
        }else{
            samplePicAdapter.notifyDataSetChanged();
        }

        if(samplePicList.size()==0){
            iv_template_no_data.setVisibility(View.VISIBLE);
        }else {
            iv_template_no_data.setVisibility(View.GONE);
        }

        if(samplePicAdapter!=null){
            samplePicAdapter.hideLoadMoreMessage();
        }

    }

    private void initListViewSetting(){
        // 设置下拉进度的背景颜色，默认就是白色的
        houseSwipRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        houseSwipRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        houseSwipRefreshLayout.setOnRefreshListener(swipeLister);
        
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        houseRecyclerview.setLayoutManager(linearLayoutManager);

        houseRecyclerview.setOnScrollListener(onScrollListener);
        houseRecyclerview.setOnTouchListener(onTouchListener);
    }


    //显示列表的滑动监听事件 上拉加载更多
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastVisiableItem = linearLayoutManager.findLastVisibleItemPosition();
//            Log.e(TAG, "最后可见目标===" + lastVisiableItem + "集合总数===" + mLinearLayoutManager.getItemCount() + "==newState==" + newState + "==刷新状态==" + swipeRefreshLayout.isRefreshing());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisiableItem + 2 >= linearLayoutManager.getItemCount()
                    && !houseSwipRefreshLayout.isRefreshing()) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //处于下拉刷新时列表不允许点击  死锁问题
            if (houseSwipRefreshLayout.isRefreshing()) {
                return true;
            } else {
                return false;
            }
        }
    };

    //下拉刷新监听事件
    private SwipeRefreshLayout.OnRefreshListener swipeLister = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新数据 重新初始化各种数据
            samplePicList.clear();
            page = 1;
            if(samplePicAdapter!=null){
                samplePicAdapter.hideLoadMoreMessage();
            }
            getDataFromNet(parameter,page,10, city, false);

        }
    };

    private void loadMore(){
        page++;
        if(samplePicAdapter!=null){
            samplePicAdapter.showLoadMoreMessage();
        }

        houseSwipRefreshLayout.setRefreshing(false);
        getDataFromNet(parameter,page,10, city, true);
        System.out.println("-----**-onScrolled load more completed------");
    }


    private class GvAdapter extends BaseAdapter {
        private ArrayList<Integer> iconList;
        private ArrayList<String> textList;
        private Context context;
        private ViewHolder holder;
        private LayoutInflater inflater;
        private boolean flag;
        private int selectedPosition = 0;


        public GvAdapter(Context context, ArrayList<Integer> iconList, ArrayList<String> textList, boolean flag) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.iconList = iconList;
            this.textList = textList;
            this.flag = flag;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public void clearSelection(int position) {
            selectedPosition = position;
        }
        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                if(flag){
                    convertView = inflater.inflate(R.layout.adapter_item_gv_styl1, null);
                    holder.iv = (ImageView) convertView.findViewById(R.id.ivSmallStyleIco1);
                    holder.tv = (TextView) convertView.findViewById(R.id.tvItemStyle1);
                }else{
                    convertView = inflater.inflate(R.layout.adapter_item_gv_styl, null);
                    holder.iv = (ImageView) convertView.findViewById(R.id.ivSmallStyleIcon);
                    holder.tv = (TextView) convertView.findViewById(R.id.tvItemStyle);
                }


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(textList.get(position));

            if(flag){
                if(selectedPosition == position){
                    holder.iv.setBackgroundResource(R.mipmap.circle_select);
                    holder.tv.setTextColor(Color.parseColor("#F97B0C"));
                }else{
                    holder.iv.setBackgroundResource(R.mipmap.circle_normal);
                    holder.tv.setTextColor(Color.parseColor("#2B2F3A"));
                }
            }else{
                holder.iv.setBackgroundResource(iconList.get(position));
                if(selectedPosition == position){
                    holder.tv.setTextColor(Color.parseColor("#F97B0C"));
                }else{
                    holder.tv.setTextColor(Color.parseColor("#2B2F3A"));
                }
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }


    private void backPlace() {
        tvSpace.setTextColor(Color.parseColor("#202124"));
        ivSpaceIcon.setBackgroundResource(R.mipmap.daohang);
        tvStyle.setTextColor(Color.parseColor("#202124"));
        ivStyleIcon.setBackgroundResource(R.mipmap.daohang);
        tvPart.setTextColor(Color.parseColor("#202124"));
        ivPartIcon.setBackgroundResource(R.mipmap.daohang);
        tvHouseStyle.setTextColor(Color.parseColor("#202124"));
        ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang);
        tvHouseColor.setTextColor(Color.parseColor("#202124"));
        ivColorIcon.setBackgroundResource(R.mipmap.daohang);
    }

    private void setTextStyle(int position) {
        switch (position) {
            case 0:
                tvSpace.setTextColor(Color.parseColor("#FFA64F"));
                ivSpaceIcon.setBackgroundResource(R.mipmap.daohang2);
                tvStyle.setTextColor(Color.parseColor("#202124"));
                ivStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvPart.setTextColor(Color.parseColor("#202124"));
                ivPartIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseStyle.setTextColor(Color.parseColor("#202124"));
                ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseColor.setTextColor(Color.parseColor("#202124"));
                ivColorIcon.setBackgroundResource(R.mipmap.daohang);
                break;
            case 1:
                tvSpace.setTextColor(Color.parseColor("#202124"));
                ivSpaceIcon.setBackgroundResource(R.mipmap.daohang);
                tvStyle.setTextColor(Color.parseColor("#FFA64F"));
                ivStyleIcon.setBackgroundResource(R.mipmap.daohang2);
                tvPart.setTextColor(Color.parseColor("#202124"));
                ivPartIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseStyle.setTextColor(Color.parseColor("#202124"));
                ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseColor.setTextColor(Color.parseColor("#202124"));
                ivColorIcon.setBackgroundResource(R.mipmap.daohang);
                break;
            case 2:
                tvSpace.setTextColor(Color.parseColor("#202124"));
                ivSpaceIcon.setBackgroundResource(R.mipmap.daohang);
                tvStyle.setTextColor(Color.parseColor("#202124"));
                ivStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvPart.setTextColor(Color.parseColor("#FFA64F"));
                ivPartIcon.setBackgroundResource(R.mipmap.daohang2);
                tvHouseStyle.setTextColor(Color.parseColor("#202124"));
                ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseColor.setTextColor(Color.parseColor("#202124"));
                ivColorIcon.setBackgroundResource(R.mipmap.daohang);
                break;
            case 3:
                tvSpace.setTextColor(Color.parseColor("#202124"));
                ivSpaceIcon.setBackgroundResource(R.mipmap.daohang);
                tvStyle.setTextColor(Color.parseColor("#202124"));
                ivStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvPart.setTextColor(Color.parseColor("#202124"));
                ivPartIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseStyle.setTextColor(Color.parseColor("#FFA64F"));
                ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang2);
                tvHouseColor.setTextColor(Color.parseColor("#202124"));
                ivColorIcon.setBackgroundResource(R.mipmap.daohang);
                break;
            case 4:
                tvSpace.setTextColor(Color.parseColor("#202124"));
                ivSpaceIcon.setBackgroundResource(R.mipmap.daohang);
                tvStyle.setTextColor(Color.parseColor("#202124"));
                ivStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvPart.setTextColor(Color.parseColor("#202124"));
                ivPartIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseStyle.setTextColor(Color.parseColor("#202124"));
                ivHouseStyleIcon.setBackgroundResource(R.mipmap.daohang);
                tvHouseColor.setTextColor(Color.parseColor("#FFA64F"));
                ivColorIcon.setBackgroundResource(R.mipmap.daohang2);
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()){
            case EC.EventCode.CHOOSE_CITY_TO_GET_DATA_FROM_NET_HOUSE:
                city = (String)event.getData();
                page = 1;
                pageSize = 10;
                getDataFromNet(parameter, page, pageSize, city, false);
                break;
        }
    }
}
