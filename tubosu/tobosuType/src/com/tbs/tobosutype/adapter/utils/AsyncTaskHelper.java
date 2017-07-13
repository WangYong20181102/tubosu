package com.tbs.tobosutype.adapter.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class AsyncTaskHelper {
	public void dataDownload(String url, OnDataDownloadListener downloadListener) {
		new MyTask(downloadListener).execute(url);
	}

	private class MyTask extends AsyncTask<String, Void, byte[]> {
		private OnDataDownloadListener downloadListener;

		public MyTask(OnDataDownloadListener downloadListener) {
			this.downloadListener = downloadListener;
		}

		@Override
		protected byte[] doInBackground(String... params) {
			BufferedInputStream bis = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				URL url = new URL(params[0]);
				HttpURLConnection httpConn = (HttpURLConnection) url
						.openConnection();
				httpConn.setDoInput(true);
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				if (httpConn.getResponseCode() == 200) {
					bis = new BufferedInputStream(httpConn.getInputStream());
					byte[] buffer = new byte[1024 * 8];
					int c = 0;
					while ((c = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, c);
						baos.flush();
					}
					return baos.toByteArray();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			downloadListener.onDataDownload(result);
			super.onPostExecute(result);
			
		}
	}

	public interface OnDataDownloadListener {
		void onDataDownload(byte[] result);
	}
}
