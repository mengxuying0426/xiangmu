package com.example.yanxiaopeidemo.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostUtil {

    public static final String url = "http://10.7.88.228/YanXiaoPei/MyImageServer";
    // public static final String url = "http://10.7.89.230:8080/YanXiaoPei/MyImageServer";
    //192.168.137.188
    public PostUtil() {

    }

    public String upLoadPhoto(String imgStr) throws Exception{
        if(imgStr==null){
            System.out.println("imgStr is null");
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            BasicNameValuePair bnvp = new BasicNameValuePair("param", imgStr);
            List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            list.add(bnvp);

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity entity2 = response.getEntity();
                String s = EntityUtils.toString(entity2);
                return s;
            }


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}