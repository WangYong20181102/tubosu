package com.tbs.tobosutype.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.ChooseActivity;
import com.tbs.tobosutype.activity.MainActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.umeng.analytics.MobclickAgent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
	 * 获取设计和报价fragment
	 * @author dec
	 *
	 */
public class ChooseDecorateDesignFragment extends Fragment{
	private static final String TAG = ChooseDecorateDesignFragment.class.getSimpleName();
	
	private EditText et_input_cellnum_design;
	
	/**填写的电话号码*/
	private String phoneNum = "";
	
	private Button btn_take_design;
	
	private TextView tv_giveup;
	
	private HashMap<String, String> pubOrderParams;
	
	private String userid = "";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_choose_decorate_design, null);
		initView(view);
		return view;
	}
	
	
	
	private void initView(View view) {
		et_input_cellnum_design = (EditText) view.findViewById(R.id.et_input_cellnum_design);
		et_input_cellnum_design.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				phoneNum=et_input_cellnum_design.getText().toString().trim();
			}
		});
		
		getCellPhoneNum();
		
		btn_take_design = (Button) view.findViewById(R.id.btn_take_design);
		tv_giveup = (TextView) view.findViewById(R.id.tv_giveup);
		
		
		tv_giveup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				//现在无视获取，则标记为 1 ，即在首再次显示让用户获取
				getActivity().getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 1).commit();
				MobclickAgent.onEvent(getActivity(), "click_receive immediately_fail");
				// 首次安装标记为5
				if("5".equals(getActivity().getSharedPreferences("Go_ChooseActivity_SP", Context.MODE_PRIVATE).getString("go_chooseStyle_string", "0"))){
					Intent intent = new Intent();
					intent.setClass(getActivity(), MainActivity.class);
					Bundle b = new Bundle();
					b.putString("first_install_string", "10");
					intent.putExtra("first_install_bundle", b);
					startActivity(intent);
				}
				getActivity().finish();
			}
		});
		
		
		btn_take_design.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!"".equals(phoneNum)&& isPhoneNum(phoneNum)){
					pd = ProgressDialog.show(getActivity(), "", "正在提交中...");
					// 类型 面积 电话号码等信息 提交
					initParams();
					pubOrderParams.put("cellphone", et_input_cellnum_design.getText().toString().trim());
					
					MobclickAgent.onEvent(getActivity(), "click_receive immediately");
					// 请求网络
					requestPubOrder();
				}else if("".equals(phoneNum)){
					Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void getCellPhoneNum(){
		// 获取手机内sim卡电话号码
		TelephonyManager tManager=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		
		if(isSimReady() && "".equals(et_input_cellnum_design.getText().toString().trim())){
			// 有电话卡 有电话号码
			phoneNum = tManager.getLine1Number()==null?"":tManager.getLine1Number()+"";
			System.out.println("获取手机sim卡里面的号码：【"+phoneNum+"】");
			if(phoneNum.contains("+86")){
				phoneNum = phoneNum.substring(3);
			}
			et_input_cellnum_design.setText(phoneNum);
			
			System.out.println("ChooseDecorateDesignFragment 默认获取手机sim卡的电话号码" + phoneNum);
		}else{
			phoneNum = et_input_cellnum_design.getText().toString().trim();
		}

	}

	/**
	 * 手机是否有sim卡
	 * @return
	 */
	private boolean isSimReady(){
		TelephonyManager tManager=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		int simState = tManager.getSimState();
		boolean hasSim = false;
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			hasSim = false;
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			hasSim = false;
			break;
		case TelephonyManager.SIM_STATE_READY:
			hasSim = true;
			break;
		}
		return hasSim;
	}
	
	
	private ProgressDialog pd;

	
	private void initParams(){
		pubOrderParams = AppInfoUtil.getPublicHashMapParams(getActivity().getApplicationContext());
		String city = getActivity().getSharedPreferences("Save_City_Info", Context.MODE_PRIVATE).getString("save_city_now", "");
		pubOrderParams.put("style",ChooseActivity.chooseString.get(0)); // 类型
		pubOrderParams.put("housearea", ChooseActivity.chooseString.get(1));
		pubOrderParams.put("city", city);//必传
		System.out.println("ChooseDecorateDesignFragment -- 必传城市是" + city);
		pubOrderParams.put("urlhistory", Constant.PIPE);
		
		System.out.println("类型["+ChooseActivity.chooseString.get(0)+"]  面积["+ChooseActivity.chooseString.get(1)+"]  电话["+et_input_cellnum_design.getText().toString().trim()+"]");
		
		// 发单入口
		pubOrderParams.put("comeurl", Constant.PIPE);
		userid = AppInfoUtil.getUserid(getActivity().getApplicationContext());
		if (!TextUtils.isEmpty(userid)) {
			pubOrderParams.put("userid", userid);
		} else {
			pubOrderParams.put("userid", "0"); //未登录
		}
		pubOrderParams.put("source", "942"); // 端口标记
	}
	
	
	/***
	 * 发单接口请求
	 */
	private void requestPubOrder() {

		OKHttpUtil.post(Constant.PUB_ORDERS, pubOrderParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String result = response.body().string();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getInt("error_code") == 0) {
								Toast.makeText(getActivity(), "恭喜你获取报价成功！", Toast.LENGTH_SHORT).show();
								Log.d(TAG, "--提交成功--");
								// 首次安装标记为5
								if("5".equals(getActivity().getSharedPreferences("Go_ChooseActivity_SP", Context.MODE_PRIVATE).getString("go_chooseStyle_string", "0"))){
									Intent intent = new Intent();
									intent.setClass(getActivity(), MainActivity.class);
									Bundle b = new Bundle();
									b.putString("first_install_string", "11");
									intent.putExtra("first_install_bundle", b);
									startActivity(intent);
								}
								//现在获取了，则标记为 0 ，即在首不再显示让用户获取
								getActivity().getSharedPreferences("Cancel_Get_Design", Context.MODE_PRIVATE).edit().putInt("cancel_String", 0).commit();
								// 提交成功后页面消失
								getActivity().finish();
							} else {
								Toast.makeText(getActivity(), "提交失败 可在首页重新申请", Toast.LENGTH_SHORT).show();
								Log.d(TAG, "--提交失败--");
							}

							if(pd!=null&&pd.isShowing()){
								pd.dismiss();
								pd = null;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	/***
	 * 判断电话号码是否合法
	 * @param phoneNum
	 * @return
	 */
	public boolean isPhoneNum(String phoneNum) {
		if(TextUtils.isEmpty(phoneNum)|| "".equals(phoneNum)){
			Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
			return false;
		}else{
			String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
			Pattern pattern = Pattern.compile(MOBILE);
	        Matcher matcher = pattern.matcher(phoneNum); 
	        boolean flag = matcher.matches();
	        if(flag==false){
	        	Toast.makeText(getActivity(), "请输入合法手机号码", Toast.LENGTH_SHORT).show();
	        }
	        return matcher.matches();
		}
	}
	
	   
}
