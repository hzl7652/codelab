package companyTask;

import java.util.Date;
import java.util.Random;

import org.nutz.dao.Chain;

import thread.ICTask;

public class IntervieweeTask extends ICTask {
	
	private long id ;
	public IntervieweeTask(String name,long id){
		super("插入2000条考试记录："+name);
		this.id = id;
	}
	private Random r = new Random();
	public void run(){
		try{
		for(int i=0;i<5000;i+=2){
			
			dao.insert("interviewee",Chain.make("version", 0).add("apply_date", new Date())
				.add("cancel_state", "100").add("check_state","0").add("iv_course_id", 5+r.nextInt(22))
				.add("memberinfo_id", i+id).add("orginfo_id", r.nextBoolean()?21:24)
			);
		}
		}catch(Exception e){
			log.debug(e);
		}
	}
}
