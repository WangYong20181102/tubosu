package com.tbs.tobosutype.fragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DecorateCompanyDetailActivity;
import com.tbs.tobosutype.activity.MyFavActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
	 * 收藏页面 之 装修公司收藏
	 * @author dec
	 *
	 */
public class MyFavCompanyFragment extends Fragment {
	private static final String TAG = MyFavCompanyFragment.class.getSimpleName();
	
//	private XListView myfav_listView_decoratestore;
	
	private ListView myfav_listView;
	
	private ImageView iv_myfav_empty_data_decorateempty;
	
	/**数据来源*/
	private List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
	
	public MyFavDecorateAdapter myFavDecorateAdapter;
	
	/**我的收藏接口*/
	private String urlDecorateStore = Constant.TOBOSU_URL + "tapp/user/my_fav";
	
	/**添加或取消 接口*/
	private String delUrl = Constant.TOBOSU_URL + "tapp/user/fav";
	
	private int page = 1;
	private int pageSize = 50;
	
	/**收藏类型  <br />
	 * 		showpic 图片 <br />
	 * 		com 公司
	 * */
	private String fav_type = "com";
	
	private String token;
	private int delNum = 0;
	private StringBuilder delId;
	
	
//	private RequestParams delParams;
	private HashMap delParams;
	private List<Boolean> isEditDel = new ArrayList<Boolean>();

	/**
	 * 显示勾选的view <br/>
	 * 		false 隐藏view <br/>
	 * 		true  显示view
	 */
	private boolean showF = false;
	

