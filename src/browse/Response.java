package browse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Response {
	
	private URL url;
	private int statusCode ;
	private String charset;
	private String statusMessage;
	private String contentType;
	private ByteBuffer data;
	private Map<String,String> headers = new HashMap<String,String> ();
	public  Response setup(HttpURLConnection con) throws IOException{
		statusCode = con.getResponseCode();
		statusMessage = con.getResponseMessage();
		contentType = con.getContentType();
		url = con.getURL();
		return this;
	}
	public URL url(){
		return url;
	}
	public int statusCode(){
		return statusCode;
	}
	public String charset(){
		return charset;
	}
	public Response charset(String charset){
		this.charset = charset;
		return this;
	}
	public byte[] data(){
		return data.array();
	}
	public Response data(ByteBuffer data){
		this.data = data;
		return this;
	}
	public Map<String,String> headers(){
		return headers;
	}
	public String header(String key){
		return headers.get(key);
	}
	public Response header(String key,String value){
		headers.put(key, value);
		return this;
	}
	public boolean hasHeader(String key){
		return header(key) != null;
	}
	public String statusMessage(){
		return statusMessage;
	}
	public String contentType(){
		return contentType;
	}
	public String body(){
		String body = null;
		if(data != null){
			if(charset != null){
				body = Charset.forName(charset).decode(data).toString();
			}else{
				body = Charset.forName(Browse.DEFAULT_CHARSET).decode(data).toString();
			}
			data.rewind();
		}
		return body;
	}
	
	public File toFile(String fileName) throws IOException{
		File file = null;
		if(data != null){
			file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data.array());
			fos.flush();
			fos.close();
			data.rewind();
		}
		return file;
	}
}