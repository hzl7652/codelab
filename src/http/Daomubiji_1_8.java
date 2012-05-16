package http;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

import browse.Browse;

public class Daomubiji_1_8 {

	static Browse b = new Browse();
	
	public static void main(String[] args) throws IOException {
		b.useragent(Browse.USERAGENT_CHROME);
		b.setHost("http://www.daomubiji.com");
		
		Document doc = Jsoup.parse(b.get("/").body());
		
		Elements divs = doc.getElementsByAttributeValue("class", "bg");
		if(divs.size()>2){
			for(int i=1;i<divs.size()-1;i++){ // 盗墓 1-8
				String title = divs.get(i).getElementsByTag("h2").html();
				System.out.println(title);
				Elements links = divs.get(i).getElementsByTag("a");
				StringBuffer sb = new StringBuffer();
				for(int j=0;j<links.size();j++){ // 盗墓 各章
					String subtitle = links.get(j).html();
					String sublink = links.get(j).attr("href");
					if(!Strings.isEmpty(subtitle)){
						sb.append(subtitle+"\n");
						Document content = Jsoup.parse(b.get(sublink).body());
						Elements conDiv = content.getElementsByClass("content");
						for(int k=3;k<conDiv.get(0).children().size();k++){ // 章中 各块
							if(conDiv.get(0).child(k).tagName().equals("p")){
								sb.append(conDiv.get(0).child(k).html()+"\n");
							}else{
								break;
							}
						}
					}
				}
				Files.write(new File(title+".txt"), sb);
			}
		}else{
			System.out.println("没有找到合适的内容");
		}
		
		
	}
	
	
}
