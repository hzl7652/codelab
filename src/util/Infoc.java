package util;

import java.io.IOException;
import java.util.Random;

import org.nutz.lang.random.StringGenerator;

/**
 * 生成随机数据类
 * 能随机生成姓名，邮件，电话，手机，邮编，生日，身份证，住址，工作单位，民族，党派，工作性质，学校，日期等
 * 具体看一下method
 * @author 王成
 *
 */
public class Infoc {
	
	/**
	 * 生成身份证
	 * 		作用： 可以根据身份证得之一下数据： 户籍所在地，生日，男女
	 * 	身份证命名规则： 六位地区码（地区编码请看data/最新区县编码），八位生日日期（yyyyMMdd），三位顺序码（男为奇数，女为偶数，999、998、997、996为百岁老人专用），一位校验码
	 * 校验规则：
	 * 1. 对前17位数字本体码加权求和
　	 *	　公式为：S = Sum(Ai * Wi), i = 0, ... , 16
　	 * 	　其中Ai表示第i位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
　	 * 2. 以11对计算结果取模
　	 *	　Y = mod(S, 11)
　	 * 3. 根据模的值得到对应的校验码
　	 *	　对应关系为：
　	 *	　	  Y值： 0 1 2 3 4 5 6 7 8 9 10
　	 *		　校验码： 1 0 X 9 8 7 6 5 4 3 2
	 * @throws IOException 
	 */
	public static String getIdCard() throws IOException{
		
		StringBuffer sb = new StringBuffer("");
		// 得到区县代码，可指定区县，地区，省份代码
		sb.append(InfoUtil.getAddrCode());
		// 得到时间代码  随机 时间段（）
		sb.append(InfoUtil.getDate());
		// 得到顺序码  随机
		sb.append(InfoUtil.getRandomNum(1000));
		// 得到验证码
		sb.append(InfoUtil.getIdCardCheckCode(sb.toString()));
		// 拼接后返回
		return sb.toString();
	}
	
	// email生成器
	private static StringGenerator sg = new StringGenerator(6,10);
	private static util.RecurArrayRandom<String> rar = new util.RecurArrayRandom<String>(new String[]{"126.com","sina.com.cn","soho.com","gmail.com","163.com","yahoo.com.cn","qq.com"});
	public static String getEmail(){
		StringBuffer sb = new StringBuffer();
		sb.append(sg.next()).append("@").append(rar.next());
		
		return sb.toString();
	}
	/**
	 * 姓名生成器
	 * @return
	 */
	public static String getName(){
		return InfoUtil.getName();
	}
	/**
	 * 邮编生成器 6位
	 */
	private static Random r = new Random();
	public static String getPostcode(){
		return new StringBuffer().append(InfoUtil.leftPadding(r.nextInt(1000000)+"", 6, "0")).toString();
	}
	/**
	 * 地址生成器
	 * @return
	 * @throws IOException
	 */
	public static String getAddress() throws IOException{
		return InfoUtil.getAddress();
	}
	public static void main(String[] args) throws IOException {
		for(int i=0;i<10;i++){
			//System.out.println(getIdCard());
			//System.out.println(getEmail());
			System.out.println(getPostcode());
		}
	}
}
