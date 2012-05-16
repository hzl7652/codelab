package poi;

import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.nutz.dao.Chain;
import org.nutz.dao.impl.NutDao;

import thread.MultiQueue;

public class Interviewee {
	
	public static NutDao dao ;
	
	public static void init() throws Exception{
		Properties pp = new Properties();
		pp.load(MultiQueue.class.getResourceAsStream("/common.properties"));
		dao = new NutDao();
		pp.put("driverClassName", pp.getProperty("datasource.driverClassName",""));
		pp.put("url", pp.getProperty("datasource.url",""));
		pp.put("username", pp.getProperty("datasource.username",""));
		pp.put("password", pp.getProperty("datasource.password",""));
		DataSource ds = BasicDataSourceFactory.createDataSource(pp);
		dao.setDataSource(ds);
	}
	public static void main(String[] args) throws Exception {
		
		init();
		for(int i=101;i<1000;i+=2){
			dao.insert("interviewee",Chain.make("version", 0).add("orginfo_id", 21).
					add("apply_date", new Date()).add("check_state", 0).add("cancel_state", "000").add("iv_course_id", 1).add("memberinfo_id", i));
		}
	}
}
