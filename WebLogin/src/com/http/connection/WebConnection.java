package com.http.connection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.constant.WebConstant;

/**
 * @author X.L.Zhang
 * 下午1:31:25 2012-11-19
 */
public class WebConnection {
	
	
	/**
	 * Login for www.renren.com
	 * */
	public static void renrenLogin(){
		HttpClient client = new DefaultHttpClient();
		
		try {
			
			//post 方式登录
			HttpPost post = new HttpPost(WebConstant.RENREN_LOGIN);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("email", WebConstant.RENREN_EMAIL));
			nvps.add(new BasicNameValuePair("password", WebConstant.RENREN_PASSWORD));
			
			post.setEntity(new UrlEncodedFormEntity(nvps));
			
			HttpResponse response = client.execute(post);
			
			System.out.println("=====================Login Completed!=====================");
			
			System.out.println("======================Content=====================");
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());
			
			if(null!= entity){
				System.out.println("response length:"+entity.getContentLength());
				System.out.println("content:"+EntityUtils.toString(entity));
			}
			
			int status = response.getStatusLine().getStatusCode();
			
			System.out.println("======================Redirect URL=====================");
			
			
			if(status == HttpStatus.SC_MOVED_PERMANENTLY || status== HttpStatus.SC_MOVED_TEMPORARILY){
				Header location = response.getFirstHeader("Location");
				String redirectUrl = location.getValue();
				HttpGet get = new HttpGet(redirectUrl);
				
				HttpClient reClient = new DefaultHttpClient();
				HttpResponse response2 = reClient.execute(get);
				
				HttpEntity entity2 = response2.getEntity();
				if(null != entity2){
					System.out.println("response length:"+entity2.getContentLength());
					//System.out.println("content:"+EntityUtils.toString(entity2));
				}
			}
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			client.getConnectionManager().shutdown();
		}
		
	}
	
	/**
	 * Login for www.xiaomi.com
	 * */
	public static void xiaomiLogin(){
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			HttpPost post = new HttpPost(WebConstant.XIAOMI_LOGIN);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user", WebConstant.XIAOMI_USER));
			nvps.add(new BasicNameValuePair("pwd", WebConstant.RENREN_PASSWORD));
			nvps.add(new BasicNameValuePair("auto", "true"));
			
			HttpEntity postEntity = new UrlEncodedFormEntity(nvps,"UTF-8");
			post.setEntity(postEntity);
			post.setHeader("Connection","keep-alive");
			post.setHeader("User-Agent", WebConstant.FIREFOX);
			HttpResponse response = client.execute(post);
			
			int status = response.getStatusLine().getStatusCode();  //get the status code
			
			System.out.println("=====================Login Completed!=====================");

			System.out.println("=====================Headers=====================");
			Header[] headers = response.getAllHeaders();
			for(Header h:headers){
				System.out.println(h.getName()+":\t"+h.getValue());
			}
			
			System.out.println("=====================Cookies=====================");
			List<Cookie> cookies = client.getCookieStore().getCookies();
            setCookies(client,cookies); // 设置 cookies
			
			System.out.println("======================Content=====================");
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());
			
			if(null!= entity){
				System.out.println("response length:"+entity.getContentLength());
				System.out.println("content:"+EntityUtils.toString(entity));
			}
			
			System.out.println("======================Redirect URL=====================");
			
			
			if(status == HttpStatus.SC_MOVED_PERMANENTLY || status== HttpStatus.SC_MOVED_TEMPORARILY){
				Header location = response.getFirstHeader("Location");
				String redirectUrl = location.getValue();
				HttpGet get = new HttpGet(redirectUrl);
				
				HttpClient reClient = new DefaultHttpClient();
				HttpResponse response2 = reClient.execute(get);
				
				HttpEntity entity2 = response2.getEntity();
				if(null != entity2){
					System.out.println("response length:"+entity2.getContentLength());
					System.out.println("content:"+EntityUtils.toString(entity2));
				}
			}
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			client.getConnectionManager().shutdown();
		}
		
	}
	
	public static void setCookies(DefaultHttpClient client, List<Cookie> cookies){
		if (cookies.isEmpty()) {
            System.out.println("Cookie is empty!");
            return;
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println(cookies.get(i).toString());
        
                client.getCookieStore().addCookie(cookies.get(i));
            }
        }
		
	}
}
