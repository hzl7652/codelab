package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class ProxyServer {

	public static Logger log = Logger.getLogger(ProxyServer.class.getName());
	
	public static void main(String[] args) {
		
		System.out.println("server starting ...");
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(8899);
			while(true){
				try {
					Socket s = ss.accept();
					new ProxyServer().new SocketThread(s).start();
				} catch (IOException e) {
					log.info("accept the socket error");
				}
			}
		} catch (IOException e) {
			log.warning("init error");
		}
		System.out.println("server stop");
	}
	class SocketThread extends Thread {
		Socket s = null;
		public SocketThread(Socket s){
			this.s = s;
		}
		@Override
		public void run() {
			try{
				InputStream is = s.getInputStream();
				
				Socket so = null;
				PrintWriter pw = null;
				Scanner sc = new Scanner(is);
				
				boolean first = true;
				while(sc.hasNextLine()){
					String line = sc.nextLine();
//					if(first){
//						first = false;
//						String address = line.substring(line.indexOf(" ")+1, line.lastIndexOf(" "));
//						URL url = new URL(address);
//						String host = url.getHost();
//						InetAddress ia = Inet4Address.getByName(host);
//						if(ia == null) 
//							throw new IOException("can't resolve the dns");
//						int port = url.getPort()==-1?80:url.getPort();
//						so = new Socket(ia,port);
//						pw = new PrintWriter(so.getOutputStream(),true);
//						System.out.println(ia.getHostAddress()+":"+port);
//					}
//					pw.write(line+"\r\n");
					System.out.println(line);
				}
				sc.close();
//				pw.close();
				
//				sc = new Scanner(so.getInputStream());
//				pw = new PrintWriter(s.getOutputStream());
//				while(sc.hasNextLine()){
//					String line = sc.nextLine();
//					pw.write(line+"\r\n");
//					System.out.println(line);
//				}
//				sc.close();
//				pw.close();
			}catch (Exception e) {
				System.out.println(Thread.currentThread().getName()+": inputstream read error--"+e.getMessage());
			}
		}
	}
}
