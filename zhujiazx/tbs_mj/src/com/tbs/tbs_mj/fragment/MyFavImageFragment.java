package com.tbs.tbs_mj.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.ImageDetailActivity;
import com.tbs.tbs_mj.activity.MyFavActivity;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
	 * 收藏页面 之 图册收藏页面
	 * @author dec
	 *
	 */
public class MyFavImageFragment extends Fragment {
	private static final String TAG = MyFavImageFragment.class.getSimpleName();
	
	/**gridview*/
	private GridView gridview_store;
	
	private ImageView iv_empty_data_store_images;
	
	private List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
	
	private FavImageAdapter favImageAdapter;
	
	/**我的收藏接口*/
	private String urlstore = Constant.TOBOSU_URL + "tapp/user/my_fav";
	
	/**添加或取消 接口*/
	private String delUrl = Constant.TOBOSU_URL + "tapp/user/fav";
	
	private int page = 1;
	private int pageSize = 70;
	
	/**收藏类型  <br />
	 * 		showpic 图片 <br />
	 * 		com 公司
	 * 
	 * */
	private String fav_type = "showpic";
	
	private HashMap<String, Object> params;
	private HashMap<String, Object> delParams;
	private SharedPreferences settings;
	private String token;
	
	private StringBuilder delId;
	public int delNum = 0;
	
	private List<Boolean> isEditDel = new ArrayList<Boolean>();
	
	private ImageViewHolder imgHolder;
	
	/**
	 * showF<br/>  true --> 显示勾选 <br/>
	 * 		  false --> 隐藏勾选
	 */
	private boolean showF = false;
	
//	/**
//	 *  等于0 是有收藏数据 <br/>
//	 *  大于0 是没收藏数据
//	 */
//	public int emptyData = 0;
	

	public List<HashMap<String, Object>> getDataList() {
		return dataList;
	}
	
