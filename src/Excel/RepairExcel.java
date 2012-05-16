package Excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class RepairExcel {

	public static void main(String[] args) throws FileNotFoundException {
		InputStream is = new FileInputStream("D:\\work-info\\310002-bak.xls");
		String outputName = "D:\\work-info\\310002-bak-bak.xls";
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
		OutputStream os = new FileOutputStream(outputName);
		wb.write(os);
		os.close();
	}

	
	public static Random r = new Random();
	public static boolean generateRow(Row row) throws IOException{
		int result = 0;
		for(int i=0;i<18;i++){
			if(! generateCell(row,i)){
				result++;
			}
		}
		if(result == 18){
			return false;
		}else{
			return true;
		}
	}
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	public static boolean generateCell(Row row,int colIndex){
		Cell cell = row.getCell(colIndex);
		if(cell == null){
			return false;
		}
		if(colIndex == 16){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return true;
		}
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			if(colIndex == 3){
				Date dvalue = cell.getDateCellValue();
				cell.setCellValue(sdf.format(dvalue));
				return true;
			}
			Double dvalue = cell.getNumericCellValue();
			if(dvalue == null) return false;
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			String value = cell.getStringCellValue();
			if(value == null || "".equals(value.trim())){
				return false;
			}
		}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
			String value = cell.getStringCellValue();
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value);
		}else{
			System.out.println("other--"+row.getRowNum()+":"+colIndex);
		}
		return true;
	}
}
