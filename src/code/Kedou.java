package code;

import java.io.UnsupportedEncodingException;

public class Kedou {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String test = "/ۨۤۜۘۙۙۛۖۤۡۗۗۘۚۢۛۜۛۡۖۢ۠ۤ۫ۖۗۢ۫ۘۨ۠۫۫ۧۗۢ۠۟ۖۗۨۗۨۨ۬ۢۖۤ۟۠ۧۨۘۗۡۗۖۜۛ۫۟ۧ";
		System.out.println(str2Hex(test));
		StringBuffer sb = new StringBuffer();
		sb.append((byte)0x2F);
		sb.append((byte)0xDB);
		sb.append((byte)0xA8);
		sb.append((byte)0xDB);
		sb.append((byte)0xA4);
		sb.append((byte)0xDB);
		sb.append((byte)0x9C);
		sb.append((byte)0xDB);
		sb.append((byte)0x98);
		sb.append((byte)0xDB);
		sb.append((byte)0x99);
		sb.append((byte)0xDB);
		sb.append((byte)0x99);
		sb.append((byte)0xDB);
		sb.append((byte)0x9B);
		sb.append((byte)0xDB);
		
		System.out.println(sb.toString());
		System.out.println(strAddEmpty(test));
	}
	static final String[] hexStr = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F".split(","); 
	public static String str2Hex(String source) throws UnsupportedEncodingException{
		byte[] bytes = source.getBytes("utf-8");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<bytes.length;i++){
			sb.append(hexStr[(bytes[i]&0xF0)>>4]);
			sb.append(hexStr[(bytes[i]&0x0F)]);
			sb.append("  ");
			if((i+1)%16==0){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	public static String strAddEmpty(String source){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<source.length();i++){
			sb.append(source.charAt(i)).append("|\t|");
			if((i+1)%16 == 0){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
