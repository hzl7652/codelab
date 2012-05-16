package util;

import java.util.Random;

public class RecurArrayRandom<T>{
	
	private T[] result ;
	private Random r = new Random();
	public RecurArrayRandom(T[] result){
		this.result = result;
	}
	public T next(){
		if(result == null || result.length ==0) return null;
		return result[r.nextInt(result.length)];
	}
}
