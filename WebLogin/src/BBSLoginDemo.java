import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class BBSLoginDemo {
	
	private final static String uid = "LittleMumu";
	private final static String pwd = "890921zxl";
	
	private static String baseUrl = "http://bbs.nju.edu.cn";
	private static String userDir = "/vd" + (int)(Math.random()*10000);
	private static String loginApp = "/bbslogin?type=2";
	private static String boardApp = "/bbssnd?board=";
	
	private static String cookieOrigStr = "";
	
	public static void loginAndPost(String postTitle, String postText, String board) throws Exception {
		// the board you're going to send a new post
		boardApp += board;
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(baseUrl+userDir+loginApp);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("id", uid));
            nvps.add(new BasicNameValuePair("pw", pwd));
            nvps.add(new BasicNameValuePair("lasturl", "bbs.nju.edu.cn/bbsmain"));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = httpclient.execute(httpPost);
            
            HttpEntity entity = response.getEntity();
            
           // System.out.println("content:"+EntityUtils.toString(entity));
            if (entity != null) {
            	InputStream instream = entity.getContent();
            	int l;
            	byte[] tmp = new byte[2048];
            	while ((l = instream.read(tmp)) != -1) {
            	}
            	
            	String entityStr = new String(tmp);
            	
            	// after login, some cookies are set, get the cookie origin string
            	cookieOrigStr = entityStr.substring(entityStr.indexOf("Cookie('")+8, entityStr.indexOf("')</script>"));
            	System.out.println(cookieOrigStr);
            }
            
            System.out.println("Login completed!");
            
            // After login, send a post
            httpPost = new HttpPost(baseUrl+userDir+boardApp);
            //httpPost.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.ACCEPT_ALL); // lenient cookie accepting policy
            nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("title", postTitle));
            nvps.add(new BasicNameValuePair("pid", "0"));
            nvps.add(new BasicNameValuePair("reid", "0"));
            nvps.add(new BasicNameValuePair("signature", "1"));
            nvps.add(new BasicNameValuePair("autocr", "on"));
            nvps.add(new BasicNameValuePair("text", postText));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            
            // set header properties of httpPost
            httpPost.setHeader("Referer", baseUrl+userDir+boardApp);
            httpPost.setHeader("Cache-Control", "max-age=0");
            httpPost.setHeader("Connection", "keep-alive");
            
            httpclient = new DefaultHttpClient();	// very important, use this to create a new connection
         // before post, must set cookies
            setCookies(cookieOrigStr, httpclient);
            
            response = httpclient.execute(httpPost);
            
            entity = response.getEntity();
            if (entity != null) {
            	InputStream instream = entity.getContent();
            	int l;
            	byte[] tmp = new byte[2048];
            	while ((l = instream.read(tmp)) != -1) {
            	}
            	
            	String entityStr = new String(tmp);
            	System.out.println(entityStr);
            	System.out.println("----------------------------------------");
            }
           
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        
	}
	
	public static void setCookies(String cookieOrigStr, DefaultHttpClient httpclient) {
		if(cookieOrigStr==null || "".equals(cookieOrigStr)) {
			return;
		}
		
		int nIndex = cookieOrigStr.indexOf('N', 0);
		int plusIndex = cookieOrigStr.indexOf('+', 0);
		String uNumVal = String.valueOf(Long.parseLong(cookieOrigStr.substring(0, nIndex))+2);
		String uIDVal = cookieOrigStr.substring(nIndex+1, plusIndex);
		String uKeyVal = String.valueOf(Long.parseLong(cookieOrigStr.substring(plusIndex+1))-2);
		
		
	    System.out.println("----------------------------------------");
	    System.out.println(uNumVal+"\t"+uIDVal+"\t"+uKeyVal);
	    System.out.println("----------------------------------------");
		
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		// Populate cookies if needed
		BasicClientCookie[] cookies = new BasicClientCookie[3];
		cookies[0] = new BasicClientCookie("_U_NUM", uNumVal);
		cookies[1] = new BasicClientCookie("_U_UID", uIDVal);
		cookies[2] = new BasicClientCookie("_U_KEY", uKeyVal);
		//cookies[3] = new BasicClientCookie("FOOTKEY", "");
		
		for(int i=0;i<cookies.length;i++) {
			cookies[i].setVersion(0);// need?
			cookies[i].setDomain("bbs.nju.edu.cn");
			cookies[i].setPath(userDir+"/");// or userDir+"/"
			
			cookieStore.addCookie(cookies[i]);
		}
		
		// Set the cookie store
		httpclient.setCookieStore(cookieStore);
	}
	
	public static void main(String[]args) {
		try {
			// send a post to test board
			loginAndPost("Test2, ignore it", "just for test 2, you can ignore it", "test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
