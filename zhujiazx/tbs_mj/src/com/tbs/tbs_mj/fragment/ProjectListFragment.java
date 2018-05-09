package com.tbs.tbs_mj.fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.SelectedImageDetailActivity;
import com.tbs.tbs_mj.customview.RoundAngleImageView;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;
import com.tbs.tbs_mj.utils.Util;
import com.tbs.tbs_mj.xlistview.XListView;
import com.tbs.tbs_mj.xlistview.XListView.IXListViewListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 精选详情页面的fragment
 */
public class ProjectListFragment extends Fragment implements IXListViewListener {
	private String TAG = "ProjectListFragment";
	public static final String BUNDLE_TITLE = "title";
	private String range_name;
	private View view;
	private XListView xlistView_projectlist;

	/**精选专题*/
	private String getListUrl = Constant.TOBOSU_URL + "tapp/spcailpic/get_list";

	private RequestParams getListParams;
	private int pageSize;
	private List<HashMap<String, String>> projectListDatas = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> requestProjectListDatas;
	private ProjectListAdapter projectListAdapter;
	private LinearLayout ll_not_data;
	private int width;
	private Activity activity;

	private int page = 1;
	private ArrayList<String> rangeName = null;
	private int fragmentPosition = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_projectlist, null);
		xlistView_projectlist = (XListView) view.findViewById(R.id.xlistView_projectlist);
		ll_not_data = (LinearLayout) view.findViewById(R.id.ll_not_data);
		xlistView_projectlist.setPullLoadEnable(true);
		xlistView_projectlist.setXListViewListener(this);
		xlistView_projectlist.setSelector(new ColorDrawable(Color.TRANSPARENT));

		Bundle arguments = getArguments();
		if (arguments != null) {
			range_name = arguments.getString(BUNDLE_TITLE);
		}

		initAdapter();
		getDataFromNet();

		initEvent();
		return view;
	}

	private void initAdapter(){
		projectListAdapter = new ProjectListAdapter();
		xlistView_projectlist.setAdapter(projectListAdapter);
	}

	private void getDataFromNet(){
		projectListAdapter = new ProjectListAdapter();
		projectListAdapter.notifyDataSetChanged();
		if(Util.isNetAvailable(getActivity())){
			OKHttpUtil.post(getListUrl, getListParams(), new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {

				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					final String s = response.body().string();
					if(getActivity()!=null){
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								System.out.println(" --ProjectListFragment 数据 --" + s + "--");
								try {
									JSONObject jsonObject = new JSONObject(s);
									if (jsonObject.getInt("error_code") == 0) {
										requestProjectListDatas = new ArrayList<HashMap<String, String>>();
										JSONObject dataObject = jsonObject.getJSONObject("data");
										JSONArray jsonArray = dataObject.getJSONArray("specialList");
										for (int i = 0; i < jsonArray.length(); i++) {
											HashMap<String, String> map = new HashMap<String, String>();
											JSONObject jsonObject2 = jsonArray.getJSONObject(i);
											map.put("id", jsonObject2.getString("id"));
											map.put("banner_title", jsonObject2.getString("banner_title"));
											map.put("thumb_img_url", jsonObject2.getString("thumb_img_url"));
											map.put("banner_description", jsonObject2.getString("banner_description"));
											requestProjectListDatas.add(map);
										}
										projectListDatas.addAll(requestProjectListDatas);
										onLoad();
										ll_not_data.setVisibility(View.GONE);

									} else if (jsonObject.getInt("error_code") == 70101) {
										ll_not_data.setVisibility(View.VISIBLE);
									} else if (jsonObject.getInt("error_code") == 70102) {
										xlistView_projectlist.stopLoadMore();
										Util.setToast(getActivity(), "没有加载更多的数据了！");
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}

				}
			});
		}
	}

	private HashMap<String, Object> hashMap = new HashMap<String, Object>();
	private HashMap<String, Object> getListParams(){
		hashMap.put("page", page+"");
		hashMap.put("pageSize", pageSize+"");
		hashMap.put("range_name", range_name);
		hashMap.put("version", AppInfoUtil.getAppVersionName(getActivity()));
		return hashMap;
	}

	private void initEvent() {

//		xlistView_projectlist.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
//				此方法无用
//			}
//		});
	}

	public static ProjectListFragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		ProjectListFragment fragment = new ProjectListFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	class ProjectListAdapter extends BaseAdapter {

		public ProjectListAdapter() {
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			width = dm.widthPixels;
		}

		@Override
		public int getCount() {
			if (projectListDatas != null) {
				return projectListDatas.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return projectListDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHodler viewHodler;
			if (convertView == null) {
				viewHodler = new ViewHodler();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_project_listview, null);
				viewHodler.roundAngleImageView = (RoundAngleImageView) convertView.findViewById(R.id.roundAngleImageView);
				viewHodler.title = (TextView) convertView.findViewById(R.id.title);
				viewHodler.tvDescr = (TextView) convertView.findViewById(R.id.descr);
				ViewGroup.LayoutParams lp = viewHodler.roundAngleImageView.getLayoutParams();
				lp.width = width;
				lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				viewHodler.roundAngleImageView.setLayoutParams(lp);

				viewHodler.roundAngleImageView.setMaxWidth(width);
				viewHodler.roundAngleImageView.setMaxHeight((int) (width * 1.5));
				convertView.setTag(viewHodler);
			} else {
				viewHodler = (ViewHodler) convertView.getTag();
			}
			viewHodler.title.setText(projectListDatas.get(position).get("banner_title"));
			viewHodler.tvDescr.setText(projectListDatas.get(position).get("banner_description"));
			ImageLoaderUtil.loadImage(getActivity(), viewHodler.roundAngleImageView, projectListDatas.get(position).get("thumb_img_url"));

			Util.setErrorLog(TAG, projectListDatas.get(position).get("thumb_img_url"));


			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(), SelectedImageDetailActivity.class);
					intent.putExtra("id", projectListDatas.get(position).get("id"));
//					Util.setLog(TAG, "列表item的id是" + projectListDatas.get(position).get("id"));
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	class ViewHodler {
		RoundAngleImageView roundAngleImageView;
		TextView title;
		TextView tvDescr;
	}

	@Override
	public void onRefresh() {
		page = 1;
//		getListParams.put("page", page);
		hashMap.put("page", page+"");
//		requestGetList();
		getDataFromNet();
	}

	@Override
	public void onLoadMore() {
		page++;
//		getListParams.put("page", page);
		hashMap.put("page", page+"");
//		requestGetList();
		getDataFromNet();

	}

	private void onLoad() {
		projectListAdapter.notifyDataSetChanged();
		xlistView_projectlist.stopRefresh();
		xlistView_projectlist.stopLoadMore();
		xlistView_projectlist.setRefreshTime();
	}
}
