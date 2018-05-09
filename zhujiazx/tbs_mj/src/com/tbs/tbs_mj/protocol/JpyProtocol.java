package com.tbs.tbs_mj.protocol;

import java.util.HashMap;

import android.annotation.SuppressLint;
import com.tbs.tbs_mj.http.IHttpCallback;
import com.tbs.tbs_mj.http.SingleHttpClient;

public class JpyProtocol implements IHttpCallback {
	private static JpyProtocol iInstance;
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, MDataUpdateNotify> iObserverMap = new HashMap<Integer, MDataUpdateNotify>();
	
	public static JpyProtocol GetInstance() {
		if (iInstance == null) {
			iInstance = new JpyProtocol();
		}
		return iInstance;
	}

	public static void Destroy() {
		if (iInstance != null) {
			SingleHttpClient.Instance().StopRequestThread();
			iInstance.iObserverMap.clear();
			iInstance = null;
		}
	}

	public interface MDataUpdateNotify {
		public boolean OnNewDataArrived(int aRequestType, int aErrCode, Object aData);
	}






	@Override
	public void onUpdate(int aRequestId, int aTotalSize, int aCompleteSize) {

	}

	@Override
	public void onComplete(int aRequestId, int aErrCode, Object aResult) {
		
	}


}