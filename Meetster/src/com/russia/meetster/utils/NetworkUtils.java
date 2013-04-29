package com.russia.meetster.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.data.MeetsterEvent;
import com.russia.meetster.data.MeetsterFriend;

public class NetworkUtils {
	public static int STATUS_OK = 200;
	
	public static String SERVER_URI = "http://10.0.2.2:5000";
	public static String CREATEUSER_URI = SERVER_URI + "/make-user";
	public static String GETUSERBYEMAIL_URI = SERVER_URI + "/get-user-by-email";
	public static String SYNCUSER_URI = SERVER_URI + "/sync-user";
	
	public static String PARAMS_USERINFO = "userinfo";
	public static String PARAMS_USERID = "userid";
	public static String PARAMS_LASTSYNCTIME = "last-sync-time";
	public static String PARAMS_EVENTS = "events";
	public static String PARAMS_EMAIL = "email";
	
	public static HttpClient getHttpClient() {
		HttpClient client = new DefaultHttpClient();
		return client;
	}
	
	public static HttpResponse postToURI(String uri, List<NameValuePair> postData) throws ClientProtocolException, IOException {
		HttpEntity entity = new UrlEncodedFormEntity(postData);
		
		HttpPost post = new HttpPost(uri);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);
		
		HttpClient client = getHttpClient();
		
		return client.execute(post);
	}
	
	public static MeetsterFriend getRemoteUserInfo(String email) {
		return null;
	}
	
	// Returns the new remote id
	public static long createRemoteUser(MeetsterFriend friend) throws ClientProtocolException, IOException, UnsupportedEncodingException, JSONException {
		JSONObject friendJSON = friend.toJSON();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAMS_USERINFO, friendJSON.toString()));
		
		HttpResponse resp = postToURI(CREATEUSER_URI, params);
		
		if (resp.getStatusLine().getStatusCode() == STATUS_OK) {
			String response = EntityUtils.toString(resp.getEntity());
			JSONObject json = new JSONObject(response);
			return json.getLong("id");
		} else {
			return -1;
		}
	}
	
	public static List<MeetsterEvent> syncEvents(Context context, Long userid, String lastSyncTime, List<MeetsterEvent> dirtyEvents) throws ClientProtocolException, IOException, JSONException {
		List<JSONObject> tempEvents = new ArrayList<JSONObject>();
		for (MeetsterEvent event : dirtyEvents) {
			tempEvents.add(event.toJSON());
		}
		
		JSONArray localEvents = new JSONArray(tempEvents);
		
		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAMS_USERID, String.valueOf(userid)));
		params.add(new BasicNameValuePair(PARAMS_LASTSYNCTIME, lastSyncTime));
		params.add(new BasicNameValuePair(PARAMS_EVENTS, localEvents.toString()));
		
		HttpResponse resp = postToURI(SYNCUSER_URI, params);
		
		List<MeetsterEvent> remoteEvents = null;
		
		if (resp.getStatusLine().getStatusCode() == STATUS_OK) {
			String response = EntityUtils.toString(resp.getEntity());
			
			JSONArray eventsJSON = new JSONArray(response);
			
			remoteEvents = new ArrayList<MeetsterEvent>();
			for (int i = 0; i < eventsJSON.length(); i++) {
				JSONObject eventJSON = eventsJSON.getJSONObject(i);
				try {
					remoteEvents.add(new MeetsterEvent(context, eventJSON));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return remoteEvents;
	}
}
