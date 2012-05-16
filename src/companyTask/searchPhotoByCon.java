package companyTask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Files;

public class searchPhotoByCon {

	private static NutDao dao ;
	private static DataSource ds ; 
	public static void main(String[] args) throws Exception {
		init();
		String srcPath = "D:\\work-info\\面试照片";
		String destpath = "D:\\work-info\\面试照片-bak";
		List<Record> records= dao.query("memberinfo", Cnd.wrap(" id in (select memberinfo_id from interviewee where check_state=1)"),null);
		System.out.println(records.size());
		if(records.size()>0){
			for(Record r : records){
					if(! Files.copyFile(new File(srcPath,r.getString("photo")), new File(destpath,r.getString("photo")))){
						System.out.println("error:"+r.getString("realname")+":"+r.getString("id_card")+":"+r.getString("photo"));
					}
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
		if(image.getWidth() >150 || image.getHeight()>200){
			return true;
		}else{
			return false;
		}
		
	}
}
