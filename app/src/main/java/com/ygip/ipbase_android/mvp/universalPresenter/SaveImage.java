package com.ygip.ipbase_android.mvp.universalPresenter;

/**
 * Created by LockyLuo on 2017/11/4.
 */

import android.os.AsyncTask;
import android.os.Environment;

import com.ygip.ipbase_android.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;



/***
 * 功能：用线程保存图片
 *
 */
public class SaveImage extends AsyncTask<String, Void, String> {
    private String imgurl = "";
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            imgurl=params[0];
            if (imgurl==null)
                return null;
            String sdcard = Environment.getExternalStorageDirectory().toString();
            File file = new File(sdcard + "/Download");
            if (!file.exists()) {
                file.mkdirs();
            }
            int idx = imgurl.lastIndexOf(".");
            String ext = imgurl.substring(idx);
            file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
            InputStream inputStream = null;
            URL url = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
            }
            byte[] buffer = new byte[4096];
            int len = 0;
            FileOutputStream outStream = new FileOutputStream(file);
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            result = "图片已保存至：" + file.getAbsolutePath();
        } catch (Exception e) {
            result = "保存失败！" + e.getLocalizedMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        ToastUtils.show(result);
    }
}
