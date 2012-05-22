package thread;

public class TestInterrupt {
	public static void main(String[] args) {
		
		Thread t = new Thread(new Runnable(){
			public void run(){
				try{
					while(true){
						try{
							System.out.println("我要睡觉了");
							//Thread.sleep(100);
							System.out.println("我睡醒了");
						}catch (Exception e){
							System.out.println(e.getClass().getName());
							break;
						}finally{
							System.out.println("内部中断的");
						}
					}
				}catch(Exception e){
					System.out.println(e.getClass().getName());
				}finally{
					System.out.println("应该是被中断了");
				}
			}
		});
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
		System.out.println(t.isInterrupted());
	}
}
