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
import org.nutz.lang.Strings;
import org.nutz.lang.random.StringGenerator;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import util.Infoc;
import browse.Browse;
import browse.KeyVal;

public class TestVirsualUser2 {
	public static Queue<String> users = new LinkedBlockingQueue<String>();
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(10);
		for(int i=0;i<100;i++){
			es.submit(new VUser1());
		}
		es.shutdown();
	}
}

class VUser1 implements Runnable {
	
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
		}catch(Exception e){
			log.error(this.toString()+" 错误原因："+e.getMessage()+"");
		}
		regist = false;
		while(!regist){
			try{
				regist = regist();
			}catch(Exception e){
				log.error(this.toString()+" 错误原因："+e.getMessage()+"");
				regist = false;
			}
		}
		regist = false;
		while(!regist){
			try{
				regist = registFull();
			}catch(Exception e){
				log.error(this.toString()+" 错误原因："+e.getMessage()+"");
				regist = false;
			}
		}
		regist = false;
		while(!regist){
			try{
				regist = uploadPhoto();
			}catch(Exception e){
				log.error(this.toString()+" 错误原因："+e.getMessage()+"");
				regist = false;
			}
		}
		regist = false;
		while(!regist){
			try{
				regist = apply();
			}catch(Exception e){
				log.error(this.toString()+" 错误原因："+e.getMessage()+"");
				regist = false;
			}
		}
		log.info(this.toString()+"  注册成功");
	}
	
	public void init() throws Exception{
		try{
			browser = new Browse();
			browser.setHost("http://localhost:9999/apply");
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
	public boolean regist() throws Exception{
		String path = null;
		try {
			path = browser.get("/common/verifycode/regVerifycode").toFile(new StringGenerator(12,12).next()+".jpg").getCanonicalPath();
			String code = Orc.picToCode(path);
			if(Strings.isEmpty(code)) {
				log.debug(new File(path).delete()+":"+path+" delete");
				log.debug(new File(path.substring(0,path.lastIndexOf("."))+".txt").delete()+":"+path.substring(0,path.lastIndexOf("."))+".txt"+" delete");
				log.debug("验证码没有破解成功");
				return false;
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
				TestVirsualUser2.users.offer(toString());
				log.debug(toString()+"注册成功,当前人数："+TestVirsualUser2.users.size());
				return true;
			}else{
				log.debug("验证码不正确");
			}
			return false;
		} catch (Exception e) {
			if(path != null){
				log.debug(new File(path).delete()+":"+path+" delete");
				log.debug(new File(path.substring(0,path.lastIndexOf("."))+".txt").delete()+":"+path.substring(0,path.lastIndexOf("."))+".txt"+" delete");
			}
			throw new Exception("regist():"+e.getMessage());
		}
		
		
	}
	public boolean registFull() throws Exception{
		try{
			browser.get("/memapp/doApplyConfirm");
			String content = browser.get("/memapp/memberRegFull").body();
			Document doc = Jsoup.parse(content);
			String memId = doc.getElementsByAttributeValue("name", "memId").get(0).val();
			Element examType = doc.getElementById("examType");
			List<String> examTypeOptions = select(examType.html(), "");
			Collection<KeyVal>  params = new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("memId", memId));
			params.add(KeyVal.createKV("flag", ""));
			params.add(KeyVal.createKV("isStudent", "1"));
			params.add(KeyVal.createKV("sex", ""+(Integer.valueOf(idCard.substring(16,17))%2==0?2:1)));
			params.add(KeyVal.createKV("issf", ""+1));
			params.add(KeyVal.createKV("idType", ""+1));
			params.add(KeyVal.createKV("sexRd", ""+2));
			params.add(KeyVal.createKV("nation", "01"));   // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("partisan", ""+1));// TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("birth", "struct"));
			params.add(KeyVal.createKV("birth_year", idCard.substring(6,10)));
			params.add(KeyVal.createKV("birth_month", ""+Integer.valueOf(idCard.substring(10,12))));
			params.add(KeyVal.createKV("birth_year", ""+Integer.valueOf(idCard.substring(12,14))));
			params.add(KeyVal.createKV("hometown.id", ""+1));// TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("examType",examTypeOptions.get(new Random().nextInt(examTypeOptions.size())))); 
			params.add(KeyVal.createKV("issfRd", ""+1));
			params.add(KeyVal.createKV("school", "北京大学")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("studRd", ""+1));
			params.add(KeyVal.createKV("learningStyle", ""+1)); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("dept", "软件工程学院计算机专业")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("eduLevel", "")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("educationCode", "")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("eduDegree", "")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("degreeCode", "")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("workYear", "")); // TODO SHOULD BE BORTH_YEAR + 25
			params.add(KeyVal.createKV("email", email));
			params.add(KeyVal.createKV("telephone", ""));  // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("mbphone", "13784380988")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("postcode", postcode));
			params.add(KeyVal.createKV("address", address));
			params.add(KeyVal.createKV("keys", "1-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "21-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "22-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "3-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "41-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "42-150")); // TODO SHOULD BY RANDOM
			params.add(KeyVal.createKV("keys", "5-150")); // TODO SHOULD BY RANDOM
			
			browser.postMulti("/memapp/memberRegFullDo", params);
			return true;
		}catch(Exception e){
			log.info(e);
			throw new Exception("registFull:"+e.getMessage());
		}
	}
	public boolean uploadPhoto() throws Exception{
		try{
			//imageSrc()
			File photo = new File( "D:\\java-work\\InfoCreator\\src\\android_logo1.jpg");
			
			File temp = copyFile(photo,new Random().nextInt(100)+"android_logo1.jpg");
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
			return true;
		}catch(Exception e){
			log.info(e);
			throw new Exception("uploadPhoto:"+e.getMessage());
		}
	}
	public boolean apply() throws Exception{
		try{
			// select orginfo
			String content = browser.get("/memapp/applyExamPlan").body();
			Document doc = Jsoup.parse(content);
			Element provinceSelect = doc.getElementById("province.id");
			String prohtml = provinceSelect.html();
			List<String> proOption = select(prohtml,"000");
			if(proOption.size()==0) throw new Exception("没有省份可选");
			String proCode = proOption.get(new Random().nextInt(proOption.size()));
			String citySelect = browser.get("/common/examRangeAjax?provinceId="+proCode).body();
			List<String> cityOption = toList(citySelect,"");
			if(cityOption.size()==0) throw new Exception("没有找到省份下的考区");
			String cityCode = cityOption.get(new Random().nextInt(cityOption.size()));
			// select examCourse
			List<String> checkPlanIds = new ArrayList<String>();
			List<String> radioPlanIds = new ArrayList<String>();
			Elements elements = doc.getElementsByAttributeValue("name", "planId");
			Elements elements2 = doc.getElementsByAttributeValue("name", "planId2");
			if(elements.size() == 0 && elements2.size() == 0) throw new Exception("没有考试科目，可用来报名");
			for(int i=0;i<elements.size();i++){
				Element e = elements.get(i);
				checkPlanIds.add(e.val().trim());
			}
			for(int i=0;i<elements2.size();i++){
				Element e = elements2.get(i);
				radioPlanIds.add(e.val().trim());
			}
			// then apply
			//planIds=22&province.id=1&city.id=136&planId=22
			Collection<KeyVal> params  = new ArrayList<KeyVal>();
			params.add(KeyVal.createKV("province.id",proCode));
			params.add(KeyVal.createKV("city.id",cityCode));
			StringBuffer sb = new StringBuffer();
			if(checkPlanIds.size()> 0){
				for(int i=0;i<checkPlanIds.size();i++){
					if(i!=0){
						sb.append(",");
					}
					sb.append(checkPlanIds.get(i));
				}
			}
			if(radioPlanIds.size()>0){
				String planId = radioPlanIds.get(new Random().nextInt(radioPlanIds.size()));
				if(sb.length()>0)
					sb.append(",");
				sb.append(planId);
			}
			params.add(KeyVal.createKV("planIds",sb.toString()));
			params.add(KeyVal.createKV("myExamType",doc.getElementsByAttributeValue("name", "myExamType").get(0).val()));
			browser.post("/memapp/applyExamDo",params);
			return true;
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


