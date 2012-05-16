package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.nutz.lang.random.StringGenerator;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public abstract class UserTask implements Runnable{
	protected String cookie = null;
	protected String host = null;
	protected Log log = Logs.getLog(this.getClass());
	public void setHost(String host){
		this.host = host;
	}
	public String getHost(){
		return host;
	}
	public void setCookie(String cookie){
		this.cookie = cookie;
	}
	public String getCookie(){
		return cookie;
	}
	protected String get(String url) throws MalformedURLException, IOException{
		HttpURLConnection  con =  (HttpURLConnection) new URL(url.indexOf("http")== -1?host+url:url).openConnection();
		log.debug(con.getURL());
		con.setInstanceFollowRedirects(false);
		con.setDoInput(true);
		if(cookie != null){
			con.setRequestProperty("Cookie", cookie);
		}
		con.connect();
		Map<String,List<String>> params = con.getHeaderFields();
		if(302 == con.getResponseCode()){
			return get(params.get("Location").get(0));
		}
		if(400 <= con.getResponseCode()){
			throw new IOException("连接出现问题，status code is:"+con.getResponseCode());
		}
		if(cookie == null){
			String cc = params.get("Set-Cookie").get(0);
			if(cc.indexOf(";") != -1){
			cookie = cc.substring(0,cc.indexOf(";"));
			}else{
			cookie = cc;
			}
		}
		if(params.get("Content-Type").get(0).indexOf("image") != -1){
			String filename = new StringGenerator(10,10).next();
			OutputStream os = new FileOutputStream(filename+".jpg");
			InputStream in = con.getInputStream();
			byte[] buffer = new byte[1024];
			int length ;
			while((length = in.read(buffer)) != -1){
				os.write(buffer, 0, length);
			}
			os.flush();
			os.close();
			return new File(filename+".jpg").getCanonicalPath();
		}else{
			StringBuffer sb = new StringBuffer();
			Scanner sc = new Scanner(con.getInputStream());
			while(sc.hasNextLine()){
				sb.append(sc.nextLine()).append("\n");
			}
			con.disconnect();
			return sb.toString();
		}
	}
	protected String post(String url,Params<String, String> params) throws MalformedURLException, IOException{
		HttpURLConnection  con =  (HttpURLConnection) new URL(url.indexOf("http")== -1?host+url:url).openConnection();
		log.debug(con.getURL());
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setInstanceFollowRedirects(false);
		StringBuffer sb1 = new StringBuffer();
		if(params!= null && params.size()> 0){
			for(int i=0;i<params.size();i++){
				Entry<String,String> kv = params.get(i);
				if(sb1.length()!=0){
					sb1.append("&");
				}
				sb1.append(kv.getKey()+"="+URLEncoder.encode(kv.getValue(),"utf-8"));
			}
		}
		con.setRequestProperty("Content-Length", ""+sb1.length());
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		if(cookie != null)
			con.setRequestProperty("Cookie", cookie);
		con.connect();
		PrintWriter pw  = new PrintWriter(con.getOutputStream(),true);
		if(sb1.length()>0)
			pw.print(sb1.toString());
		pw.flush();
		pw.close();
		Map<String,List<String>> headers = con.getHeaderFields();
		if(con.getResponseCode() == 302){
			get(headers.get("Location").get(0));
			return null;
		}
		if(400 <= con.getResponseCode()){
			Scanner sc = new Scanner(con.getErrorStream());
			StringBuffer sbss = new StringBuffer();
			while(sc.hasNextLine()){
				sbss.append(sc.nextLine()).append("\n");
			}
			log.error(sbss.toString());
			throw new IOException("连接出现问题，status code is:"+con.getResponseCode());
		}
		StringBuffer sb = new StringBuffer();
		Scanner sc = new Scanner(con.getInputStream());
		while(sc.hasNextLine()){
			sb.append(sc.nextLine()).append("\n");
		}
		con.disconnect();
		return sb.toString();
	}
	protected String postMulti(String url,Params<String,String> params) throws MalformedURLException, IOException{
		HttpURLConnection  con =  (HttpURLConnection) new URL(url.indexOf("http")== -1?host+url:url).openConnection();
		log.debug(con.getURL());
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setInstanceFollowRedirects(false);
		String boundary = "---------------------------7db2223527012e";
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
		if(cookie != null)
			con.setRequestProperty("Cookie", cookie);
		con.connect();
		OutputStream os  = con.getOutputStream();
		if(params!= null && params.size()> 0){
			for(int i=0;i<params.size();i++){
				
				Entry<String,String> kv = params.get(i);
				if(kv.getKey().startsWith("file:")){
					File file = new File(kv.getValue());
					if(!file.exists()) continue;
					os.write(("--"+boundary).getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("Content-Disposition: form-data; name=\"".getBytes("utf-8"));
					os.write(kv.getKey().substring(5).getBytes("utf-8"));
					os.write("\"; filename=\"".getBytes("utf-8"));
					os.write(kv.getValue().getBytes("utf-8"));
					os.write("\"\r\n".getBytes("utf-8"));
					os.write("Content-Type: image/pjpeg\r\n\r\n".getBytes("utf-8"));
					byte[] buffer = new byte[1024];
					FileInputStream fr = new FileInputStream(file);
					int len = 0;
					while((len = fr.read(buffer))!= -1)
						os.write(buffer,0,len);
					os.write("\r\n".getBytes("utf-8"));
				}else{
					os.write(("--"+boundary).getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("Content-Disposition: form-data; name=\"".getBytes("utf-8"));
					os.write((kv.getKey()+"\"").getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write(kv.getValue().getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
				}
			}
			os.write(("--"+boundary).getBytes("utf-8"));
			os.write("--\r\n".getBytes("utf-8"));
		}
		os.flush();
		os.close();
		Map<String,List<String>> headers = con.getHeaderFields();
		if(con.getResponseCode() == 302){
			get(headers.get("Location").get(0));
			return null;
		}
		if(400 <= con.getResponseCode()){
			Scanner sc = new Scanner(con.getErrorStream());
			StringBuffer sbss = new StringBuffer();
			while(sc.hasNextLine()){
				sbss.append(sc.nextLine()).append("\n");
			}
			log.error(sbss.toString());
			throw new IOException("连接出现问题，status code is:"+con.getResponseCode());
		}
		StringBuffer sb = new StringBuffer();
		Scanner sc = new Scanner(con.getInputStream());
		while(sc.hasNextLine()){
			sb.append(sc.nextLine()).append("\n");
		}
		con.disconnect();
		return sb.toString();
	}
}
