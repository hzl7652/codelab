package concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class TestCollection {
	@Test
	public void testMap(){
		final Map<Integer, Integer> map = new HashMap<Integer, Integer>();  //  test error
		final Map<Integer, Integer> map2 = Collections.synchronizedMap(new HashMap<Integer, Integer>()); // test success
		final Map<Integer, Integer> map3 = new ConcurrentHashMap<Integer, Integer>();   // test success
		final ConcurrentMap<Integer, Integer> map4 = new ConcurrentHashMap<Integer, Integer>();  // test success
		int max = 10000;
		int con = 100;
		execute(con,max,new Runnable(){
			public void run(){
				try{
					int r = new Random().nextInt(10);
					map.put(1, r);
					Thread.sleep(10);
					map.remove(1);
					Thread.sleep(10);
					r = new Random().nextInt(10);
					map.put(2, r);
					Thread.sleep(10);
					map.remove(2);
					
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		});
		Assert.assertTrue(map.size()!=0);
		
		execute(con,max,new Runnable(){
			public void run(){
				try{
					int r = new Random().nextInt(10);
					map2.put(1, r);
					Thread.sleep(10);
					map2.remove(1);
					Thread.sleep(10);
					r = new Random().nextInt(10);
					map2.put(2, r);
					Thread.sleep(10);
					map2.remove(2);
					
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		});
		Assert.assertEquals(0, map2.size());
		
		execute(con,max,new Runnable(){
			public void run(){
				try{
					int r = new Random().nextInt(10);
					map3.put(1, r);
					Thread.sleep(10);
					map3.remove(1);
					Thread.sleep(10);
					r = new Random().nextInt(10);
					map3.put(2, r);
					Thread.sleep(10);
					map3.remove(2);
					
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		});
		Assert.assertEquals(0, map3.size());
		
		execute(con,max,new Runnable(){
			public void run(){
				try{
					int r = new Random().nextInt(10);
					map4.put(1, r);
					Thread.sleep(10);
					map4.remove(1);
					Thread.sleep(10);
					r = new Random().nextInt(10);
					map4.put(2, r);
					Thread.sleep(10);
					map4.remove(2);
					
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		});
		Assert.assertEquals(0, map4.size());
		
		
	}
	@Test
	public void testList(){
		
	}
	@Test
	public void testQueue(){
		
	}
	private void execute(int conMax ,int max,final Runnable runnable){
		final AtomicInteger total = new AtomicInteger(0);
		System.out.println(total.intValue());
		Runnable newrun = new Runnable() {
			@Override
			public void run() {
				total.incrementAndGet();
				runnable.run();
			}
		};
		ExecutorService es = Executors.newFixedThreadPool(conMax);
		for(int i=0;i<max;i++){
			es.submit(newrun);
		}
		es.shutdown();
		while(total.intValue() != max)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
