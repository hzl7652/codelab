package browse;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * 模拟浏览器类
 * not suppot thread safe ,
 * 		because browser can't be used by two users at the some time
 * @author 王成
 *
 */
public class Browse {
	public static String DEFAULT_CHARSET = "UTF-8";
	public static String USERAGENT_CHROME = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.106 Safari/535.2";
	private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
	private static final int bufferSize = 0x20000;
	private Map<String,String> cookies = new HashMap<String, String>();
	private Map<String,String> headers = new HashMap<String, String>();
	private int timeout = 30000;
	private String host;
	private Proxy proxy;
	public Proxy proxy(){
		return proxy;
	}
	public void proxy(Proxy p){
		proxy = p;
	}
	public void setHost(String host){
		if(host!= null && !(host.startsWith("http")|| host.startsWith("https"))){
			throw new RuntimeException("目前只支持http和https");
		}
		this.host = host;
	}
	public String getHost(){
		return host;
	}
	public void cookie(String key,String value){
		cookies.put(key, value);
	}
	public String cookie(String key){
		return cookies.get(key);
	}
	public void useragent(String agent){
		headers.put("User-Agent", agent);
	}
	public String useragent(){
		return headers.get("User-Agent");
	}
	public void header(String key,String value){
		headers.put(key, value);
	}
	public String header(String key){
		return headers.get(key);
	}
	public int timeout(){
		return timeout;
	}
	public void timeout(int time){
		timeout = time;
	}

