package com.russia.meetster.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.russia.meetster.data.MeetsterFriend;

public class NetworkUtils {
	public static int STATUS_OK = 200;
	
	public static String SERVER_URI = "http://10.0.2.2:5000";
	public static String CREATEUSER_URI = SERVER_URI + "/make-user";
	public static String GETUSERBYEMAIL_URI = SERVER_URI + "/get-user-by-email";
	public static String SYNCUSER_URI = SERVER_URI + "/sync-user";
	
	public static String PARAM_USERINFO = "userinfo";
	
	public static HttpClient getHttpClient() {
		HttpClient client = new DefaultHttpClient();
		return client;
	}
	
	public static MeetsterFriend getRemoteUserInfo(String email) {
		return null;
	}
	
	// Returns the new remote id
	public static long createRemoteUser(MeetsterFriend friend) throws ClientProtocolException, IOException, UnsupportedEncodingException, JSONException {
		JSONObject friendJSON = friend.toJSON();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_USERINFO, friendJSON.toString()));
		
		HttpEntity entity = new UrlEncodedFormEntity(params);
	
		HttpPost post = new HttpPost(CREATEUSER_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);
		
		HttpClient client = getHttpClient();
		
		HttpResponse resp = client.execute(post);
		
		if (resp.getStatusLine().getStatusCode() == STATUS_OK) {
			String response = EntityUtils.toString(resp.getEntity());
			JSONObject json = new JSONObject(response);
			return json.getLong("id");
		} else {
			return -1;
		}
	}
}
