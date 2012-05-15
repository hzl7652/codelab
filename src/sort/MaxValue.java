package sort;

/**
 * 有一无序的整数数组，求其能排列组成的最大值
 * @author tt
 *
 */
public class MaxValue {
	public static void main(String[] args) {
		int[] values = {11,12,21,255,3334,2234,99,1,44,89};
		String str = test(values);
		System.out.println(str);
	}
	public static String test(int[] values){
		if(values.length == 1){
			return ""+values[0];
		}
		String[] strValues = new String[values.length];
		int maxLength = 0;
		for(int i=0;i<values.length;i++){
			strValues[i] = String.valueOf(values[i]);
			if(strValues[i].length() > maxLength){
				maxLength = strValues[i].length();
			}
		}
		for(int i=0;i<maxLength;i++){
			sort(strValues,i);
		}
		StringBuilder sb = new StringBuilder();
		for(String v : strValues){
			sb.append(v).append("  ");
		}
		return sb.toString();
	}
	public static void sort(String[] strValues,int index){
		while(true){
			boolean change = false;
			for(int i=0;i<strValues.length-1;i++){
				String first = strValues[i];
				String second = strValues[i+1];
				if(first.length() >= index+1 && second.length() >= index+1){// 数值大的直接去前边
					if(first.substring(0, index).equals(second.substring(0,index)) && Integer.valueOf(first.substring(index,index+1)) < Integer.valueOf(second.substring(index,index+1))){
						strValues[i] = second;
						strValues[i+1] = first;
						change = true;
					}else if(first.substring(0, index).equals(second.substring(0,index)) &&  Integer.valueOf(first.substring(index,index+1)) == Integer.valueOf(second.substring(index,index+1))){
						if(first.length()>second.length()){
							strValues[i] = second;
							strValues[i+1] = first;
							change = true;
						}
					}
				}else if(first.length() ==index  && second.length() >index){
					if(second.startsWith(first)){
						if(Integer.valueOf(second.substring(0,1)) < Integer.valueOf(second.substring(index,index+1))){
							strValues[i] = second;
							strValues[i+1] = first;
							change = true;
						}
					}
				}
			}
			if(!change){
				break;
			}
		}
	}
}
