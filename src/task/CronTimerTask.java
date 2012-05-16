package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CronTimerTask {

	public static final Map<String,TimerAndTask> tasksList = new HashMap<String, TimerAndTask>();
	
	public static void addTask(String name,Runnable task,Date start,long delay){
		if(!tasksList.containsKey(name)){
			Timer timer = new Timer(true);
			MyTask myTask = new MyTask(task,start,delay);
			timer.schedule(myTask, start,delay);
			tasksList.put(name, new TimerAndTask(timer, myTask));
		}
	}
	public static Map<String,MyTask> listTask(){
		Map<String,MyTask> results = new HashMap<String, MyTask>();
		for(String name: tasksList.keySet()){
			results.put(name, tasksList.get(name).task);
		}
		return results;
	}
	public static boolean cancelTask(String name){
		TimerAndTask tat = tasksList.remove(name);
		if(tat != null){
			try{
				tat.task.cancel();
				tat.timer.cancel();
				return true;
			}catch(Exception e){
				return false;
			}
		}else{
			return true;
		}
	}
	public static String taskStatus(String name){
		TimerAndTask tat = tasksList.get(name);
		if(tat != null){
			return tat.task.status;
		}else
			return null;
	}
}
class TimerAndTask {
	protected Timer timer;
	protected MyTask task;
	public TimerAndTask(Timer timer,MyTask task){
		this.timer = timer;
		this.task = task;
	}
}
class MyTask extends TimerTask{
	private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	protected String status = "任务尚未开始";
	protected Date startTime = null;
	protected long delay = 0;
	private Runnable task = null;
	public MyTask(Runnable task,Date start,long delay){
		this.task = task;
		this.startTime = start;
		this.delay = delay;
	}
	@Override
	public void run() {
		try{
			status = sdf.format(new Date())+ "  任务"+ "开始执行";
			task.run();
			status += "," + sdf.format(new Date())+"  任务于"+"执行结束";
		}catch(Exception e){
			status = "," + sdf.format(new Date())+"  任务执行失败，ERROR STATUS:"+e.getMessage();
		}
	}
}
