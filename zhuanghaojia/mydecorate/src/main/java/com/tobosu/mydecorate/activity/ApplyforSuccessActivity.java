package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.tobosu.mydecorate.R;


/**
 *  申请成功页面
 * @author
 *
 */
public class ApplyforSuccessActivity extends Activity{
	private ImageView applyfor_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyfor_success);
		applyfor_back = (ImageView) findViewById(R.id.applyfor_back);
		applyfor_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(66);
				finish();
			}
		});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			setResult(66);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
