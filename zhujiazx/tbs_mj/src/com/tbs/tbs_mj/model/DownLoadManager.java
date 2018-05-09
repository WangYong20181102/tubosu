package com.tbs.tbs_mj.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tbs.tbs_mj.customview.CustomProgressDialog;

import android.os.Environment;

public class DownLoadManager {
	public static File getFileFromServer(String path, CustomProgressDialog pd)
			throws Exception {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			URL url = new URL(path);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(5000);

			pd.setMax(conn.getContentLength());

			InputStream is = conn.getInputStream();

			File file = new File(Environment.getExternalStorageDirectory(), "tobosutype.apk");

			FileOutputStream fos = new FileOutputStream(file);

			BufferedInputStream bis = new BufferedInputStream(is);

			byte[] buffer = new byte[1024];

			int len;

			int total = 0;

			while ((len = bis.read(buffer)) != -1) {

				fos.write(buffer, 0, len);

				total += len;

				pd.setProgress(total);

			}
			fos.close();

			bis.close();

			is.close();

			return file;

		}

		else {

			return null;

		}

	}

}
