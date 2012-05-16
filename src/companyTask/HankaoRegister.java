package companyTask;

import http.Orc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.random.StringGenerator;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import util.Infoc;
import browse.Browse;
import browse.KeyVal;

public class HankaoRegister  implements Runnable {
	
	public static Queue<String> users = new LinkedBlockingQueue<String>();
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(10);
		for(int i=0;i<10000;i++){
			es.submit(new HankaoRegister());
		}
		es.shutdown();
	}
	
	private String username;
	private String idCard ;
	private String email;
	private String address;
	private String postcode;
	private boolean regist = false;
	private static Log log = Logs.get();
	private Browse browser = null;
	@Override
	public void run() {
		try{
		init();
		while(!regist)
			regist();
		registFull();
		uploadPhoto();
		apply();
		}catch(Exception e){
			log.error(this.toString()+" 错误原因："+e.getMessage()+"");
		}
	}
	
	public void init() throws Exception{
		try{
			browser = new Browse();
			browser.setHost("http://127.0.0.1:8080/applysys");
			username = Infoc.getName();
			idCard = Infoc.getIdCard();
			email = Infoc.getEmail();
			address = Infoc.getAddress();
			postcode = Infoc.getPostcode();
			browser.get("/");
		}catch(Exception e){
			throw new Exception("init():"+e.getMessage());
		}
	}
	public void regist() throws Exception{
		String path = null;
		try {
			path = browser.get("/common/verifycode/regVerifycode").toFile(new StringGenerator(12,12).next()+".jpg").getCanonicalPath();
			String code = Orc.picToCode(path);
			if(Strings.isEmpty(code)) {
				log.debug(new File(path).delete()+":"+path+" delete");
				log.debug(new File(path.substring(0,path.lastIndexOf("."))+".txt").delete()+":"+path.substring(0,path.lastIndexOf("."))+".txt"+" delete");
				log.debug("验证码没有破解成功");
				return;
			}
			Collection<KeyVal> params= new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("realname", username));
			params.add(KeyVal.createKV("password", "000000"));
			params.add(KeyVal.createKV("passwordRe", "000000"));
			params.add(KeyVal.createKV("idType", "1"));
			params.add(KeyVal.createKV("idCard", idCard));
			params.add(KeyVal.createKV("protect", "您的小学校名是?"));
			params.add(KeyVal.createKV("answer", "0000"));
			params.add(KeyVal.createKV("email", email));
			params.add(KeyVal.createKV("verifycode", code));
			String content = browser.post("/memapp/memberRegDo", params).body();
			if(content.indexOf("验证码不正确，请重新填写！") == -1){
				log.info(toString()+"注册成功");
				regist = true;
			}else{
				log.debug("验证码不正确");
			}
			log.debug(new File(path).delete()+":"+path+" delete");
			log.debug(new File(path.substring(0,path.lastIndexOf("."))+".txt").delete()+":"+path.substring(0,path.lastIndexOf("."))+".txt"+" delete");
		} catch (Exception e) {
			if(path != null){
				log.debug(new File(path).delete()+":"+path+" delete");
				log.debug(new File(path.substring(0,path.lastIndexOf("."))+".txt").delete()+":"+path.substring(0,path.lastIndexOf("."))+".txt"+" delete");
			}
			throw new Exception("regist():"+e.getMessage());
		}
		
	}
	public void registFull() throws Exception{
		try{
			String content = browser.get("/memapp/memberRegFull").body();
			Document doc = Jsoup.parse(content);
			String memId = doc.getElementsByAttributeValue("name", "memId").get(0).val();
			Collection<KeyVal>  params = new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("memId", memId));
			params.add(KeyVal.createKV("flag", ""));
			params.add(KeyVal.createKV("sex", (Integer.valueOf(idCard.substring(16,17))%2==0?"2":"1")));
			params.add(KeyVal.createKV("nation", "01"));  
			params.add(KeyVal.createKV("birth", "struct"));
			params.add(KeyVal.createKV("birth_year", idCard.substring(6,10)));
			params.add(KeyVal.createKV("birth_month", ""+Integer.valueOf(idCard.substring(10,12))));
			params.add(KeyVal.createKV("birth_year", ""+Integer.valueOf(idCard.substring(12,14))));
			params.add(KeyVal.createKV("province.id", "1")); 
			params.add(KeyVal.createKV("city.id", "1")); 
			params.add(KeyVal.createKV("eduLevel", "1")); 
			params.add(KeyVal.createKV("eduDegree", "1")); 
			params.add(KeyVal.createKV("career", "1")); 
			params.add(KeyVal.createKV("biz", "1")); 
			params.add(KeyVal.createKV("unit", "")); 
			params.add(KeyVal.createKV("mbphone", "13784380988")); 
			params.add(KeyVal.createKV("postcode", postcode));
			params.add(KeyVal.createKV("address", address));
			
			browser.postMulti("/memapp/memberRegFullDo", params);
		}catch(Exception e){
			log.info(e);
			throw new Exception("registFull:"+e.getMessage());
		}
	}
	public void uploadPhoto() throws Exception{
		try{
			//imageSrc()
			File photo = new File( "D:\\java-work\\InfoCreator\\src\\android_logo1.jpg");
			File temp = new File(new Random().nextInt(100)+"android_logo1.jpg");
			Files.copyFile(photo,temp);
			String content = browser.postMulti("/memapp/imgsrc",null,KeyVal.createKV("image", temp.getCanonicalPath())).body();
			log.debug(temp.delete()+temp.getCanonicalPath()+" delete");
			//imageCorp()
			//x1=5&y1=13&w=180&h=240&src=path&format=jpg
			Document doc = Jsoup.parse(content);
			String path = doc.getElementById("src").val();
			Collection<KeyVal>  params = new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("x1","5"));
			params.add(KeyVal.createKV("y1","13"));
			params.add(KeyVal.createKV("w","180"));
			params.add(KeyVal.createKV("h","240"));
			params.add(KeyVal.createKV("src",path));
			params.add(KeyVal.createKV("format","jpg"));
			browser.post("/memapp/img",params);
		}catch(Exception e){
			log.info(e);
			throw new Exception("uploadPhoto:"+e.getMessage());
		}
	}
	public void apply() throws Exception{
		try{
			// select orginfo
			String content = browser.get("/memapp/spotLeaveQuery").body();
			Document doc = Jsoup.parse(content);
			Element provinceSelect = doc.getElementById("province.id");
			String prohtml = provinceSelect.html();
			List<String> proOption = select(prohtml,"");
			if(proOption.size()==0) throw new Exception("没有省份可选");
			String proCode = proOption.get(new Random().nextInt(proOption.size()));
			String citySelect = browser.get("/common/examRangeAjax?provinceId="+proCode).body();
			List<String> cityOption = toList(citySelect,"");
			if(cityOption.size()==0) throw new Exception("没有找到省份下的考区");
			String cityCode = cityOption.get(new Random().nextInt(cityOption.size()));
			// then apply
			Collection<KeyVal> params  = new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("province.id",proCode));
			params.add(KeyVal.createKV("city.id",cityCode));
			
			doc = Jsoup.parse(browser.post("/memapp/spotLeaveQueryResult",params).body());
			Elements courses = doc.getElementsByTag("a");
			if(courses.size() == 0){
				throw new IOException("没有可用的考点，散了吧");
			}
			Element course = courses.get(R.random(0, courses.size()-1));
			String end = course.attr("href").substring(course.attr("href").lastIndexOf("/")+1);
			doc = Jsoup.parse(browser.get("/memapp/"+end).body());
			courses = doc.getElementsByAttributeValue("name", "examCoursePlanId");
			if(courses.size() == 0)  throw new IOException("没有可用的考试");
			String examCoursePlanId = courses.get(R.random(0, courses.size()-1)).val();
			String memberinfoId = doc.getElementsByAttributeValue("name", "memberinfo.id").get(0).val();
			String orginfoId = doc.getElementsByAttributeValue("name", "orginfo.id").get(0).val();
			params.clear();
			params.add(KeyVal.createKV("memberinfo.id", memberinfoId));
			params.add(KeyVal.createKV("orginfo.id", orginfoId));
			params.add(KeyVal.createKV("examCoursePlan.id", examCoursePlanId));
			params.add(KeyVal.createKV("radioValue", examCoursePlanId));
			params.add(KeyVal.createKV("examCoursePlanId", examCoursePlanId));
			browser.post("/memapp/applyExamCourse", params);
			
		}catch(Exception e){
			throw new Exception("apply:"+e.getMessage());
		}
	}
	public int login()throws Exception{
		return 200;
	}
	public String toString(){
		return new StringBuffer(username).append(":").append(idCard).toString();
	}
	public List<String> select(String html,String empty){
		html = html.trim();
		String[] options = html.split("</option>");
		List<String> results = new ArrayList<String>();
		for(String o : options){
			//String value = o.substring(o.indexOf(">")+1);
			int start = o.indexOf("value=\"")+7;
			String key = o.substring(start,o.indexOf("\"",start));
			if(!key.equals(empty)){
				results.add(key);
			}
		}
		return results;
	}
	public List<String> toList(String content,String empty){
		content = content.trim();
		String[] options = content.split(",");
		List<String> results = new ArrayList<String>();
		for(String o: options){
		//	String value = o.substring(o.indexOf(":")+1);
			String key = o.substring(0,o.indexOf(":"));
			if(!key.equals(empty)){
				results.add(key);
			}
		}
		return results;
	}
	public File copyFile(File src,String name) throws IOException{
		FileOutputStream fos = new FileOutputStream(name);
		FileInputStream fis = new FileInputStream(src);
		byte[] buffer = new byte[0x20000];
		int len = 0;
		while( (len = fis.read(buffer) )!=  -1){
			fos.write(buffer, 0, len);
		}
		fos.flush();
		fos.close();
		fis.close();
		return new File(name);
	}
}


