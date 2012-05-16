package companyTask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Files;

public class PatchBatchApplyPhotos {

	private static NutDao dao ;
	private static DataSource ds ; 
	public static void main(String[] args) throws Exception {
		init();
		String srcPath = "D:\\work-info\\官网照片文件";
		String destpath = "D:\\java-work\\kaowu\\teach\\web-app\\photos";
		List<File> files = scanFiles(srcPath);
		Random r = new Random();
		List<Record> records= dao.query("memberinfo", Cnd.wrap(" id in (select memberinfo_id from applicant where check_state in (0,1))"),null);
		//List<Record> records= dao.query("memberinfo", Cnd.wrap("exam_no is not null and id in (select memberinfo_id from applicant,exam_course_plan,exam_course where exam_course_plan_id = exam_course_plan.id and exam_course_id = exam_course.id and check_state = 1 )"),null);
		System.out.println(records.size());
		if(records.size()>0){
			for(Record re : records){
				if(! Files.copyFile(files.get(r.nextInt(files.size())), new File(destpath,re.getString("photo")))){
					System.out.println(re.getString("id_card")+":"+re.getString("realname")+"::出错了");
				}
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
	public static int width150160 = 0;
	public static int height200210 = 0;
	public static int all = 0;
	
	public static boolean testPhoto(File photo) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(photo);
		} catch (IOException e) {
			System.out.println("error:"+photo.getAbsolutePath());
			return false;
		}
//		if(image.getWidth() >150 || image.getHeight()>200){
//			return true;
//		}else{
//			return false;
//		}
		return true;
	}
	public static List<File> scanFiles(String dirPath){
		File dir = new File(dirPath);
		List<File> result = new ArrayList<File>();
		if(! dir.exists() || !dir.isDirectory()){
			return result;
		}
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isHidden()) continue;
			else{
				if(files[i].isDirectory()){
					if(files[i].getName().startsWith(".")) continue;
					result.addAll(scanFiles(files[i].getAbsolutePath()));
				}else{
					if(files[i].isFile()){
						if(files[i].getName().endsWith(".jpg")){
							if(testPhoto(files[i])){
								result.add(files[i]);
							}else{
								continue;
							}
						}else{
							System.out.println("other type:"+files[i].getAbsolutePath());
						}
					}
				}
			}
		}
		return result;
		
		
	}
}
