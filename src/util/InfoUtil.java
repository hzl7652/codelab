package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 工具类，如加载data，md5校验等
 * @author 王成
 *
 */
public class InfoUtil {

	private static List<String> addrCode = new ArrayList<String>();
	private static Map<String,String> addrMap = new HashMap<String, String>(); 
	private static Random r = new Random();
	private static Log log = Logs.getLog(InfoUtil.class);
	private static final String[] xing = {"张","王","李","赵","陈","杨","吴","刘","黄","周","徐","朱",
			"林","孙","马","高","胡","郑","郭","萧","谢","何","许","宋","沈","罗","韩","邓","梁","叶"}; 
	private static final String[] ming = {
		"安安","荌荌","安卉","娜","安妮","安然","傲冬","傲晴","傲雪","白雪","白云","碧螺","碧菡","碧玉","蓝","冰冰","绿","文","采萱","芳茵","芳蕙","芳春","芳洲",
		"初雪","春华","春雪","含烟","含玉","涵菡","晗蕾","涵韵","晗玥","寒凝","寒香","寒雁","和悌","美","芳芳","方方","芳菲","芳华","芳馨","芳泽","芳馥","芳懿","才哲",
		"才俊","和","成弘","化","成济","礼","成龙","仁","成双","天","成文","成业","益","成荫","成","安和","安康","安澜","安宁","平","安然","安顺",
		"德运","德泽","德明","安翔","安晏","安宜","安怡","安易","安志","昂然","昂雄","德本","海","德厚","德华","德辉","德惠","德容","德润","德","馨","曜",
		"德业","德义","德庸","德佑","德宇","德元","米琪","梦凡","梦菲","梦菡","梦露","梦琪","梦秋","梦竹","妙晴","玛丽","茉莉"
	};
	
	/**
	 *  取得区县代码
	 * @return
	 * @throws IOException 
	 */
	public static String getAddrCode() throws IOException{
		// 首先加载区县代码，若没有 抛出异常，这个必须得有
		if(addrCode.size() == 0){
			synchronized (addrCode) {
				if(addrCode.size() == 0){
					InputStream is = InfoUtil.class.getResourceAsStream("/data/datacode.txt");
					if(is == null ) throw new IOException("datacode.txt not found");
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String buffer = null;
					int num = 0;
					while((buffer = br.readLine()) != null){
						num++;
						if(num<3) continue;
						if(buffer.length()< 6) continue;
						else if(! "00".equals(buffer.substring(4, 6))){
							addrCode.add(buffer.substring(0,6));
							addrMap.put(buffer.substring(0,6), buffer.substring(6).trim());
						}else 
							addrMap.put(buffer.substring(0,6), buffer.substring(6).trim());
							continue;
					}
					
					log.debug("共添加"+addrCode.size()+"个区县代码");
				}
				if(addrCode.size() == 0){
					throw new IOException("datacode must be empty,please check it");
				}
			}
		}
		return addrCode.get(r.nextInt(addrCode.size()));
	}
	/**
	 * 取得随机的时间 应可定义转换格式 如 yyyy-MM-dd hh-mm-ss:sss
	 * 建议直接用str进行拼接
	 * @return
	 */
	public static String getDate(){
		StringBuffer sb = new StringBuffer();
		// 取得年份 年份（1900-2011）
		sb.append(r.nextInt(25)+1970);
		// 取得月份和日期 （暂不考虑闰年）
		int month = r.nextInt(12)+1;
		sb.append(leftPadding(month+"",2,"0"));
		switch(month){
		case 1:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 2:
			sb.append(leftPadding((r.nextInt(28)+1)+"",2,"0")); break;
		case 3:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 4:
			sb.append(leftPadding((r.nextInt(30)+1)+"",2,"0")); break;
		case 5:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 6:
			sb.append(leftPadding((r.nextInt(30)+1)+"",2,"0")); break;
		case 7:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 8:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 9:
			sb.append(leftPadding((r.nextInt(30)+1)+"",2,"0")); break;
		case 10:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		case 11:
			sb.append(leftPadding((r.nextInt(30)+1)+"",2,"0")); break;
		case 12:
			sb.append(leftPadding((r.nextInt(31)+1)+"",2,"0")); break;
		}
		return sb.toString();
	}
	/**
	 *  取一个数字
	 */
	public static String getRandomNum(int num){
		return leftPadding(r.nextInt(num)+"", ((num-1)+"").length(), "0");
	}
	/**
	 * 
	 * 身份证号码最后一位的校验码
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
	 * @param str 前置：str.length() == 17
	 * @return
	 */
	public static char getIdCardCheckCode(String str){
		int[] codes = {7 ,9, 10, 5, 8, 4, 2, 1, 6,3, 7, 9, 10, 5, 8, 4, 2};
		char[] ccodes = {'1','0','X','9','8','7','6','5','4','3','2'};
		int sum = 0;
		for(int i=0;i<17;i++){
			sum += Integer.valueOf(str.substring(i,i+1))*codes[i];
		}
		int result = sum%11;
		return ccodes[result];
		
	}
	/**
	 * 将字符串左补 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String leftPadding(String str,int len,String ch){
		if(str == null ) str = "";
		StringBuffer sb = new StringBuffer();
		if(str.length()<len){
			for(int i=0;i<(len-str.length());i++)
				sb.append(ch);
			sb.append(str);
			return sb.toString();
		}else{
			return str;
		}
	}
	/**
	 * 姓名生成
	 * @return
	 */
	public static String getName(){
		return new StringBuffer().append(xing[r.nextInt(xing.length)]).append(ming[r.nextInt(ming.length)]).toString();
	}
	/**
	 * 地址生成器
	 * @return
	 * @throws IOException 
	 */
	public static String getAddress() throws IOException{
		StringBuffer sb = new StringBuffer();
		getAddrCode();
		String addr = addrCode.get(r.nextInt(addrCode.size()));
		String twoAddr = addr.substring(0,2);
		sb.append(addrMap.get(twoAddr+"0000"));
		if(!"11".equals(twoAddr) && !"12".equals(twoAddr) && !"31".equals(twoAddr) && !"50".equals(twoAddr)){
			sb.append(addrMap.get(addr.substring(0,4)+"00"));
		}
		sb.append(addrMap.get(addr)).append(r.nextInt(1000)).append("号");
		return sb.toString();
	}
	public static void main(String[] args) throws IOException {
		for(int i = 0;i<10 ;i++){
			//System.out.println(getAddrCode());
			//System.out.println(getDate());
			//System.out.println(getRandomNum(1000));
			//System.out.println(getName());
			System.out.println(getAddress());
		}
		System.out.println(getIdCardCheckCode("13068319870719103"));
	}
}
