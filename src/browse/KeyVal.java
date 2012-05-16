package browse;

public class KeyVal {
	private String key;
	private String val;
	
	public KeyVal(String k,String v){
		key = k;
		val = v;
	}
	public static KeyVal createKV(String k,String v){
		return new KeyVal(k,v);
	}
	
	public void key(String k){
		key = k;
	}
	public String key(){
		return key;
	}
	public void val(String v){
		val = v;
	}
	public String val(){
		return val;
	}
}