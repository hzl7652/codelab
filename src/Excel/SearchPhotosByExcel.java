package Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SearchPhotosByExcel {

	static String outputName = "D:\\work-info\\华师大\\My JPEGs";
	public static void main(String[] args) throws FileNotFoundException {
		InputStream is = new FileInputStream("D:\\work-info\\华师大\\310002.xls");
		try {
			createTheExportExcel(is, outputName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createTheExportExcel(InputStream is,String outputName) throws Exception{
		Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheet("Sheet1");
		if(sheet == null) return ;
		int rowIndex = 1;
		boolean flag = true;
		while(flag){
			Row row = sheet.getRow(rowIndex++);
			if(row == null) break;
			flag = generateRow(row);
		}
		
	}

	
	public static Random r = new Random();
	public static boolean generateRow(Row row) throws IOException{
			if(! generateCell(row,0)){
				return false;
			}
		return true;
	}
	static int i=1;
	public static boolean generateCell(Row row,int colIndex){
		Cell cell  = row.getCell(5);
		if(cell == null) return false;
		String value = cell.getStringCellValue();
		if(value == null || "".equals(value.trim())){
			return false;
		}
		value = value.trim();
		i++;
		if(! new File(outputName,value+".jpg").exists()){
			System.out.println(row.getCell(0).getStringCellValue()+":"+value+":"+(i));
		}
		return true;
	}
}
