package thread;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class MultiQueue {

	private static ExecutorService es = null;
	private static Integer THREAD_POOL_SIZE_DEFAULT = 10;
	private static NutDao dao ;
	private static DataSource ds ; 
	private static final Log log = Logs.getLog(MultiQueue.class);
	static {
		
		try {
			init();
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw Lang.makeThrow(e.getMessage());
		}
	}
	public static void addTask(ICTask task){
		log.debugf("任务 %s 开始运行",task.getName());
		task.setDao(dao);
		es.submit(task);
	}
	private static void init() throws Exception{
		log.debug("queue init ");
		Properties pp = new Properties();
		pp.load(MultiQueue.class.getResourceAsStream("/common.properties"));
		dao = new NutDao();
		pp.put("driverClassName", pp.getProperty("datasource.driverClassName",""));
		pp.put("url", pp.getProperty("datasource.url",""));
		pp.put("username", pp.getProperty("datasource.username",""));
		pp.put("password", pp.getProperty("datasource.password",""));
		ds = BasicDataSourceFactory.createDataSource(pp);
		dao.setDataSource(ds);
		String num = pp.getProperty("thread.pool.size");
		
		if(Strings.isEmpty(num))
			es = Executors.newFixedThreadPool(THREAD_POOL_SIZE_DEFAULT);
		else
			es = Executors.newFixedThreadPool(Integer.valueOf(num));
		log.debug("queue init success");	
	}
	public static void shutdown(){
		if(!es.isShutdown()){
			es.shutdown();
		}
	}
}
