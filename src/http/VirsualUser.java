package http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VirsualUser extends UserTask{
	
	private String username = null;
	private List<UserTask> tasks = new ArrayList<UserTask>();
	public void setUsername(String username){
		this.username = username;
	}
	public String getUsername(){
		return username;
	}
	
	public void login(String uri,Map<String, Object> params) throws Exception{
		get(host+uri);
	}
	public void logout(String uri) throws Exception{
		get(host+uri);
	}
	public void addTask(UserTask task){
		tasks.add(task);
	}
	public void run() {
		if(tasks.size()>0){
			try{
			for(int i=0;i<tasks.size();i++){
				tasks.get(i).setHost(getHost());
				tasks.get(i).run();
			}
			}catch(Exception e){
				System.out.println("用户："+username+":操作过程中出错了");
			}
		}
	}
}
