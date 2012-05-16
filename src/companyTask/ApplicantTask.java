package companyTask;

import java.util.Date;

import org.nutz.dao.Chain;

import thread.ICTask;

public class ApplicantTask extends ICTask {
	
	private long id ;
	public ApplicantTask(String name,long id){
		super("插入2000条考试记录："+name);
		this.id = id;
	}
	public void run(){
		for(int i=0;i<2000;i++){
			dao.insert("applicant",Chain.make("version", 0).add("apply_date", new Date())
				.add("cancel_state", "100").add("check_state","0").add("exam_course_plan_id", 1).add("isdel", "0")
				.add("memberinfo_id", i+id).add("orginfo_id", 21)
			);
		}
	}
}
