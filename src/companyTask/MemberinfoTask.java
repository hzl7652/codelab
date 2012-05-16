package companyTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nutz.dao.Chain;

import thread.ICTask;
import util.Infoc;

public class MemberinfoTask extends ICTask{
	
	public MemberinfoTask(String name) {
		super("插入2000条考生记录："+name);
	}
	
	@Override
	public void run() {
		for(int i=0;i<2000;i++){
			try{
				String idCard = Infoc.getIdCard();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String name = Infoc.getName();
				Date birth = sdf.parse(idCard.substring(6,14));
				dao.insert("memberinfo", Chain.make("version", 0).add("address", Infoc.getAddress())
						.add("answer", "000000").add("birth", birth).add("dept","计算机科学与技术").add("email", Infoc.getEmail())
						.add("exam_type", "4").add("id_type", "1").add("id_card", idCard).add("hometown_id", 2)
						.add("is_student", "1").add("isdel", "0").add("ismodify", "Y").add("issf", "1").add("isstop", "N")
						.add("learning_style", "2").add("nation", "04").add("partisan", "3").add("password", "yYSu0BSux2I6VPBZHaB6hf1Ldi0=")
						.add("photo", "2011/"+name+".jpg").add("postcode", Infoc.getPostcode()).add("protect", "您的小学校名是?")
						.add("realname",name).add("regtime", new Date()).add("school","北京大学").add("sex", Integer.valueOf(idCard.substring(16,17))%2==1?1:2)
				);
			}catch(IOException e){
				log.debugf("%s 任务发生io异常,错误代码 Infoc.getIdCard()",getName());
			} catch (ParseException e) {
				log.debugf("%s 任务发生io异常,错误代码 sdf.parse(idCard.substring(6,14))",getName());
			}
		}
	}
}