	private Handler myCompanyHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x000000000015:
				if(dataList.size()==0){
					iv_myfav_empty_data_decorateempty.setVisibility(View.VISIBLE);
				}
				if(myFavDecorateAdapter!=null){
					myFavDecorateAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		};
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View decorateView = inflater.inflate(R.layout.fragment_myfav_company, null);
		myfav_listView = (ListView) decorateView.findViewById(R.id.myfav_listView_decoratestore);
		iv_myfav_empty_data_decorateempty = (ImageView) decorateView.findViewById(R.id.iv_myfav_empty_data_decorateempty);
		getDataFromNet();
		return decorateView;
	}
	
	
	
	private void getDataFromNet() {
		token = AppInfoUtil.getToekn(getActivity().getApplicationContext());

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("token", token);
		hashMap.put("fav_type", fav_type);
		hashMap.put("page", page+"");
		hashMap.put("pageSize", pageSize+"");
		OKHttpUtil.post(urlDecorateStore, hashMap, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, okhttp3.Response response) throws IOException {
				final String json = response.body().string();
				Util.setErrorLog(TAG, "收藏公司： " + json);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						List<HashMap<String, String>> temDataList = new ArrayList<HashMap<String, String>>();
						temDataList = (List<HashMap<String, String>>) parseDecorateStoreListJSON(json);
						if (temDataList != null) {
							if (temDataList.size() == 0) {
								iv_myfav_empty_data_decorateempty.setVisibility(View.VISIBLE);
							} else {
								if (page == 1) {
									dataList.clear();
								}
								for (int i = 0; i < temDataList.size(); i++) {
									dataList.add(temDataList.get(i));
								}
								if(dataList.size()>0){
									initCompanyAdapter();
									iv_myfav_empty_data_decorateempty.setVisibility(View.GONE);
								}else{
									myFavDecorateAdapter.notifyDataSetChanged();
									iv_myfav_empty_data_decorateempty.setVisibility(View.VISIBLE);
								}

//								if(myFavDecorateAdapter!=null){
//									myFavDecorateAdapter.notifyDataSetChanged();
//								}
							}
//							onLoad();
						}
					}
				});
			}
		});
    }
	


	private void initCompanyAdapter() {
		
		myFavDecorateAdapter = new MyFavDecorateAdapter(getActivity(), dataList);
		myfav_listView.setAdapter(myFavDecorateAdapter);
		myFavDecorateAdapter.notifyDataSetChanged();
		iv_myfav_empty_data_decorateempty.setVisibility(View.GONE);
//			myfav_listView.setPullLoadEnable(true);
//			myfav_listView.setXListViewListener(this);
		myfav_listView.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除默认的黄色背景
		
		myfav_listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				String comid = dataList.get(position).get("comid").toString();
				Intent detailIntent = new Intent(getActivity(), DecorateCompanyDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("comid", comid);
				detailIntent.putExtras(bundle);
				startActivity(detailIntent);
			}
		});
			
	}


	/***
	 * 装修公司列表adapter
	 * @author dec
	 *
	 */
	class MyFavDecorateAdapter extends BaseAdapter {
		private List<HashMap<String, String>> list = null;
		private Context context;

		public MyFavDecorateAdapter(Context context, List<HashMap<String, String>> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder = null;
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_decorate_list, null);
				mHolder.item_company_name =  (TextView) convertView.findViewById(R.id.item_company_name);
				mHolder.item_company_bao = (ImageView) convertView.findViewById(R.id.item_company_bao);
//				mHolder.item_company_addr = (TextView) convertView.findViewById(R.id.item_company_address);
				mHolder.item_design_num = (TextView) convertView.findViewById(R.id.item_design_num);
//				mHolder.item_design_solution = (TextView) convertView.findViewById(R.id.item_design_solution);
				mHolder.item_decorate_logo = (ImageView) convertView.findViewById(R.id.item_decorate_logo);
				mHolder.item_decorate_business_license = (ImageView) convertView.findViewById(R.id.item_decorate_business_license);
				mHolder.item_company_district = (TextView) convertView.findViewById(R.id.item_company_district);
				mHolder.item_decoratecompany_box = (CheckBox) convertView.findViewById(R.id.item_decoratecompany_box);
				
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			
			mHolder.item_company_name.setText(list.get(position).get("comsimpname"));
//			Log.d(TAG, "所在区域"+list.get(position).get("districtid"));
//			mHolder.item_company_addr.setText("address"); //无此字段
			mHolder.item_company_district.setText(list.get(position).get("districtid"));
			mHolder.item_design_num.setText(list.get(position).get("casenormalcount") + "套设计");
			
			// 无案例字段
//			if (list.get(position).get("anliCount").equals("0")) {
//				mHolder.item_design_solution.setVisibility(View.GONE);
//			} else {
//				mHolder.item_design_solution.setVisibility(View.VISIBLE);
//				mHolder.item_design_solution.setText(list.get(position).get("anliCount") + "套案例");
//			}
			
			final String logoUrl = list.get(position).get("logosmall").toString();
			final String certificationUrl = list.get(position).get("certification").toString();
			final String zxbUrl = list.get(position).get("jjb_logo").toString();

			if ("0".equals(certificationUrl)) {
				mHolder.item_decorate_business_license.setImageResource(R.drawable.image_business_license_unselect);
			} else {
				mHolder.item_decorate_business_license.setImageResource(R.drawable.image_business_license);
			}
			if ("0".equals(zxbUrl)) {
				mHolder.item_company_bao.setImageResource(R.drawable.image_bao);
			} else {
				mHolder.item_company_bao.setImageResource(R.drawable.image_bao_select);
			}

			if ("".equals(logoUrl)) {
				mHolder.item_decorate_logo.setImageResource(R.drawable.decorate_default);
			} else {
				ImageLoaderUtil.loadImage(getActivity(), mHolder.item_decorate_logo, logoUrl);
			}
			mHolder.item_decoratecompany_box.setOnCheckedChangeListener(null); 
			if(showF==false){
				mHolder.item_decoratecompany_box.setVisibility(View.GONE);
			}else{
				
				if(isEditDel.get(position)==false){ //false
					mHolder.item_decoratecompany_box.setChecked(false);
				}
				
				mHolder.item_decoratecompany_box.setVisibility(View.VISIBLE);
				mHolder.item_decoratecompany_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// 变成true
						Boolean flag = !isEditDel.get(position);
						if (flag) {
							delNum++;
						} else {
							delNum--;
						}
						isEditDel.remove(position); // 移除false
						isEditDel.add(position, flag); // 添加true进入集合
						sendDeleteNumberBroadcast();
					}
					
				});
			}
			
			return convertView;
		}
		
		
		class ViewHolder {
			/**是否选中图标*/
			private CheckBox item_decoratecompany_box;
			/**装修公司logo*/
			private ImageView item_decorate_logo;
			/**装修公司地址*/
//			private TextView item_company_addr;
			/**装修公司名称*/
			private TextView item_company_name;
			/**公司认证*/
			private ImageView item_decorate_business_license;
			/**装修保*/
			private ImageView item_company_bao;
			/**装修公司有多少套案例*/
//			private TextView item_design_solution;
			/**装修公司有多少套设计*/
			private TextView item_design_num;
			/**装修公司区域*/
			private TextView item_company_district;
		}

		/***
		 * 显示勾选
		 */
		public void setSelectedShow() {
			for(int i=0;i<list.size();i++){
				if(isEditDel.get(i)){
					isEditDel.remove(i);
					isEditDel.add(i, false);
				}
//				Log.d(TAG, "显示 外"+isEditDel.get(i));
				showF = true;
				notifyDataSetChanged();
			}
			delNum = 0;
			sendDeleteNumberBroadcast();
		}
		/**
		 * 隐藏勾选
		 */
		public void setSeletedDismiss() {
			for(int i=0;i<list.size();i++){
				if(isEditDel.get(i)){
					isEditDel.remove(i);
					isEditDel.add(i, false);
				}
//				Log.d(TAG, "消失 外"+isEditDel.get(i));
				showF = false;
				notifyDataSetChanged();
			}
			delNum = 0;
			sendDeleteNumberBroadcast();
		}

	}
	
	/***
	 * 更新删除数
	 */
	private void sendDeleteNumberBroadcast() {
		Intent intent = new Intent(MyFavActivity.DELETE_COMPANY_ACTION);
		Bundle b = new Bundle();
		b.putString("delete_company_num", delNum+"");
		intent.putExtra("delete_company_num_bundle", b);
		getActivity().sendBroadcast(intent);
	}
	
	/**
	 * 隐藏公司列表的勾选
	 */
	public void hideCompanySelectedView(){
		if(myFavDecorateAdapter!=null){
			myFavDecorateAdapter.setSeletedDismiss();
		}
	}
	
	
	/***
	 * 显示公司列表的勾选
	 * @author dec
	 */
	public void showCompanySelectedView(){
		if(myFavDecorateAdapter!=null){
			myFavDecorateAdapter.setSelectedShow();
//			Toast.makeText(getActivity(), "888隐藏 路径", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/***
	 * 添加或删除接口请求
	 */
	public void deleteFavCompanies() {
		
//		delParams = AppInfoUtil.getPublicParams(getActivity());
		delParams = new HashMap<String, String>();
		delParams.put("token", token);
		delParams.put("oper_type", "0");
		delParams.put("fav_type", fav_type);
		delId = new StringBuilder();
		
		// 注意这个坑爹的id和comid 及相他们相应的key和value
		for (int i = 0; i < isEditDel.size(); i++) {
			if (isEditDel.get(i)) {
				if (delId.length() == 0) {
					delId.append(dataList.get(i).get("comid"));
				} else {
					delId.append(",").append(dataList.get(i).get("comid"));
				}
			}
		}
//		Log.d(TAG,"要删除的id是"+delId+"");
//		delParams.put("comid", delId + "");
		delParams.put("id", delId + "");
		Log.d(TAG, "打印delParams"+delParams.toString());

		deleteCompany();
	}
	

	private void deleteCompany(){
		OKHttpUtil.post(delUrl, delParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Util.setToast(getActivity(), "删除装修公司失败，请稍后再试~");
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String json = response.body().string();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(json);
							int error_code = jsonObject.getInt("error_code");
							if(error_code==0){
								Util.setToast(getActivity(),jsonObject.getString("msg"));
							}
							showF = false;
							for (int i = 0; i < isEditDel.size(); i++) {
								if (isEditDel.get(i)) {
									isEditDel.remove(i);
									dataList.remove(i);
									i--;
									delNum = 0;
								}
							}
							Message message = new Message();
							message.what=0x000000000015;
							myCompanyHandler.sendMessage(message);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

	}
	
	/**
	 * 解析json
	 * @param result
	 * @return
	 */
	private List<HashMap<String, String>> parseDecorateStoreListJSON(String result) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject object = new JSONObject(result);
			int error_code = object.getInt("error_code");
			if (error_code == 0) {
				JSONArray array = object.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					HashMap<String, String> dataMap = new HashMap<String, String>();
					JSONObject dataObject = array.getJSONObject(i);
					dataMap.put("id", dataObject.getString("id"));
					dataMap.put("comid", dataObject.getString("comid"));
//					dataMap.put("conid", dataObject.getString("conid"));
					dataMap.put("comsimpname", dataObject.getString("comsimpname"));
					dataMap.put("logosmall", dataObject.getString("logosmall"));
					dataMap.put("certification", dataObject.getString("certification"));
					dataMap.put("jjb_logo", dataObject.getString("jjb_logo"));
					dataMap.put("dis", dataObject.getString("dis"));
					dataMap.put("casenormalcount", dataObject.getString("casenormalcount"));
					isEditDel.add(false);
					list.add(dataMap);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(myFavDecorateAdapter!=null){
			myFavDecorateAdapter.notifyDataSetChanged();
			if(myFavDecorateAdapter.getCount()==0){
				iv_myfav_empty_data_decorateempty.setVisibility(View.VISIBLE);
			}else {
				iv_myfav_empty_data_decorateempty.setVisibility(View.GONE);
			}
		}
	}
}
