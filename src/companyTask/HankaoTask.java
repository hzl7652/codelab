package companyTask;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;

import thread.ICTask;
import util.Infoc;

public class HankaoTask extends ICTask {

	@Override
	public void run() {
		// 插入20000条memberinfo数据
		try {
			Record record = dao.fetch("memberinfo", Cnd.wrap("order by id desc"));
			int index  = 0;
			if(record!= null)
				index = record.getInt("id");
			for (int i = 0; i < 20000; i++) {
				Map<String, Object> ctx = newMap();
				String name = Infoc.getName();
				String idCard = Infoc.getIdCard();

				ctx.put("version", 0);
				ctx.put("address", Infoc.getAddress());
				ctx.put("answer", "000000");
				ctx.put("birth", new Date());
				ctx.put("birth_city_id", 8);
				ctx.put("biz", "IT行业");
				ctx.put("career", "技术人员");
				ctx.put("edu_degree", "学士");
				ctx.put("edu_level", "本科");
				ctx.put("email", Infoc.getEmail());
				ctx.put("id_card", idCard);
				ctx.put("id_type", "身份证");
				ctx.put("isdel", 0);
				ctx.put("isstop", "N");
				ctx.put("mbphone", "13473737337");
				ctx.put("nation", "汉族");
				ctx.put("password", "yYSu0BSux2I6VPBZHaB6hf1Ldi0=");
				ctx.put("photo", "2011/" + name + ".jpg");
				ctx.put("postcode", Infoc.getPostcode());
				ctx.put("protect", "您的小学校名是?");
				ctx.put("realname", name);
				ctx.put("regtime", new Date());
				ctx.put("sex",
						Integer.valueOf(idCard.substring(16, 17)) % 2 == 0 ? "女"
								: "男");
				insert("memberinfo", ctx);

			}
			
			Random r = new Random();
			
			// 插入20000条applicant
			for (int i = 0; i < 20000; i++) {
				Map<String, Object> ctx = newMap();
				ctx.put("version", 0);
				ctx.put("apply_date", new Date());
				ctx.put("cancel_state", "100");
				ctx.put("check_state", 0);
				ctx.put("exam_course_plan_id", r.nextInt(2)+1);
				ctx.put("isdel", 0);
				ctx.put("memberinfo_id", index++);
				ctx.put("orginfo_id", 22);
				
				insert("applicant", ctx);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