	/**
	 *  METHOD.GET
	 *  the key value must be encode by urlencode  or maybe some wrong text
	 *  @param url  可以是绝对路径或者相对路径（需要依赖host） 
	 *  只支持 http或者https
	 * @return
	 */
	public Response get(String url) throws IOException{
		
		HttpURLConnection  con = null;
		try{
			con = createCon(url);
			con.setConnectTimeout(timeout);
			con.setInstanceFollowRedirects(false);
			con.setDoInput(true);
			setProperties(con);
			con.connect();
			return toResult(con);
		}catch (IOException e) {
			if(con != null){
				con.disconnect();
			}
			throw e;
		}
	}
	/**
	 *  METHOD.POST
	 *  the key value must be encode by urlencode  or maybe some wrong text
	 *  @param url  可以是绝对路径或者相对路径（需要依赖host） 
	 *  只支持 http或者https
	 * @return
	 */
	public Response post(String url,Collection<KeyVal> params) throws IOException{
		HttpURLConnection  con = null;
		try{
			con = createCon(url);
			con.setConnectTimeout(timeout);
			con.setRequestMethod("POST");
			con.setInstanceFollowRedirects(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			setProperties(con);
			con.connect();
			OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream(),DEFAULT_CHARSET);
			if(params!= null && params.size() >0 ){
				boolean first = true;
				for(KeyVal p : params){
					if(first){
						first =false;
					}else{
						os.append('&');
					}
					os.write(URLEncoder.encode(p.key(),DEFAULT_CHARSET));
					os.write('=');
					os.write(URLEncoder.encode(p.val(),DEFAULT_CHARSET));
				}
			}
			os.flush();
			os.close();
			return toResult(con);
		}catch (IOException e) {
			if(con != null){
				con.disconnect();
			}
			throw e;
		}
	}
	/**
	 *  METHOD.POST  json
	 *  the key value must be encode by urlencode  or maybe some wrong text
	 *  @param url  可以是绝对路径或者相对路径（需要依赖host） 
	 *  只支持 http或者https
	 * @return
	 */
	public Response postJson(String url,Collection<KeyVal> params) throws IOException{
		HttpURLConnection  con = null;
		try{
			con = createCon(url);
			con.setConnectTimeout(timeout);
			con.setRequestMethod("POST");
			con.setInstanceFollowRedirects(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			setProperties(con);
			con.connect();
			OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream(),DEFAULT_CHARSET);
			if(params!= null && params.size() >0 ){
				boolean first = true;
				for(KeyVal p : params){
					if(first){
						first =false;
						os.append("{");
					}else{
						os.append(',');
					}
					os.write("\""+URLEncoder.encode(p.key(),DEFAULT_CHARSET)+"\"");
					os.write(':');
					os.write("\""+URLEncoder.encode(p.val(),DEFAULT_CHARSET)+"\"");
				}
				if(!first){
					os.append("}");
				}
			}
			os.flush();
			os.close();
			return toResult(con);
		}catch (IOException e) {
			if(con != null){
				con.disconnect();
			}
			throw e;
		}
	}
	/**
	 *  METHOD.POST
	 *  the key value must be encode by urlencode  or maybe some wrong text
	 *  @param url  可以是绝对路径或者相对路径（需要依赖host） 
	 *  只支持 http或者https
	 * @return
	 */
	public Response postMulti(String url,Collection<KeyVal> params,KeyVal... files) throws IOException{
		HttpURLConnection  con = null;
		try{
			con = createCon(url);
			con.setConnectTimeout(timeout);
			con.setRequestMethod("POST");
			con.setInstanceFollowRedirects(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			String boundary = "---------------------------7db2223527012e";
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
			setProperties(con);
			con.connect();
			OutputStream os = con.getOutputStream();
			boolean hasWrite = false;
			if(params!= null && params.size() >0 ){
				for(KeyVal p : params){
					os.write(("--"+boundary).getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("Content-Disposition: form-data; name=\"".getBytes("utf-8"));
					os.write((p.key()+"\"").getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write(p.val().getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
				}
				hasWrite = true;
			}
			if(files != null && files.length >0){
				for(KeyVal f : files){
					File file = new File(f.val());
					if(!file.exists()) throw new IOException("file:"+f.val()+" is not exists");
					os.write(("--"+boundary).getBytes("utf-8"));
					os.write("\r\n".getBytes("utf-8"));
					os.write("Content-Disposition: form-data; name=\"".getBytes("utf-8"));
					os.write(f.key().getBytes("utf-8"));
					os.write("\"; filename=\"".getBytes("utf-8"));
					os.write(f.val().getBytes("utf-8"));
					os.write("\"\r\n".getBytes("utf-8"));
					// TODO  the content-type should not be static , should set by the file content-type
					os.write("Content-Type: image/pjpeg\r\n\r\n".getBytes("utf-8"));
					byte[] buffer =  new byte[bufferSize];
					FileInputStream fr = new FileInputStream(file);
					int len = 0;
					while((len = fr.read(buffer))!= -1)
						os.write(buffer,0,len);
					os.write("\r\n".getBytes("utf-8"));
					fr.close();
				}
				hasWrite = true;
			}
			if(hasWrite){
				os.write(("--"+boundary).getBytes("utf-8"));
				os.write("--\r\n".getBytes("utf-8"));
			}
			os.flush();
			os.close();
			return toResult(con);
		} catch (IOException e){
			if(con != null){
				con.disconnect();
			}
			throw e;
		}
	}
	private ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        while(true) {
            read  = inStream.read(buffer);
            if (read == -1) break;
            outStream.write(buffer, 0, read);
        }
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        return byteData;
    }
	private String getCharsetFromContentType(String contentType) {
        if (contentType == null) return null;
        
        Matcher m = charsetPattern.matcher(contentType);
        if (m.find()) {
            return m.group(1).trim().toUpperCase();
        }
        return null;
    }
	private HttpURLConnection createCon(String url) throws IOException{
		if(url== null) throw new IOException("url must not null");
		String realUrl = null;
		if(url.indexOf("http") != -1)
			realUrl = url;
		else{
			if(host.endsWith("/") && url.startsWith("/")){
				realUrl = host + url.substring(1);
			}else if(!host.endsWith("/") && !url.startsWith("/")){
				realUrl = host+"/"+url;
			}else{
				realUrl = host + url;
			}
		}
		if(proxy != null)
			return  (HttpURLConnection) new URL(realUrl).openConnection(proxy);
		else{
			return  (HttpURLConnection) new URL(realUrl).openConnection();
		}
	}
	private void setProperties(HttpURLConnection con){
		if(headers.size()>0){
			for(Entry<String, String> header : headers.entrySet()){
				con.setRequestProperty(header.getKey(), header.getValue());
			}
		}
		if(cookies.size()>0){
			StringBuffer cookieSb = new StringBuffer();
			boolean first = true;
			for(Entry<String, String> cookie : cookies.entrySet()){
				if(first){
					first =false;
				}else{
					cookieSb.append("; ");
				}
				cookieSb.append(cookie.getKey()).append("=").append(cookie.getValue());
			}
			con.setRequestProperty("Cookie", cookieSb.toString());
		}
	}
	private Response toResult(HttpURLConnection con) throws IOException{
		if(HttpURLConnection.HTTP_OK == con.getResponseCode()){
			// success
		}else if(con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || con.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER){
			return get(con.getHeaderField("Location"));
			
		}else
			throw new IOException("连接出现问题，status code is:"+con.getResponseCode());
		
		Response res = new Response();
		res.setup(con);
		Map<String,List<String>> headerFields = con.getHeaderFields();
		for(Entry<String, List<String>> h : headerFields.entrySet()){
			if(h.getKey() == null) continue;
			else{
				if(h.getKey().equalsIgnoreCase("Set-Cookie")){
					for(String cookie: h.getValue() ){
						String key = cookie.substring(0,cookie.indexOf("="));
						String value = cookie.substring(cookie.indexOf("=")+1);
						if(value.indexOf(";")!= -1){
							value = value.substring(0,value.indexOf(";"));
						}
						cookie(key, value);
					}
				}else{
					if(!h.getValue().isEmpty()){
						res.header(h.getKey(), h.getValue().get(0));
					}
				}
			}
		}
		InputStream is = null;
		try{
			if(res.hasHeader("Content-Encoding") && res.header("Content-Encoding").equalsIgnoreCase("gzip")){
				is = new BufferedInputStream(new GZIPInputStream(con.getInputStream()));
			}else{
				is = new BufferedInputStream(con.getInputStream());
			}
			res.data(readToByteBuffer(is));
			res.charset(getCharsetFromContentType(res.contentType()));
		}catch(IOException e){
			if(is != null) is.close();
		}
		return res;
	}
}
