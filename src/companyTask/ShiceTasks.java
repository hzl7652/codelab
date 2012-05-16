package companyTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;

import thread.ICTask;
import util.Infoc;
import util.RecurArrayRandom;

public class ShiceTasks extends ICTask {

	public ShiceTasks(String name) {
		super(name);
	}

	public void run() {
		try {
			int index = 152;
//			Record r = dao.fetch("memberinfo", Cnd.wrap("order by id desc"));
//			if(r!= null && !r.isEmpty())
//				index = r.getInt("id") - 1;
//			
//			RecurArrayRandom<String> rars = new RecurArrayRandom<String>(
//					new String[] { "北京大学", "清华大学", "北京理工大学", "北京外国语大学" });
//			for (int i = 0; i < 50; i++) {
//				String idCard = Infoc.getIdCard();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//				String name = Infoc.getName();
//				Date birth = sdf.parse(idCard.substring(6, 14));
//				Map<String, Object> attrs = newMap();
//				attrs.put("version", 0);
//				attrs.put("address", Infoc.getAddress());
//				attrs.put("answer", "000000");
//				attrs.put("birth", birth);
//				attrs.put("dept", "计算机科学与技术");
//				attrs.put("email", Infoc.getEmail());
//				attrs.put("exam_type", "4");
//				attrs.put("id_type", "1");
//				attrs.put("id_card", idCard);
//				attrs.put("hometown_id", 2);
//				attrs.put("is_student", "1");
//				attrs.put("isdel", "0");
//				attrs.put("ismodify", "Y");
//				attrs.put("issf", "1");
//				attrs.put("isstop", "N");
//				attrs.put("learning_style", "2");
//				attrs.put("nation", "04");
//				attrs.put("partisan", "3");
//				attrs.put("password", "yYSu0BSux2I6VPBZHaB6hf1Ldi0=");
//				attrs.put("photo", "2011/" + name + ".jpg");
//				attrs.put("postcode", Infoc.getPostcode());
//				attrs.put("protect", "您的小学校名是?");
//				attrs.put("realname", name);
//				attrs.put("regtime", new Date());
//				attrs.put("school", rars.next());
//				attrs.put("sex",
//						Integer.valueOf(idCard.substring(16, 17)) % 2 == 1 ? 1
//								: 2);
//				insert("memberinfo", attrs);
//			}
//			for (int i = 0; i < 50; i++) {
//				String idCard = Infoc.getIdCard();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//				String name = Infoc.getName();
//				Date birth = sdf.parse(idCard.substring(6, 14));
//				Map<String, Object> attrs = newMap();
//				attrs.put("version", 0);
//				attrs.put("address", Infoc.getAddress());
//				attrs.put("answer", "000000");
//				attrs.put("birth", birth);
//				attrs.put("work_year", "1988");
//				attrs.put("email", Infoc.getEmail());
//				attrs.put("exam_type", "3");
//				attrs.put("id_type", "1");
//				attrs.put("id_card", idCard);
//				attrs.put("hometown_id", 2);
//				attrs.put("is_student", "2");
//				attrs.put("isdel", "0");
//				attrs.put("ismodify", "Y");
//				attrs.put("issf", "1");
//				attrs.put("isstop", "N");
//				attrs.put("learning_style", "2");
//				attrs.put("nation", "04");
//				attrs.put("partisan", "3");
//				attrs.put("password", "yYSu0BSux2I6VPBZHaB6hf1Ldi0=");
//				attrs.put("photo", "2011/" + name + ".jpg");
//				attrs.put("postcode", Infoc.getPostcode());
//				attrs.put("protect", "您的小学校名是?");
//				attrs.put("realname", name);
//				attrs.put("regtime", new Date());
//				attrs.put("school", rars.next());
//				attrs.put("sex",
//						Integer.valueOf(idCard.substring(16, 17)) % 2 == 1 ? 1
//								: 2);
//				attrs.put("edu_level", "2");
//				attrs.put("edu_degree", "2");
//				insert("memberinfo", attrs);
//			}
//			
			RecurArrayRandom<Integer> rar = new RecurArrayRandom<Integer>(
					new Integer[] { 21 });
			for (int i = 0; i < 50; i++) {
				index++;
				Map<String, Object> attrs = newMap();
				attrs.put("version", 0);
				attrs.put("apply_date", new Date());
				attrs.put("cancel_state", "100");
				attrs.put("check_state", 0);
				attrs.put("exam_course_plan_id", 7);
				attrs.put("isdel", 0);
				attrs.put("memberinfo_id", index);
				attrs.put("orginfo_id", rar.next());
				insert("applicant", attrs);
			}
//			for (int i = 0; i < 50; i++) {
//				index++;
//				Map<String, Object> attrs = newMap();
//				attrs.put("version", 0);
//				attrs.put("apply_date", new Date());
//				attrs.put("cancel_state", "100");
//				attrs.put("check_state", 0);
//				attrs.put("exam_course_plan_id", 8);
//				attrs.put("isdel", 0);
//				attrs.put("memberinfo_id", index);
//				attrs.put("orginfo_id", rar.next());
//				insert("applicant", attrs);
//				
//			}
		} catch (Exception e) {
			log.debug("任务出现错误\n" + e.getMessage());
		}
	}

}