	public void setDataList(List<HashMap<String, Object>> dataList) {
		this.dataList = dataList;
	}

	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x000000000012:
				if(dataList.size()==0){
					iv_empty_data_store_images.setVisibility(View.VISIBLE);
//					emptyData++;
				}
				if(favImageAdapter!=null){
					favImageAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		};
	};
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View storeView = inflater.inflate(R.layout.fragment_myfav_image, null);
		gridview_store = (GridView) storeView.findViewById(R.id.gridview_imagestore);
		iv_empty_data_store_images = (ImageView) storeView.findViewById(R.id.iv_empty_data_store_images);

		settings = getActivity().getSharedPreferences("userInfo", 0);
		token = settings.getString("token", "");
		
		if (!TextUtils.isEmpty(token)) {
//			params = AppInfoUtil.getPublicParams(getActivity().getApplicationContext());
			params = new HashMap<String, Object>();
			params.put("token", token);
			params.put("fav_type", fav_type);
			params.put("page", page + "");
			params.put("pageSize", pageSize + "");
			requestImageStorePost();
		}

		favImageAdapter = new FavImageAdapter(getActivity(), dataList); // false是初始状态
		gridview_store.setAdapter(favImageAdapter);
		favImageAdapter.notifyDataSetChanged();
		
		gridview_store.setSelector(new ColorDrawable(Color.TRANSPARENT));
		/*if(favImageAdapter!=null){
			gridview_store.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
					String conid = dataList.get(position).get("conid").toString();
					Bundle bundle = new Bundle();
					bundle.putString("id", conid);
					Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}*/
		
		return storeView;
	}

	
	
	
	public class FavImageAdapter extends BaseAdapter {

		private List<HashMap<String, Object>> list;

		private Context context;

		public FavImageAdapter(Context context, List<HashMap<String, Object>> list) {
			this.context = context;
			this.list = list;
		}

		public List<HashMap<String, Object>> getList() {
			return list;
		}
		
		public void setList(List<HashMap<String, Object>> list) {
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
			if(convertView==null){
				imgHolder = new ImageViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_imagestore, null);
				imgHolder.deleteImage = (ImageView) convertView.findViewById(R.id.item_gridview_image);
				imgHolder.deleteBox = (CheckBox) convertView.findViewById(R.id.item_gridview_edit);
				convertView.setTag(imgHolder);
			}else{
				imgHolder = (ImageViewHolder) convertView.getTag();
			}
			
//			ImageLoaderUtil.loadImage(getActivity(), imgHolder.deleteImage, list.get(position).get("img_url").toString());
			Glide.with(getActivity()).load(list.get(position).get("img_url").toString()).placeholder(R.drawable.icon_tobosu_default).into(imgHolder.deleteImage);
			imgHolder.deleteBox.setOnCheckedChangeListener(null); 
			if(showF==false){
				// 隐藏勾选
				imgHolder.deleteBox.setVisibility(View.GONE);
			}else{
				// 显示勾选
				
				if(isEditDel.get(position)==false){ //false
					imgHolder.deleteBox.setChecked(false);
				}
				
				imgHolder.deleteBox.setVisibility(View.VISIBLE);
				imgHolder.deleteBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						Boolean flag = !isEditDel.get(position); // 将false改为true 显示勾选中
						if (flag) {
							delNum++;
						} else {
							delNum--;
						}
						isEditDel.remove(position);
						isEditDel.add(position, flag);
						sendDeleteNumBroadcast();
					}

					
				});
			}
			
			return convertView;
		}
		
		
		
		/***
		 * 更新删除数
		 */
		private void sendDeleteNumBroadcast() {
			Intent intent = new Intent(MyFavActivity.DELETE_IMAGE_ACTION);
			Bundle b = new Bundle();
			b.putString("delete_image_num", delNum+"");
			intent.putExtra("delete_image_num_bundle", b);
			getActivity().sendBroadcast(intent);
		}
		/**
		 * 隐藏勾选 恢复未被勾选
		 */
		public void setSeletedDismiss(){
			
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
			sendDeleteNumBroadcast();
		}
		
		public void setSelectedShow(){
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
			sendDeleteNumBroadcast();
		}
		
	}
	
	class ImageViewHolder{
		ImageView deleteImage;
		CheckBox deleteBox;
	}


	
	
	/***
	 * 添加或删除接口请求
	 */
	public void deleteFavImages() {
		
		delParams = AppInfoUtil.getPublicHashMapParams(getActivity());
		delParams.put("oper_type", "0");
		delParams.put("token", token);
		delId = new StringBuilder();
		
		// 注意这个坑爹的id和conid 及相他们相应的key和value
		for (int i = 0; i < isEditDel.size(); i++) {
			if (isEditDel.get(i)) { // 选中
				if (delId.length() == 0) {
					delId.append(dataList.get(i).get("conid"));
				} else {
					delId.append(",").append(dataList.get(i).get("conid"));
				}
			}
		}
		
		delParams.put("fav_type", "showpic");
		delParams.put("id", delId + "");

		OKHttpUtil.post(delUrl, delParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Util.setToast(getActivity(), "删除图册失败，请稍后再试~");
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
								Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
							}
							showF = false;
							for (int i = 0; i < isEditDel.size(); i++) {
								if (isEditDel.get(i)) {
									dataList.remove(i);
									isEditDel.remove(i);
									i--;
								}
							}
							delNum = 0;
							Message message = new Message();
							message.what=0x000000000012;
							myHandler.sendMessage(message);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

	}

	/**
	 * 图库上的勾隐藏
	 */
	public void imageSelectedGoDie(){
		if(favImageAdapter!=null){
			favImageAdapter.setSeletedDismiss();
//			Toast.makeText(getActivity(), "888隐藏 路径", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 图库的勾显示
	 */
	public void showImagesSelectedView(){
		if(favImageAdapter!=null){
			favImageAdapter.setSelectedShow();
		}
	}
	
	
	/***
	 * 收藏接口请求
	 */
	private void requestImageStorePost() {
		OKHttpUtil.post(urlstore, params, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
//				dataList.clear();
				Util.setErrorLog(TAG, "收藏图片onFailure：==  " + e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String result = response.body().string();
				Util.setErrorLog(TAG, "收藏图片： " + result);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						List<HashMap<String, Object>> temDataList = new ArrayList<HashMap<String, Object>>();
						temDataList = (List<HashMap<String, Object>>) parseImageStoreListJSON(result);
						if (temDataList != null) {
							if (temDataList.size() == 0) {
								iv_empty_data_store_images.setVisibility(View.VISIBLE);
							} else {
								if (page == 1) {
									dataList.clear();
								}
								for (int i = 0; i < temDataList.size(); i++) {
									dataList.add(temDataList.get(i));
								}
								favImageAdapter.notifyDataSetChanged();
//								emptyData = 0;
							}
						} else {
							iv_empty_data_store_images.setVisibility(View.VISIBLE);
//							emptyData++;
						}
					}
				});

			}
		});
	}

	
	/****
	 * 解析json
	 * @param result
	 * @return
	 */
	private List<HashMap<String, Object>> parseImageStoreListJSON(String result) {
		try {
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				JSONObject dataObject = array.getJSONObject(i);
				dataMap.put("id", dataObject.getString("conid"));
				dataMap.put("conid", dataObject.getString("conid"));
				dataMap.put("img_url", dataObject.getString("img_url"));
				isEditDel.add(false);
				list.add(dataMap);
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
		if (favImageAdapter != null) {
			favImageAdapter.notifyDataSetChanged();
		}
	}
	
}
