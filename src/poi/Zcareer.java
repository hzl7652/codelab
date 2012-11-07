package poi;

import java.io.FileInputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.nutz.dao.Chain;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Strings;

import thread.MultiQueue;

public class Zcareer {
	
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
		Workbook wb = WorkbookFactory.create(new FileInputStream("F:/workshop/apply/中职中专学科专业列表.xls"));
		
		Sheet sheet = wb.getSheet("Sheet1");
		if(sheet == null) return ;
		Row row = null; 
		int rowIndex = 0;
		String dalei = "";
		while ( (row = sheet.getRow(rowIndex++)) != null){
			Cell cell = row.getCell(0);
			Cell cell2 = row.getCell(1);
			if(cell != null && cell2 != null && !Strings.isEmpty(cell.getStringCellValue()) && !Strings.isEmpty(cell2.getStringCellValue())){
				String value1 = cell.getStringCellValue().trim();
				if(value1.length() ==1){
					continue;
				}else if(value1.length() ==3){
					dalei = value1;
					continue;
				}else{
					String value2 = cell2.getStringCellValue().trim();
					dao.insert("zcareer",Chain.make("version", 0).add("big_type", dalei).add("small_type", value1+"-"+value2));
				}
			}else{
				break;
			}
		}
		System.out.println(rowIndex);
	}
}
