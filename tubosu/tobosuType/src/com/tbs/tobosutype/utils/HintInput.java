package com.tbs.tobosutype.adapter.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

	/***
	 * 监听虚拟键盘的工具类
	 * @author dec
	 *
	 */
public class HintInput {
	private int length;
	private EditText editText;
	private Context context;

	public HintInput(int length, EditText editText, Context context) {
		this.length = length;
		this.editText = editText;
		this.context = context;
		editText.addTextChangedListener(textWatcher);
		
	}

	public TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			hideInput(context, editText);
		}
	};
	
	private void hideInput(Context context, View view) {
		if (editText.getText().toString().trim().length() == length) {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

}
