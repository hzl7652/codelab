package test;

import org.junit.Test;
import org.nutz.lang.random.RecurArrayRandom;

import thread.ICTask;
import thread.MultiQueue;


public class RandomTest {

	public static util.RecurArrayRandom rar = new util.RecurArrayRandom(new String[]{"126.com","sina.com.cn","soho.com","gmail.com","163.com","yahoo.com.cn","qq.com"});
	public static RecurArrayRandom<String> nutzrar = new RecurArrayRandom<String>(new String[]{"126.com","sina.com.cn","soho.com","gmail.com","163.com","yahoo.com.cn","qq.com"});
	
	@Test
	public void testNutzRecurArrayRandom(){
		for(int i=0;i<51;i++)
			MultiQueue.addTask(new ICTask("task"+i){
				@Override
				public void run() {
					for(int i=0;i<1000000;i++){
						nutzrar.next();
					}
					
				}
			});
	}
	@Test
	public void testMyRecurArrayRandom(){
		for(int i=0;i<51;i++)
			MultiQueue.addTask(new ICTask("task"+i){
				@Override
				public void run() {
					for(int i=0;i<1000000;i++){
						rar.next();
					}
					
				}
			});
	}
}
