package task;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class TestCronTask {
	/**
	 * 多任务执行，通过控制台去添加任务，查看任务，取消任务，观察任务是否正常
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String taskInfo = "0:查看当前任务列表\n1:添加任务\n2:取消任务\n,请输入要执行的任务代码";
		System.out.println(taskInfo);
		while(true){
			int code = sc.nextInt();
			switch(code){
			case 0:
				Map<String ,MyTask> tasks = CronTimerTask.listTask();
				if(tasks.size() == 0){
					System.out.println("当前没有任务");
				}else{
					for(String name : tasks.keySet()){
						System.out.println(String.format("%s	%s	%d	%s", name,tasks.get(name).startTime.toString(),tasks.get(name).delay,tasks.get(name).status));
					}
				}
				System.out.println(taskInfo);
				break;
			case 1:
				sc.nextLine();
				System.out.println("输入任务名称:");
				final String name = sc.nextLine();
				CronTimerTask.addTask(name, new Runnable() {
					@Override
					public void run() {
						System.err.println(name + ":"+Thread.currentThread().getName()+":正在运行");
					}
				}, new Date(), 5000+new Random().nextInt(5000));
				System.out.println("添加完成");
				System.out.println(taskInfo);
				break;
			case 2:
				sc.nextLine();
				System.out.println("输入任务名称:");
				String name2 = sc.nextLine();
				CronTimerTask.cancelTask(name2);
				System.out.println("取消成功");
				System.out.println(taskInfo);
				break;
			default :
				System.out.println(taskInfo);
			}
		}
	}
}
