package Excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import util.Infoc;

public class HankaoBatchApply {

	public static void main(String[] args) {
		InputStream is = HankaoBatchApply.class.getResourceAsStream("HanKaoBasicInput.xls");
		String outputName = "output_10000.xls";
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
		for(int i=0;i<10000;i++){
			Row row = sheet.createRow(rowIndex++);
			generateRow(row);
		}
		OutputStream os = new FileOutputStream(outputName);
		wb.write(os);
		os.close();
	}
	public static String[] sex = "男[1],女[2]".split(",");
	public static String[] kemu = "1,2,3,4,5,6".split(",");
	public static String[] kaodian = "110103,110105,430101,110101,110102,430102".split(",");
	public static String[] hangye = "行政管理[01],农渔林矿[02],艺术娱乐[03],银行金融[04],餐饮业[05],建筑业[06],手工业[07],教育业[08],IT业[09],健康及社会保障[10],安装运维[11],法律服务[12],制造业[13],个体服务[14],零售业[15],科技业[16],通讯媒体[17],运输业[18],公用事业[19],批发业[20],其他行业[21]".split(",");
	public static String[] zhiye = "教师[0],公务员[1],专业技术人员[2],工人[3],农民[4],军人[5],无业人员[6],干部[8],自由职业者[9],学生[10],私营企业者[11],公司职员[12],其他[13]".split(",");
	public static String[] xueli = "研究生[1],大学本科[2],大学专科[3],中师[4],幼师[5],其他中专[6],大学在读[7]".split(",");
	public static String[] xuewei = "博士[1],硕士[2],学士[3],无学位[4]".split(",");
	public static String[] mizu = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,97,98".split(",");
	public static String[] idType = "身份证[1],军官证[2],护照[3],港澳居民身份证[4]".split(",");
	public static String[] provinceCode = "北京[11],天津[12],河北[13],山西[14],内蒙古[15],辽宁[21],吉林[22],黑龙江[23],上海[31],江苏[32],浙江[33],安徽[34],福建[35],江西[36],山东[37],河南[41],湖北[42],湖南[43],广东[44],广西[45],海南[46],重庆[50],四川[51],贵州[52],云南[53],西藏[54],陕西[61],甘肃[62],青海[63],宁夏[64],新疆[65],台湾[71],香港[81],澳门[82],国外[99]".split(",");
	
	
	public static Random r = new Random();
	public static void generateRow(Row row) throws IOException{
		int colIndex = 0;
		String idCard = Infoc.getIdCard();
		String birth = idCard.substring(6,14);
		birth =  birth.substring(0,4)+"-"+birth.substring(4,6)+"-"+birth.substring(6,8); 
		generateCell(row, colIndex++, Infoc.getName()); // 真实姓名
		generateCell(row, colIndex++, sex[r.nextInt(sex.length)]);  // 性别
		generateCell(row, colIndex++, mizu[r.nextInt(mizu.length)]); // 民族
		generateCell(row, colIndex++, birth);    // 出生日期
		generateCell(row, colIndex++, idType[0]); // 证件类型
		generateCell(row, colIndex++, idCard);  // 证件号码
		generateCell(row, colIndex++, provinceCode[r.nextInt(provinceCode.length)]);  // 户籍所在省代码
		generateCell(row, colIndex++, xueli[r.nextInt(xueli.length)]);  // 最高学历
		generateCell(row, colIndex++, xuewei[r.nextInt(xuewei.length)]);  // 最高学位
		generateCell(row, colIndex++, "18210638294");  // 手机号
		generateCell(row, colIndex++, Infoc.getEmail());  // email
		generateCell(row, colIndex++, Infoc.getAddress());  // 通信地址
		generateCell(row, colIndex++, "");  // 电话号码  empty
		generateCell(row, colIndex++, "");  // 邮编  empty
		generateCell(row, colIndex++, hangye[r.nextInt(hangye.length)]);  // 行业
		generateCell(row, colIndex++, zhiye[r.nextInt(zhiye.length)]);  // 职业
		generateCell(row, colIndex++, kemu[r.nextInt(kemu.length)]);  // 报考科目
		generateCell(row, colIndex++, kaodian[r.nextInt(kaodian.length)]);  // 考点
	}
	public static void generateCell(Row row,int colIndex,String value){
		Cell cell = row.createCell(colIndex);
		cell.setCellValue(value);
	}
}
