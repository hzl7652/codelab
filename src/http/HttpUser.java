package http;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HttpUser {
	static String cookie = null;
	public static void main(String[] args) throws MalformedURLException, IOException {
		get("http://localhost:8080/teach");
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("username", "admin100");
		params.put("password", "000000");
		post("http://localhost:8080/teach/login/doLogin", params);
	}
	
	public static void get(String url) throws MalformedURLException, IOException{
		HttpURLConnection  con =  (HttpURLConnection) new URL(url).openConnection();
		con.setInstanceFollowRedirects(false);
		con.setDoInput(true);
		if(cookie != null){
			con.setRequestProperty("Cookie", cookie);
		}
		con.connect();
		System.out.println(con.getURL().toString());
		System.out.println(con.getResponseCode());
		Map<String,List<String>> params = con.getHeaderFields();
		for(String key : params.keySet()){
			StringBuffer sb = new StringBuffer();
			for(String k : params.get(key)){
				if(sb.length() != 0){
					sb.append(",");
				}
				sb.append(k);
			}
			System.out.println((key==null?"":(key+":"))+sb.toString());
		}
		if(302 == con.getResponseCode()){
			get(params.get("Location").get(0));
		}
		if(cookie == null){
			String cc = params.get("Set-Cookie").get(0);
			if(cc.indexOf(";") != -1){
			cookie = cc.substring(0,cc.indexOf(";"));
		}else{
			cookie = cc;
		}
		System.out.println("cookie:"+cookie);
		}
//		Scanner sc = new Scanner(con.getInputStream());
//		while(sc.hasNextLine()){
//			System.out.println(sc.nextLine());
//		}
		con.disconnect();
	}
	public static void post(String url,Map<String,Object> params) throws MalformedURLException, IOException{
		HttpURLConnection  con =  (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestProperty("Cookie", cookie);
		con.connect();
		PrintWriter pw  = new PrintWriter(con.getOutputStream(),true);
		StringBuffer sb1 = new StringBuffer();
		for(String key: params.keySet()){
			if(sb1.length()!=0){
				sb1.append("&");
			}
			sb1.append(key+"="+URLEncoder.encode(params.get(key).toString(),"utf-8"));
		}
		if(sb1.length()>0)
			pw.print(sb1.toString());
		pw.flush();
		System.out.println(con.getURL().toString());
		System.out.println(con.getResponseCode());
		Scanner sc = new Scanner(con.getInputStream());
		while(sc.hasNextLine()){
			System.out.println(sc.nextLine());
		}
		Map<String,List<String>> headers = con.getHeaderFields();
		for(String key : headers.keySet()){
			StringBuffer sb = new StringBuffer();
			for(String k : headers.get(key)){
				if(sb.length() != 0){
					sb.append(",");
				}
				sb.append(k);
			}
			System.out.println((key==null?"":(key+":"))+sb.toString());
		}
		if(con.getResponseCode() == 302){
			get(headers.get("Location").get(0));
		}
		con.disconnect();
	}
}
