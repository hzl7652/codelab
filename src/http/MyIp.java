package http;

import java.io.IOException;

import browse.Browse;
import browse.Response;

public class MyIp {
	public static String getOutIp(Browse browser) throws IOException{
		browser.useragent(Browse.USERAGENT_CHROME);
		Response res = browser.get("http://www.ip138.com/ip2city.asp");
		res.charset("gb2312");
		String content = res.body();
		return content.substring(content.indexOf("[")+1,content.indexOf("]"));
	}
	
	public static String getOutIp() throws IOException{
		Browse browser = new Browse();
		browser.useragent(Browse.USERAGENT_CHROME);
		Response res = browser.get("http://www.ip138.com/ip2city.asp");
		res.charset("gb2312");
		String content = res.body();
		return content.substring(content.indexOf("[")+1,content.indexOf("]"));
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getOutIp());;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
