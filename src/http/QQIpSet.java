package http;

import java.io.IOException;

public class QQIpSet {
	static String qqips = "183.60.48.145," +
			"183.60.48.165,119.147.32.232,112.95.240.53,112.90.84.112,112.90.84.119,112.95.240.65," +
			"119.147.32.226,119.147.45.211,125.39.205.39,183.60.48.152,183.60.50.25,112.90.84.51,112.95.240.11,119.147.45.223,125.39.205.44";
	
	public static void main(String[] args) throws IOException{
		String[] ips = qqips.split(",");
		for(String ip : ips){
			
			Runtime.getRuntime().exec("route delete "+ip);
			Runtime.getRuntime().exec("route add "+ip+" mask 255.255.255.255 192.168.0.1");
		}
	}
}

