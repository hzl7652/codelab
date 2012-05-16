package companyTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import browse.Browse;


public class FatchPhotos {
	static String dir = "gaoqingbizhi-1680x1050/";
//	static ExecutorService es = Executors.newSingleThreadExecutor();
	static ExecutorService es = Executors.newFixedThreadPool(10);
	public static void main(String[] args) {
		new File(dir).mkdirs();
		for(int i=0;i<38;i++){
//			es.submit(new FetchPage(i+1));
			es.submit(new FetchPageBySite(i+1, "/desktop/1680x1050/special-"+(i+1)+".html"));
		}
		es.shutdown();
	}
	
}
class FetchPage implements Runnable {
	
	int page = 0;
	public FetchPage(int page){
		this.page = page;
	}
	@Override
	public void run() {
		Browse ber = new Browse();
		ber.setHost("http://www.gaoqingbizhi.com");
		ber.useragent("AutoIt");
		
		try {
			Document doc = Jsoup.parse(ber.get("/websoft/Random.asp?do=newbmp&page="+page).body());
			Elements photos = doc.getElementsByAttributeValue("class", "photo");
			for(int i=0 ;i<photos.size();i++){
				Element e = photos.get(i);
				Element img = e.getElementsByTag("img").get(0);
				ber.get(img.attr("src")).toFile(FatchPhotos.dir+"gaoqingbizhi-"+(page*12 -12 +i)+".jpg");
			}
			System.out.println("page::"+page+":: 抓取完毕。");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

class FetchPageBySite implements Runnable {
	int page =0;
	String url = "";
	public FetchPageBySite(int page,String url){
		this.page = page;
		this.url = url;
	}
	public void run(){
		Browse ber = new Browse();
		ber.setHost("http://www.gaoqingbizhi.com");
		ber.useragent(Browse.USERAGENT_CHROME);
		Document doc = null;
		try {
			doc = Jsoup.parse(ber.get(url).body());
		} catch (IOException e) {
			return ;
		}
			Elements photos = doc.getElementsByTag("dl");
			for(int i=0 ;i<photos.size();i++){
				Element e = photos.get(i);
				try {
					String imagePage = e.getElementsByTag("a").get(0).attr("href");
					Document ddd = Jsoup.parse(ber.get(imagePage).body());
					ber.header("Referer", ber.getHost()+imagePage);
					ber.get(ddd.getElementsByAttributeValue("width", "718").get(0).attr("src")).toFile(FatchPhotos.dir+"gaoqingbizhi-"+(page*30 -30 +1+i)+".jpg");
				} catch (IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
			System.out.println("page::"+page+":: 抓取完毕。");
	}
}
