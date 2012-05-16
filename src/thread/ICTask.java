package thread;

import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public abstract class ICTask implements Runnable{

	private String name;
	protected Dao dao;
	public final Log log = Logs.getLog(this.getClass());
	public ICTask(){
		name = "task"+this.hashCode();
	}
	public ICTask(String name){
		this.name = name;
	}
	public void setDao(Dao dao){
		this.dao = dao;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Dao getDao() {
		return dao;
	}
	
	protected void insert(String tname,Map<String,Object> attrs){
		if(attrs == null || attrs.size()==0 || Strings.isEmpty(tname)) return ;
		Chain c = null;
		for(String key : attrs.keySet()){
			if(c==null){
				c = Chain.make(key, attrs.get(key));
			}else{
				c = c.add(key, attrs.get(key));
			}
		}
		dao.insert(tname, c);
	}
	protected Map<String,Object> newMap(){
		return new HashMap<String,Object>();
	}
}
