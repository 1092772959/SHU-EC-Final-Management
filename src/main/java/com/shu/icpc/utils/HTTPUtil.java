package com.shu.icpc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPUtil {
    public static byte[] getByteFrom(String urlStr){
        URL url = null;
        byte[] res = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            res = FileUtil.readInputStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }


}
