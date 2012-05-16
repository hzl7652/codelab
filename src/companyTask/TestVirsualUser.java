package companyTask;

import http.Orc;
import http.Params;
import http.UserTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.lang.Strings;

import util.Infoc;

public class TestVirsualUser {
	public static Queue<String> users = new LinkedBlockingQueue<String>();
	public static void main(String[] args) {
//		ExecutorService es = Executors.newFixedThreadPool(10);
//		for(int i=0;i<10;i++){
//			es.submit(new VUser());
//		}
//		es.shutdown();
		for(int i=0;i<10;i++)
			new VUser().run();
	}
}

class VUser extends UserTask {
	
	private String username;
	private String idCard ;
	private String email;
	private String address;
	private String postcode;
	private boolean regist = false;
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
			setHost("http://localhost:8888/apply");
			username = Infoc.getName();
			idCard = Infoc.getIdCard();
			email = Infoc.getEmail();
			address = Infoc.getAddress();
			postcode = Infoc.getPostcode();
			get("/");
		}catch(Exception e){
			throw new Exception("init():"+e.getMessage());
		}
	}
	public void regist() throws Exception{
		String path = null;
		try {
			path = get("/common/verifycode/regVerifycode");
			String code = Orc.picToCode(path);
			if(Strings.isEmpty(code)) {
				new File(path).delete();
				new File(path.substring(0,path.lastIndexOf("."))+".txt").delete();
				log.debug("验证码没有破解成功");
				return;
			}
			Params<String, String> params = new Params<String, String>();
			params.put("realname", username);
			params.put("password", "000000");
			params.put("passwordRe", "000000");
			params.put("idType", "1");
			params.put("idCard", idCard);
			params.put("protect", "您的小学校名是?");
			params.put("answer", "0000");
			params.put("email", email);
			params.put("verifycode", code);
			String content = post("/memapp/memberRegDo", params);
			if(content.indexOf("验证码不正确，请重新填写！") == -1){
				TestVirsualUser.users.offer(toString());
				log.info(toString()+"注册成功,当前人数："+TestVirsualUser.users.size());
				new File(path).delete();
				new File(path.substring(0,path.lastIndexOf("."))+".txt").delete();
				regist = true;
			}else{
				log.debug("验证码不正确");
			}
		} catch (Exception e) {
			if(path != null){
				new File(path).delete();
				new File(path.substring(0,path.lastIndexOf("."))+".txt").delete();
			}
			throw new Exception("regist():"+e.getMessage());
		}
		
	}
	public void registFull() throws Exception{
		try{
			get("/memapp/doApplyConfirm");
			String content = get("/memapp/memberRegFull");
			Document doc = Jsoup.parse(content);
			String memId = doc.getElementsByAttributeValue("name", "memId").get(0).val();
			Element examType = doc.getElementById("examType");
			List<String> examTypeOptions = select(examType.html(), "");
			Params<String,String> params  = new Params<String,String>();
			params.put("memId", memId);
			params.put("flag", "");
			params.put("isStudent", "1");
			params.put("sex", ""+(Integer.valueOf(idCard.substring(16,17))%2==0?2:1));
			params.put("issf", ""+1);
			params.put("idType", ""+1);
			params.put("sexRd", ""+2);
			params.put("nation", "01");   // TODO SHOULD BY RANDOM
			params.put("partisan", ""+1);// TODO SHOULD BY RANDOM
			params.put("birth", "struct");
			params.put("birth_year", idCard.substring(6,10));
			params.put("birth_month", ""+Integer.valueOf(idCard.substring(10,12)));
			params.put("birth_year", ""+Integer.valueOf(idCard.substring(12,14)));
			params.put("hometown.id", ""+1);// TODO SHOULD BY RANDOM
			params.put("examType",examTypeOptions.get(new Random().nextInt(examTypeOptions.size()))); 
			params.put("issfRd", ""+1);
			params.put("school", "北京大学"); // TODO SHOULD BY RANDOM
			params.put("studRd", ""+1);
			params.put("learningStyle", ""+1); // TODO SHOULD BY RANDOM
			params.put("dept", "软件工程学院计算机专业"); // TODO SHOULD BY RANDOM
			params.put("eduLevel", ""); // TODO SHOULD BY RANDOM
			params.put("educationCode", ""); // TODO SHOULD BY RANDOM
			params.put("eduDegree", ""); // TODO SHOULD BY RANDOM
			params.put("degreeCode", ""); // TODO SHOULD BY RANDOM
			params.put("workYear", ""); // TODO SHOULD BE BORTH_YEAR + 25
			params.put("email", email);
			params.put("telephone", "");  // TODO SHOULD BY RANDOM
			params.put("mbphone", "13784380988"); // TODO SHOULD BY RANDOM
			params.put("postcode", postcode);
			params.put("address", address);
			params.put("keys", "1-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "21-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "22-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "3-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "41-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "42-150"); // TODO SHOULD BY RANDOM
			params.put("keys", "5-150"); // TODO SHOULD BY RANDOM
			
			
			
			postMulti("/memapp/memberRegFullDo", params);
		}catch(Exception e){
			throw new Exception("registFull:"+e.getMessage());
		}
	}
	public void uploadPhoto() throws Exception{
		try{
			//imageSrc()
			Params<String,String> params = new Params<String,String>();
			params.put("file:image", "D:\\java-work\\InfoCreator\\src\\android_logo1.jpg"); // TODO SHOULD BY RANDOM
			String content = postMulti("/memapp/imgsrc",params);
			
			//imageCorp()
			//x1=5&y1=13&w=180&h=240&src=path&format=jpg
			Document doc = Jsoup.parse(content);
			String path = doc.getElementById("src").val();
			params = new Params<String,String>();
			params.put("x1","5");
			params.put("y1","13");
			params.put("w","180");
			params.put("h","240");
			params.put("src",path);
			params.put("format","jpg");
			post("/memapp/img",params);
		}catch(Exception e){
			throw new Exception("uploadPhoto:"+e.getMessage());
		}
	}
	public void apply() throws Exception{
		try{
			// select orginfo
			String content = get("/memapp/applyExamPlan");
			Document doc = Jsoup.parse(content);
			Element provinceSelect = doc.getElementById("province.id");
			String prohtml = provinceSelect.html();
			List<String> proOption = select(prohtml,"000");
			if(proOption.size()==0) throw new Exception("没有省份可选");
			String proCode = proOption.get(new Random().nextInt(proOption.size()));
			String citySelect = get("/common/examRangeAjax?provinceId="+proCode);
			List<String> cityOption = toList(citySelect,"");
			if(cityOption.size()==0) throw new Exception("没有找到省份下的考区");
			String cityCode = cityOption.get(new Random().nextInt(cityOption.size()));
			// select examCourse
			List<String> planIds = new ArrayList<String>();
			String planType = "";
			Elements elements = doc.getElementsByAttributeValue("name", "planId");
			for(int i=0;i<elements.size();i++){
				Element e = elements.get(i);
				planType = e.attr("type");
				planIds.add(e.val().trim());
			}
			if(elements.size() == 0) throw new Exception("没有考试科目，可用来报名");
			// then apply
			//planIds=22&province.id=1&city.id=136&planId=22
			Params<String,String> params  = new Params<String,String>();
			params.put("province.id",proCode);
			params.put("city.id",cityCode);
			if(planType.equals("checkbox")){
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<planIds.size();i++){
					if(i!=0){
						sb.append(",");
					}
					sb.append(planIds.get(i));
					params.put("planId",planIds.get(i));
				}
				params.put("planIds",sb.toString());
			}else{
				String planId = planIds.get(new Random().nextInt(planIds.size()));
				params.put("planIds",planId);
				params.put("planId",planId);
			}
			post("/memapp/applyExamDo",params);
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
}


