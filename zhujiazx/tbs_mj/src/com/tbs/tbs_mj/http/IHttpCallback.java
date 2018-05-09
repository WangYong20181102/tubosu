package com.tbs.tbs_mj.http;

public interface IHttpCallback 
{
	public void onUpdate(int aRequestId, int aTotalSize, int aCompleteSize);
	public void onComplete(int aRequestId, int aErrCode, Object aResult);

}
