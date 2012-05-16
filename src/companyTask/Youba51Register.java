package companyTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.el.El;
import org.nutz.lang.random.StringGenerator;

import browse.Browse;
import browse.KeyVal;
import browse.Response;

import util.Infoc;

public class Youba51Register {
	public static void main(String[] args) {
		new YoubaUser().run();
	}
}

class YoubaUser implements Runnable {

	private String username ;
	private String email ;
	private String registUrl = "/register.php";
	private String verifyCodeUrl = "/ajax.php?action=updatesecqaa&inajax=1&ajaxtarget=secanswer_menu_content";
	private StringGenerator sg = new StringGenerator(8,12);
	private String host = "http://51youba.com" ;
	private Browse browser = new Browse();
//	ScriptEngine se = new ScriptEngineManager().getEngineByName("js");
//	private String refer = "51youba.com/?fromuid=1058416";
	public void run() {
			browser.useragent(Browse.USERAGENT_CHROME);
			String[] commonds = new String[]{
//					"netsh interface ip set address \"本地连接 2\" static 192.168.0.122 255.255.255.0 192.168.0.2 1",
//					"netsh interface ip set address \"本地连接 2\" static 192.168.242.122 255.255.255.0 192.168.242.254 1",	
//					"netsh interface ip set address \"本地连接 2\" static 192.168.0.122 255.255.255.0 192.168.0.1 1"	
					//"proxy 127.0.0.1 1080"
					"arp -a"
			};
			for(String com : commonds){
					if(com.startsWith("proxy")){
						String[] split = com.split(" ");
						if(split.length != 3)
							continue;
						browser.proxy(new Proxy(Type.SOCKS,InetSocketAddress.createUnresolved(split[1], Integer.valueOf(split[2]))));
					}else{
						browser.proxy(null);
					}
					try {
						init();
						while(regist()){}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(! com.startsWith("proxy")){
						try{
						Process p = Runtime.getRuntime().exec(com);
								p.waitFor();
							p.destroy();
						}catch(IOException e){
							e.printStackTrace();
						}catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			}
	}
	void init(){
		browser.setHost(host);
		username = sg.next();
		email = Infoc.getEmail();
	}
	boolean regist() throws IOException{
		Response res  = browser.get(registUrl);
		res.charset("GBK");
		Document doc = Jsoup.parse(res.body());
		Elements es = doc.getElementsByAttributeValue("name", "formhash");
		String formhash = null;
		if(es.size()>0){
			formhash = es.get(0).val();
		}
		
		System.out.println(formhash);
		String content = browser.get(verifyCodeUrl).body() ;
		String question = content.substring(content.indexOf("[CDATA[")+7,content.indexOf("]")-1);
		System.out.println(question);
		int answer = Double.valueOf(El.eval(question).toString()).intValue();
		System.out.println(answer);
		Collection<KeyVal> params = new ArrayList<KeyVal>();
		if(formhash != null)
		params.add(KeyVal.createKV("formhash", formhash));
		params.add(KeyVal.createKV("referer", ""));
		params.add(KeyVal.createKV("activationauth", ""));
		params.add(KeyVal.createKV("fromuser", "feiyan35488"));
		params.add(KeyVal.createKV("username", username));
		params.add(KeyVal.createKV("password", "354888"));
		params.add(KeyVal.createKV("password2", "354888"));
		params.add(KeyVal.createKV("email", email));
		params.add(KeyVal.createKV("secanswer", answer+""));
		res = browser.post("/register.php?regsubmit=yes&inajax=1", params);
		res.charset("GBK");
		System.out.println(res.body());
		if(res.body().indexOf("IP 地址在 24 小时内无法注册") == -1 ){
			if(res.body().indexOf("感谢您注册") == -1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
}
