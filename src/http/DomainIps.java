package http;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import browse.Browse;
import browse.KeyVal;

/**
 * 获取域名下的ip地址
 * @author 王成
 *
 */
public class DomainIps {
	
	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		if(sc.hasNextLine()){
			Set<String>  ips = new HashSet<String>();
			int ipsSize = ips.size();
			int repeat = 0;
			int error = 0;
			String domain = sc.nextLine();
			while(true){
				try {
					ips.addAll(getIps4domain(domain));
					error = 0;
					if(ipsSize == ips.size()){
						repeat += 1;
					}else{
						repeat = 0;
						ipsSize = ips.size();
					}
					if(repeat >10){
						break;
					}
				} catch (IOException e) {
					error += 1;
					if(error >5){
						break;
					}
					e.printStackTrace();
				}
			}
			for(String ip:ips){
				System.out.println(ip);
				try {
					Process p = Runtime.getRuntime().exec("route ADD "+ip+" MASK 255.255.255.255 192.168.0.1");
					p.waitFor();
					p.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static final String domainUrl = "http://www.ip866.com/DNSanalysis.aspx/AjaxQueryIp";
	public static Set<String> getIps4domain(final String domain) throws IOException{
		Set<String> result = new HashSet<String>();
		Browse b = new Browse();
		b.useragent(Browse.USERAGENT_CHROME);
		Collection<KeyVal> params = new ArrayList<KeyVal>();
		b.post("http://www.ip866.com", params);
		b.header("Host", "www.ip866.com");
		b.header("Accept", "*/*");
		b.header("Connection", "keep-alive");
		b.header("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		b.header("Accept-Encoding", "gzip,deflate,sdch");
		b.header("Accept-Language", "zh-CN,zh;q=0.8");
		b.header("Content-Type", "application/json; charset=UTF-8");
		b.header("Origin", "http://www.ip866.com");
		b.header("Referer", "http://www.ip866.com/DNSanalysis.aspx?input="+domain);
	//	b.cookie("cck_lasttime","1334125113776");
	//	b.cookie("cck_count","0");
		params.add(KeyVal.createKV("ipInputText", domain));
		params.add(KeyVal.createKV("ddDnsType", "1"));
		params.add(KeyVal.createKV("ddDNSServer", "202.106.196.115"));
		params.add(KeyVal.createKV("customDnsServer", ""));
		String body = b.postJson(domainUrl,params ).body();
		Pattern p = Pattern.compile(":(\\d+\\.\\d+\\.\\d+\\.\\d+)");
		Matcher m = p.matcher(body);
		while(m.find()){
			result.add(m.group(1));
		}
		return result;
	}
}
