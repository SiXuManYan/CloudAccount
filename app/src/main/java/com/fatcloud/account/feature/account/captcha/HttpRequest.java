package com.fatcloud.account.feature.account.captcha;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    public static final String UTF_8 = "UTF-8";

    private static String cookie = null;


    /**
     * GET请求
     *
     * @param actionUrl
     * @param params
     * @return
     */
    public static String httpGet(String actionUrl, Map<String, String> params) {
        try {
            StringBuffer urlbuff = new StringBuffer(actionUrl);
            if (params != null && params.size() > 0) {
                if (actionUrl.indexOf("?") >= 0) {
                    urlbuff.append("&");
                } else {
                    urlbuff.append("?");
                }
                for (String key : params.keySet()) {
                    urlbuff.append(key).append("=").append(URLEncoder.encode(params.get(key), UTF_8)).append("&");
                }
                urlbuff.deleteCharAt(urlbuff.length() - 1);
                Log.v("---request---Get---", urlbuff.toString());
            }
            URL url = new URL(urlbuff.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(false);// 允许输出
            conn.setUseCaches(false);// 不使用Cache
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", UTF_8);
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            int cah = conn.getResponseCode();
            if (cah != 200)
                throw new RuntimeException("请求url失败");
            if (conn.getHeaderField("Set-Cookie") != null) {
                cookie = conn.getHeaderField("Set-Cookie");
            }
            Log.i("", "------------------cookie:" + cookie);
            Map<String, List<String>> keys = conn.getHeaderFields();
            for (String key : keys.keySet()) {
                List<String> list = keys.get(key);
                for (String value : list) {
                    Log.i("", "header: key:" + key + "  values:" + value);
                }
            }

            InputStream is = conn.getInputStream();
            int ch;
            StringBuilder b = new StringBuilder();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            is.close();
            conn.disconnect();
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}