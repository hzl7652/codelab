package companyTask;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import browse.Browse;

public class BeijingAir {

	public static void main(String[] args) throws IOException {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				JFrame jf = new BAFrame("北京空气质量曲线图");
//				jf.setVisible(true);
//			}
//		});
		
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//		Accept-Charset:GBK,utf-8;q=0.7,*;q=0.3
//		Accept-Encoding:gzip,deflate,sdch
//		Accept-Language:zh-CN,zh;q=0.8
//		Cache-Control:no-cache
//		Connection:keep-alive
//		Cookie:guest_id=v1%3A131114138334175687; secure_session=true; twll=l%3D1320714645; pid=v1%3A132124869257028941033; k=10.34.231.112.1321583147175732; twid=u%3D257541498%7Cbm%2Bz%2Bu7zatqWBnA03zZ19XkPpBA%3D; __utma=43838368.434159395.1307676636.1321860042.1321924597.23; __utmc=43838368; __utmz=43838368.1307676636.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)
//		Host:search.twitter.com
//		Pragma:no-cache
//		User-Agent:Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.106 Safari/535.2
		
		Browse br = new Browse();
		br.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		br.header("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		br.header("Accept-Encoding", "gzip,deflate,sdch");
		br.header("Cache-Control", "no-cache");
		br.header("Connection", "keep-alive");
		br.header("Host", "search.twitter.com");
		br.header("Pragma", "no-cache");
		br.useragent(Browse.USERAGENT_CHROME);
		br.proxy(new Proxy(Type.SOCKS, InetSocketAddress.createUnresolved("192.168.242.124", 1080)));
		try {
			String content = br.get("http://search.twitter.com/search.json?from=BeijingAir").body();
			System.out.println(content);
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
	}
}

class BAFrame extends JFrame {
	public static final String url = "http://search.twitter.com/search.json?from=BeijingAir";
	private static final long serialVersionUID = -7758679617818385968L;
	JTextArea ja ;
	public BAFrame(String title){
		super(title);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		JButton jb = new JButton("刷新");
		jb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Browse br = new Browse();
						br.useragent(Browse.USERAGENT_CHROME);
						br.proxy(new Proxy(Type.SOCKS, InetSocketAddress.createUnresolved("192.168.242.124", 1080)));
						try {
							System.out.println("clicked");
							String content = br.get(url).body();
							System.out.println(content);
							ja.setText(content);
						} catch (IOException e1) {
							ja.append("\n"+e1.getMessage());
						}
						
					}
				});
			}
		});
		add(jb);
		ja = new JTextArea();
		ja.setEditable(false);
		ja.setAutoscrolls(true);
		ja.setRows(20);
		ja.setColumns(50);
		add(ja);
	}
}
