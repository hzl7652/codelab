package concurrent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FivePersonAccess {
	// 该方法只允许5个用户同时访问， 多于5人时需等待前面的人完成
	public static void main(String[] args) {
		final FivePersonAccess fpa = new FivePersonAccess();
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while(fpa.total.get() > 0){
					System.out.println(String.format("fpa.total: %d ,active threads: %d ",fpa.total.get(),Thread.activeCount()));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		class MR implements Runnable{
			int num = 0;
			FivePersonAccess fpa = null;
			public MR(int num,FivePersonAccess fpa){
				this.num = num;
				this.fpa = fpa;
			}
			public void run(){
				fpa.fivePerson(num);
			}
		}
		for(int i=0;i<100;i++){
			new Thread(new MR(i,fpa)).start();
		}
	}
	AtomicInteger total = new AtomicInteger(0);
	int limit = 5;
	Object lock = new Object();
	public void fivePerson(int num){
		try{
			synchronized (lock) {
				while((total.get()+1) > limit)
					lock.wait();
				total.incrementAndGet();
			}
			Thread.sleep(new Random().nextInt(1000)+500);
			System.err.println(num);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			synchronized(lock){
				total.decrementAndGet();
				lock.notify();
			}
		}
	}
}
