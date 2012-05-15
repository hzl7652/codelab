package basic;

public class InnerClass {

	private int value = 0;
	static int svalue = 1;
	class IsInner {
		
		int getValue(){
			return value;
		}
	}
	public class IsPuInner{
		public int getValue(){
			return value;
		}
	}
	private class IsPriInner{
		private int getValue(){
			return value;
		}
	}
	
	static class staticInnerClass {
		int getValue(){
			return svalue;
		}
		static int getValue2(){
			return svalue;
		}
	}
	
	public static void main(String[] args) {
		InnerClass ic = new InnerClass();
		InnerClass.IsPriInner pri = ic.new IsPriInner();
		System.out.println(pri.getValue());
		InnerClass.staticInnerClass sic = new staticInnerClass();
		System.out.println(sic.getValue());
		System.out.println(staticInnerClass.getValue2());
	}
}
