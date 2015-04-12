package com.david.pda.util.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class PostUtil {
	public String postForString(Map<String, String> pair, String url) {
		String result = null;
		System.out.println("start url:" + url);
		// 第一步，创建HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		// 设置HTTP POST请求参数必须用NameValuePair对象
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : pair.keySet()) {
			params.add(new BasicNameValuePair(key, pair.get(key)));
		}
		try {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result:" + result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end url:" + url);
		return result;
	}
}
