package companyTask;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Files;

public class CopyPhoto {

	private static NutDao dao ;
	private static DataSource ds ; 
	public static void main(String[] args) throws Exception {
		init();
		String srcPath = "D:\\work-info\\官网照片文件";
		String destpath = "D:\\work-info\\官网照片文件-temp-new";
		List<Record> records= dao.query("memberinfo", Cnd.wrap("exam_no is not null and id in (select memberinfo_id from applicant,exam_course_plan,exam_course where exam_course_plan_id = exam_course_plan.id and exam_course_id = exam_course.id and course_code in ('101','201') and check_state = 1 )"),null);
		System.out.println(records.size());
		if(records.size()>0){
			for(Record r : records){
				if(!Files.copyFile(new File(srcPath,r.getString("photo")), new File(destpath,r.getString("photo")))){
					System.out.println("error:"+r.getString("realname")+":"+r.getString("id_card")+":"+r.getString("photo"));
				}
//				if(! new File(srcPath,r.getString("photo")).exists()){
//					System.out.println("error:"+r.getString("realname")+":"+r.getString("id_card")+":"+r.getString("photo"));
//				}
			}
		}
		
	}
	static void  init() throws Exception{
		Properties pp = new Properties();
		dao = new NutDao();
		pp.put("url","jdbc:mysql://localhost:3306/teachdb?useUnicode=true&characterEncoding=utf-8" );
		pp.put("driverClassName", "com.mysql.jdbc.Driver");
		pp.put("username", "root");
		pp.put("password", "000000");
		ds = BasicDataSourceFactory.createDataSource(pp);
		dao.setDataSource(ds);
	}
}
