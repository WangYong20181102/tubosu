package com.tbs.tobosutype.http;

public interface IHttpCallback 
{
	public void onUpdate(int aRequestId, int aTotalSize, int aCompleteSize);
	public void onComplete(int aRequestId, int aErrCode, Object aResult);

}
