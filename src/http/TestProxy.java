package http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import browse.Browse;

public class TestProxy {
	
	public static String myIp = "123.122.124.203";
	
	public static void  main(String[] args) {
			System.out.println(test("64.213.163.23",80));
	}
	
	public static boolean test(String ip,int port) {
		try{
			Proxy proxy = new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(ip, port));
			Browse browser = new Browse();
			browser.proxy(proxy);
			String outIp = MyIp.getOutIp(browser);
			System.out.println(outIp);
			return myIp.equals(outIp);
		}catch(IOException e){
			return false;
		}
	}
	
}
